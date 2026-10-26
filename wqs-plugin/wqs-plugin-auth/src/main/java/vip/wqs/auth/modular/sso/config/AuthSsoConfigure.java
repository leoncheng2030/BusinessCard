
package vip.wqs.auth.modular.sso.config;

import cn.dev33.satoken.sso.config.SaSsoClientConfig;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import vip.wqs.common.prop.CommonProperties;

/**
 * 单点登录客户端配置
 *
 * @author jetox
 * @date 2021/10/9 14:24
 **/
@Configuration
public class AuthSsoConfigure {

    @Resource
    private CommonProperties commonProperties;

    /**
     * 配置SSO客户端相关参数
     */
    @Autowired
    private void configSsoClient(SaSsoClientConfig saSsoClientConfig) {
        saSsoClientConfig.setCurrSsoLogin(commonProperties.getBackendUrl() + "/auth/sso/b/doLoginByTicket");
        saSsoClientConfig.setCurrSsoLogoutCall(commonProperties.getBackendUrl() + "/auth/sso/b/logoutCall");
        saSsoClientConfig.setIsHttp(true);
        saSsoClientConfig.setIsSlo(true);
        saSsoClientConfig.setRegLogoutCall(true);
        saSsoClientConfig.setIsCheckSign(true);
    }
}
