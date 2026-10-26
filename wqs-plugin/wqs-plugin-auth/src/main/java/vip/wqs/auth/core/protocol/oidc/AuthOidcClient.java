
package vip.wqs.auth.core.protocol.oidc;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.utils.AuthStateUtils;
import vip.wqs.auth.core.enums.AuthPlatformEnum;
import vip.wqs.auth.core.protocol.base.AuthBaseClient;
import vip.wqs.auth.core.protocol.oidc.impl.wechat.AuthOidcWechatClient;
import vip.wqs.auth.core.protocol.oidc.impl.wechatmini.AuthOidcWechatMiniClient;
import vip.wqs.common.cache.CommonCacheOperator;
import vip.wqs.common.exception.CommonException;

/**
 * OIDC认证源客户端
 *
 * @author jetox
 * @date 2024/11/8 15:41
 **/
@Slf4j
public class AuthOidcClient extends AuthBaseClient<AuthOidcBaseJson> {

    public AuthOidcClient(JSONObject authSource, String authPlatform) {
        super(authSource, authPlatform);
    }

    @Override
    public AuthOidcBaseJson getAuthBaseJson() {
        return JSONUtil.toBean(this.getAuthSource(), AuthOidcBaseJson.class);
    }

    @Override
    public void valid() {
        AuthOidcBaseJson authOidcBaseJson = this.getAuthBaseJson();
        if(StrUtil.isBlank(authOidcBaseJson.getClientId())) {
            throw new CommonException("ClientId不能为空");
        }
        if(StrUtil.isBlank(authOidcBaseJson.getClientSecret())) {
            throw new CommonException("ClientSecret不能为空");
        }
    }

    public AuthRequest getAuthRequest() {
        return getAuthRequest(false);
    }

    public AuthRequest getAuthRequest(boolean ignoreCheckState) {
        String authPlatform = this.getAuthPlatform();
        AuthPlatformEnum authPlatformEnum = AuthPlatformEnum.valueOf(authPlatform);
        AuthRequest authRequest = null;
        switch (authPlatformEnum) {
            case WECHAT -> authRequest = new AuthOidcWechatClient(getAuthBaseJson()).getAuthRequest(ignoreCheckState);
            case WECHAT_MINI -> authRequest = new AuthOidcWechatMiniClient(getAuthBaseJson()).getAuthRequest(ignoreCheckState);
        }
        return authRequest;
    }

    @Override
    public String getAuthorizeUrl(String clientType) {
        // 校验认证源配置
        this.valid();
        // 获取客户端
        AuthRequest authRequest = this.getAuthRequest();
        // 获取状态
        String state = AuthStateUtils.createState();
        // 获取缓存操作类
        CommonCacheOperator commonCacheOperator = SpringUtil.getBean(CommonCacheOperator.class);
        // 放入缓存
        JSONObject stateCache = JSONUtil.createObj().set("clientType", clientType);
        commonCacheOperator.put(CONFIG_CACHE_KEY + state, stateCache, 300);
        // 执行认证
        return authRequest.authorize(state);
    }

    @Override
    public AuthResponse<AuthUser> doLogin(AuthCallback authCallback) {
        String code = authCallback.getCode();
        if(ObjectUtil.isEmpty(code)) {
            throw new CommonException("code不能为空");
        }
        String state = authCallback.getState();
        AuthRequest authRequest = this.getAuthRequest(ObjectUtil.isEmpty(state));
        AuthResponse<AuthUser> authResponse = authRequest.login(authCallback);

        // 如果失败了，且是因为 state 校验失败（Illegal state），则尝试降级忽略 state 校验再次登录
        if(!authResponse.ok()) {
            String errorMsg = authResponse.getMsg();
            if(ObjectUtil.isNotEmpty(state) && errorMsg != null && errorMsg.contains("Illegal state")) {
                log.warn(">>> OIDC state校验失败，尝试降级忽略state校验登录，state={}, error={}", state, errorMsg);
                authRequest = this.getAuthRequest(true);
                authResponse = authRequest.login(AuthCallback.builder().code(code).state(state).build());
            }
        }

        if(!authResponse.ok()) {
            throw new CommonException(authResponse.getMsg());
        }
        return handleAuthResponse(authResponse);
    }
}
