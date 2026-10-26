
package vip.wqs.auth.core.protocol.base;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.PhoneUtil;
import cn.hutool.json.JSONObject;
import lombok.Getter;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import vip.wqs.auth.core.util.AuthPhoneUtil;
import vip.wqs.common.exception.CommonException;

/**
 * 认证源客户端抽象类
 *
 * @author jetox
 * @date 2024/11/19 15:01
 **/
@Getter
public abstract class AuthBaseClient<T extends AuthBaseJson> {

    // 前端页面地址
    public static final String FRONT_CALLBACK_URL = "/iam/auth/callback";

    /** 缓存前缀 */
    public static final String CONFIG_CACHE_KEY = "auth-source-state:";

    private final JSONObject authSource;

    private final String authPlatform;

    public AuthBaseClient(JSONObject authSource, String authPlatform) {
        this.authSource = authSource;
        this.authPlatform = authPlatform;
    }

    /**
     * 获取基础配置
     *
     * @author jetox
     * @date 2024/11/19 15:17
     **/
    public abstract T getAuthBaseJson();

    /**
     * 验证认证源
     *
     * @author jetox
     * @date 2024/12/30 10:52
     **/
    public abstract void valid();

    /**
     * 获取认证url
     *
     * @author jetox
     * @date 2025/2/6 16:28
     **/
    public abstract String getAuthorizeUrl(String clientType);

    /**
     * 执行登录
     *
     * @author jetox
     * @date 2025/2/11 14:07
     **/
    public abstract AuthResponse<AuthUser> doLogin(AuthCallback authCallback);

    /**
     * 处理响应结果
     *
     * @author jetox
     * @date 2025/2/11 16:39
     **/
    public AuthResponse<AuthUser> handleAuthResponse(AuthResponse<AuthUser> authResponse) {

        AuthBaseJson authBaseJson = this.getAuthBaseJson();
        if(authResponse.ok()) {
            String uuid;

            AuthUser authUser = authResponse.getData();
            authUser.setSource(this.authPlatform);
            com.alibaba.fastjson.JSONObject rawUserInfo = authUser.getRawUserInfo();
            if(ObjectUtil.isNotEmpty(rawUserInfo)) {
                Object rawUserInfoData = rawUserInfo.get("data");
                if(ObjectUtil.isNotEmpty(rawUserInfoData)) {
                    rawUserInfo = com.alibaba.fastjson.JSONObject.parseObject(rawUserInfoData.toString());
                }
                String sourceProperty = authBaseJson.getSourceProperty();
                if(ObjectUtil.isNotEmpty(sourceProperty)) {
                    Object sourcePropertyValue = rawUserInfo.get(sourceProperty);
                    if(ObjectUtil.isNotEmpty(sourcePropertyValue)) {
                        uuid = Convert.toStr(sourcePropertyValue);
                        if(PhoneUtil.isMobile(uuid)) {
                            uuid = AuthPhoneUtil.trimTelNum(uuid);
                        }
                        authUser.setUuid(uuid);
                    }
                }
            }
            return authResponse;
        } else {
            throw new CommonException(authResponse.getMsg());
        }
    }
}
