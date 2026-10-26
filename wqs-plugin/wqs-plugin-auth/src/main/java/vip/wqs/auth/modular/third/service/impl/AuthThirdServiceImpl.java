
package vip.wqs.auth.modular.third.service.impl;

import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.oauth2.consts.SaOAuth2Consts;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.hutool.HutoolImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.model.AuthCallback;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.auth.api.SaBaseLoginUserApi;
import vip.wqs.auth.core.enums.AuthPlatformEnum;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.core.protocol.AuthClientFactory;
import vip.wqs.auth.core.protocol.base.AuthBaseClient;
import vip.wqs.auth.modular.login.enums.AuthDeviceTypeEnum;
import vip.wqs.auth.modular.login.param.AuthAccountPasswordLoginParam;
import vip.wqs.auth.modular.login.service.AuthService;
import vip.wqs.auth.modular.third.entity.AuthThirdUser;
import vip.wqs.auth.modular.third.mapper.AuthThirdMapper;
import vip.wqs.auth.modular.third.param.AuthThirdBindAccountParam;
import vip.wqs.auth.modular.third.param.AuthThirdCallbackParam;
import vip.wqs.auth.modular.third.param.AuthThirdRenderParam;
import vip.wqs.auth.modular.third.result.AuthThirdRenderResult;
import vip.wqs.auth.modular.third.service.AuthThirdService;
import vip.wqs.common.cache.CommonCacheOperator;
import vip.wqs.common.exception.CommonException;
import vip.wqs.dev.api.DevConfigApi;

import java.util.List;

/**
 * 第三方登录Service接口实现类
 *
 * @author jetox
 * @date 2022/7/8 16:20
 **/
@Slf4j
@Service
public class AuthThirdServiceImpl extends ServiceImpl<AuthThirdMapper, AuthThirdUser> implements AuthThirdService {

    // 微信
    private static final String SNOWY_THIRD_WECHAT_ALLOW_LOGIN_FLAG_KEY = "SNOWY_THIRD_WECHAT_ALLOW_LOGIN_FLAG";
    private static final String SNOWY_THIRD_WECHAT_CLIENT_ID_KEY = "SNOWY_THIRD_WECHAT_CLIENT_ID";
    private static final String SNOWY_THIRD_WECHAT_CLIENT_SECRET_KEY = "SNOWY_THIRD_WECHAT_CLIENT_SECRET";
    private static final String SNOWY_THIRD_WECHAT_REDIRECT_URL_KEY = "SNOWY_THIRD_WECHAT_REDIRECT_URL";

    // 微信小程序
    private static final String SNOWY_THIRD_WECHAT_MINI_ALLOW_LOGIN_FLAG_KEY = "SNOWY_THIRD_WECHAT_MINI_ALLOW_LOGIN_FLAG";
    private static final String SNOWY_THIRD_WECHAT_MINI_CLIENT_ID_KEY = "SNOWY_THIRD_WECHAT_MINI_CLIENT_ID";
    private static final String SNOWY_THIRD_WECHAT_MINI_CLIENT_SECRET_KEY = "SNOWY_THIRD_WECHAT_MINI_CLIENT_SECRET";

    @Resource
    private CommonCacheOperator commonCacheOperator;

    @Resource
    private DevConfigApi devConfigApi;

    @Resource
    private AuthService authService;

    @Resource(name = "loginUserApi")
    private SaBaseLoginUserApi loginUserApi;

    @Resource(name = "clientLoginUserApi")
    private SaBaseLoginUserApi clientLoginUserApi;

    @Override
    public AuthThirdRenderResult render(AuthThirdRenderParam authThirdRenderParam) {
        // 校验认证源是否存在
        String platform = authThirdRenderParam.getPlatform();
        AuthPlatformEnum.validate(platform);
        // 校验登录端类型
        String clientType = authThirdRenderParam.getClientType();
        SaClientTypeEnum.validate(clientType);
        try {
            // 创建客户端
            AuthBaseClient<?> authSourceBaseClient = this.getAuthClient(platform);
            // 获取认证地址，需客户端类型
            String authorizeUrl = authSourceBaseClient.getAuthorizeUrl(clientType);
            // 构造结果
            AuthThirdRenderResult authThirdRenderResult = new AuthThirdRenderResult();
            // 设置授权地址
            authThirdRenderResult.setAuthorizeUrl(authorizeUrl);
            // 返回结果
            return authThirdRenderResult;
        } catch (Exception e) {
            throw new CommonException(e.getMessage());
        }
    }

    @SuppressWarnings("ALL")
    @Transactional(rollbackFor = Exception.class)
    @Override
    public String callback(AuthThirdCallbackParam authThirdCallbackParam, AuthCallback authCallback) {
        // 校验认证源是否存在
        String platform = authThirdCallbackParam.getPlatform();
        AuthPlatformEnum.validate(platform);
        // 创建客户端
        AuthBaseClient<?> authSourceBaseClient = this.getAuthClient(platform);
        // 获取state
        String state = authCallback.getState();
        if(ObjectUtil.isEmpty(state)) {
            state = SaHolder.getRequest().getParam(SaOAuth2Consts.Param.state);
        }
        if(ObjectUtil.isEmpty(state)) {
            state = SaHolder.getRequest().getParam("RelayState");
        }
        // 定义登录端类型
        String clientType = authThirdCallbackParam.getClientType();
        if(ObjectUtil.isEmpty(clientType)) {
            // 默认登录端类型，如果是微信小程序，则默认C端登录，否则默认B端登录
            clientType = platform.equalsIgnoreCase(AuthPlatformEnum.WECHAT_MINI.getValue())? SaClientTypeEnum.C.getValue():SaClientTypeEnum.B.getValue();
        } else {
            SaClientTypeEnum.validate(clientType);
        }
        // state缓存的配置（clientType），随下方state读取逻辑填充
        JSONObject stateCacheValueJsonObject = null;
        // 如果state不为空，尝试从缓存中获取配置并清理
        if(ObjectUtil.isNotEmpty(state)) {
            // 获取缓存操作类
            CommonCacheOperator commonCacheOperator = SpringUtil.getBean(CommonCacheOperator.class);
            // 获取缓存值（render发起登录时按state缓存clientType）
            Object stateCacheValueObj = null;
            try {
                stateCacheValueObj = commonCacheOperator.get(AuthBaseClient.CONFIG_CACHE_KEY + state);
            } catch (Exception e) {
                log.warn(">>> SSO state Redis查询异常，跳过校验 state={}, error={}", state, e.getMessage());
            }
            // 判断是否为空
            if(ObjectUtil.isNotEmpty(stateCacheValueObj)){
                // 转换为json对象
                stateCacheValueJsonObject = JSONUtil.parseObj(stateCacheValueObj);
                // 如果接口参数未传clientType，则从缓存中获取
                if(ObjectUtil.isEmpty(authThirdCallbackParam.getClientType())) {
                    clientType = stateCacheValueJsonObject.getStr("clientType");
                }
                // 移除缓存（无论是否使用缓存中的 clientType，只要 state 匹配就清理）
                commonCacheOperator.remove(AuthBaseClient.CONFIG_CACHE_KEY + state);
            } else {
                log.warn(">>> SSO state 校验失败（可能未使用render发起登录），跳过校验 state={}", state);
            }
        }
        // 执行请求
        AuthResponse<AuthUser> authResponse = authSourceBaseClient.doLogin(authCallback);
        if (authResponse.ok()) {
            // 授权的用户信息
            AuthUser authUser = authResponse.getData();
            // 获取第三方用户id
            String uuid = authUser.getUuid();
            // 根据第三方用户id和平台标识获取用户信息
            AuthThirdUser authThirdUser = this.getOne(new LambdaQueryWrapper<AuthThirdUser>().eq(AuthThirdUser::getThirdId, uuid)
                    .eq(AuthThirdUser::getCategory, platform));
            // 定义系统用户id
            String userId;
            if(ObjectUtil.isEmpty(authThirdUser)) {
                // 如果三方用户不存在，自动创建本地用户并绑定
                userId = this.createAndBindUser(authUser, platform, clientType);
            } else {
                // 否则直接获取用户id，判断是否存在（有可能没绑定）
                userId = authThirdUser.getUserId();
                if(ObjectUtil.isEmpty(userId)) {
                    // 三方用户存在但未绑定本地用户，自动创建并绑定
                    userId = this.createAndBindUserForExistThird(authUser, authThirdUser, platform, clientType);
                }
            }
            // 登录设备
            String device = platform.equalsIgnoreCase(AuthPlatformEnum.WECHAT_MINI.getValue())?
                    AuthDeviceTypeEnum.MINI.getValue():AuthDeviceTypeEnum.PC.getValue();
            // 已绑定用户，直接用userId登录
            return authService.doLoginById(userId, device, clientType);
        } else {
            throw new CommonException("第三方登录授权回调失败，原因：{}", authResponse.getMsg());
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public String bindAccount(AuthThirdBindAccountParam authThirdBindAccountParam) {
        AuthThirdUser authThirdUser = this.getById(authThirdBindAccountParam.getThirdId());
        if(ObjectUtil.isEmpty(authThirdUser)) {
            throw new CommonException("三方用户不存在");
        }
        if(ObjectUtil.isNotEmpty(authThirdUser.getUserId())) {
            throw new CommonException("三方用户已绑定，不可重复绑定");
        }
        AuthAccountPasswordLoginParam authAccountPasswordLoginParam = new AuthAccountPasswordLoginParam();
        authAccountPasswordLoginParam.setAccount(authThirdBindAccountParam.getAccount());
        authAccountPasswordLoginParam.setPassword(authThirdBindAccountParam.getPassword());
        authAccountPasswordLoginParam.setValidCode(authThirdBindAccountParam.getValidCode());
        authAccountPasswordLoginParam.setValidCodeReqNo(authThirdBindAccountParam.getValidCodeReqNo());
        String token = authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.B.getValue());
        String userId = StpUtil.getLoginIdAsString();
        authThirdUser.setUserId(userId);
        this.updateById(authThirdUser);
        return token;
    }

    @Override
    public void deleteByUserIdList(List<String> userIdList) {
        if(ObjectUtil.isNotEmpty(userIdList)) {
            this.remove(new LambdaQueryWrapper<AuthThirdUser>().in(AuthThirdUser::getUserId, userIdList));
        }
    }

    /**
     * 创建授权请求客户端
     *
     * @author jetox
     * @date 2022/7/8 16:48
     **/
    private AuthBaseClient<?> getAuthClient(String platform) {
        AuthBaseClient<?> authClient = null;
        platform = platform.toUpperCase();
        HttpUtil.setHttp(new HutoolImpl());
        AuthPlatformEnum.validate(platform);

        if(platform.equals(AuthPlatformEnum.WECHAT.getValue())){
            // 检查是否允许登录
            if(!Boolean.parseBoolean(devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_ALLOW_LOGIN_FLAG_KEY))) {
                throw new CommonException("微信登录已禁用");
            }
            if(!devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_REDIRECT_URL_KEY).startsWith("http")) {
                throw new CommonException("重定向地址配置错误");
            }
            // 微信登录
            authClient = AuthClientFactory.createClient(platform, JSONUtil.createObj()
                    .set("clientId", devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_CLIENT_ID_KEY))
                    .set("clientSecret", devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_CLIENT_SECRET_KEY))
                    .set("callbackUrl", devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_REDIRECT_URL_KEY)));
        }
        if(platform.equals(AuthPlatformEnum.WECHAT_MINI.getValue())){
            // 检查是否允许登录
            if(!Boolean.parseBoolean(devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_MINI_ALLOW_LOGIN_FLAG_KEY))) {
                throw new CommonException("微信小程序登录已禁用");
            }
            // 微信小程序登录
            authClient = AuthClientFactory.createClient(platform, JSONUtil.createObj()
                    .set("clientId", devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_MINI_CLIENT_ID_KEY))
                    .set("clientSecret", devConfigApi.getValueByKey(SNOWY_THIRD_WECHAT_MINI_CLIENT_SECRET_KEY)));
        }

        return authClient;
    }

    /**
     * 创建本地用户并绑定三方用户（三方用户记录不存在的情况）
     *
     * @param authUser 第三方用户信息
     * @param platform 平台标识
     * @param clientType 登录端类型
     * @return 创建的本地用户ID
     *
     * @author yubaoshan
     * @date 2025/06/26
     */
    private String createAndBindUser(AuthUser authUser, String platform, String clientType) {
        // 自动创建本地用户
        String userId = this.autoCreateUser(authUser, platform, clientType);

        // 插入三方用户记录并绑定
        AuthThirdUser authThirdUser = new AuthThirdUser();
        authThirdUser.setThirdId(authUser.getUuid());
        authThirdUser.setUserId(userId);
        authThirdUser.setAvatar(authUser.getAvatar());
        authThirdUser.setName(authUser.getUsername());
        authThirdUser.setNickname(authUser.getNickname());
        authThirdUser.setGender(authUser.getGender() != null ? authUser.getGender().getDesc() : "未知");
        authThirdUser.setCategory(platform);
        authThirdUser.setExtJson(JSONUtil.toJsonStr(authUser.getRawUserInfo()));
        this.save(authThirdUser);

        log.info(">>> SSO登录自动创建用户成功，userId={}, thirdId={}, source={}", userId, authUser.getUuid(), platform);

        return userId;
    }

    /**
     * 为已存在的三方用户创建本地用户并绑定（三方用户记录存在但未绑定的情况）
     *
     * @param authUser 第三方用户信息
     * @param authThirdUser 已存在的三方用户记录
     * @param platform 平台标识
     * @param clientType 登录端类型
     * @return 创建的本地用户ID
     *
     * @author yubaoshan
     * @date 2025/06/26
     */
    private String createAndBindUserForExistThird(AuthUser authUser, AuthThirdUser authThirdUser, String platform, String clientType) {
        // 自动创建本地用户
        String userId = this.autoCreateUser(authUser, platform, clientType);

        // 更新三方用户记录，绑定userId
        authThirdUser.setUserId(userId);
        this.updateById(authThirdUser);

        log.info(">>> SSO登录为已存在三方用户创建本地用户并绑定成功，userId={}, thirdId={}, source={}", userId, authUser.getUuid(), platform);

        return userId;
    }

    /**
     * 自动创建本地用户
     *
     * @author yubaoshan
     * @date 2024/07/05
     */
    private String autoCreateUser(AuthUser authUser, String platform, String clientType) {
        String email = authUser.getEmail();
        String phone = platform.equalsIgnoreCase("phone") ? authUser.getUsername() : null;
        String userId;

        // 生成账号：来源 + "_" + 完整UUID，保证全局唯一性，避免截取导致的冲突
        String account = platform.toLowerCase() + "_" + authUser.getUuid();

        // 生成美观的默认名称（昵称）
        String name = authUser.getNickname();
        if (StrUtil.isBlank(name)) {
            name = authUser.getUsername();
        }
        if (StrUtil.isBlank(name)) {
            name = getFriendlyPlatformName(platform) + "用户_" + RandomUtil.randomString(10);
        }

        if (SaClientTypeEnum.B.getValue().equals(clientType)) {
            if (StrUtil.isNotBlank(phone)) {
                userId = loginUserApi.createUserWithPhone(phone).getId();
            } else if (StrUtil.isNotBlank(email)) {
                userId = loginUserApi.createUserWithEmail(email).getId();
            } else {
                String randomPassword = RandomUtil.randomString(16);
                userId = loginUserApi.createUserWithAccount(account, randomPassword, name).getId();
            }
        } else {
            if (StrUtil.isNotBlank(phone)) {
                userId = clientLoginUserApi.createClientUserWithPhone(phone).getId();
            } else if (StrUtil.isNotBlank(email)) {
                userId = clientLoginUserApi.createClientUserWithEmail(email).getId();
            } else {
                String randomPassword = RandomUtil.randomString(16);
                userId = clientLoginUserApi.createClientUserWithAccount(account, randomPassword, name).getId();
            }
        }
        return userId;
    }

    /**
     * 获取友好的平台名称
     *
     * @author yubaoshan
     * @date 2024/07/05
     */
    private String getFriendlyPlatformName(String source) {
        if (StrUtil.isBlank(source)) {
            return "第三方";
        }
        for (AuthPlatformEnum platformEnum : AuthPlatformEnum.values()) {
            if (platformEnum.getValue().equalsIgnoreCase(source)) {
                return platformEnum.getDescription();
            }
        }
        return source;
    }
}
