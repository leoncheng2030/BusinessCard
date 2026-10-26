
package vip.wqs.auth.modular.sso.service;

import vip.wqs.auth.modular.sso.param.AuthGetSsoAuthUrlParam;
import vip.wqs.auth.modular.sso.param.AuthSsoTicketLoginParam;

/**
 * 单点登录Service接口
 *
 * @author jetox
 * @date 2022/8/30 9:20
 **/
public interface AuthSsoService {

    /**
     * 获取认证中心地址
     *
     * @author jetox
     * @date 2022/8/30 9:36
     **/
    String getSsoAuthUrl(AuthGetSsoAuthUrlParam authGetSsoAuthUrlParam, String type);

    /**
     * 根据ticket执行单点登录
     *
     * @author jetox
     * @date 2022/8/30 9:36
     **/
    String doLoginByTicket(AuthSsoTicketLoginParam authSsoTicketLoginParam, String type);

    /**
     * 单点注销回调
     *
     * @author jetox
     * @date 2022/8/30 9:36
     **/
    Object logoutCall(String type);

    /**
     * 推送客户端地址
     *
     * @author jetox
     * @date 2022/8/30 9:36
     **/
    Object pushClient(String type);
}
