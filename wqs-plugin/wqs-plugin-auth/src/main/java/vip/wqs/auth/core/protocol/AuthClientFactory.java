
package vip.wqs.auth.core.protocol;


import cn.hutool.json.JSONObject;
import vip.wqs.auth.core.enums.AuthPlatformEnum;
import vip.wqs.auth.core.protocol.base.AuthBaseClient;
import vip.wqs.auth.core.protocol.oidc.AuthOidcClient;

/**
 * 认证源客户端工厂类
 *
 * @author jetox
 * @date 2024/11/19 15:00
 **/
public class AuthClientFactory {

    /**
     * 创建认证源客户端
     *
     * @author jetox
     * @date 2024/11/19 15:02
     **/
    public static AuthBaseClient<?> createClient(String authPlatform, JSONObject authSource) {
        AuthPlatformEnum authPlatformEnum = AuthPlatformEnum.valueOf(authPlatform);
        return switch (authPlatformEnum) {
            case WECHAT, WECHAT_MINI -> new AuthOidcClient(authSource, authPlatform);
        };
    }
}
