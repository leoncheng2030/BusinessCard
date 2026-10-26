
package vip.wqs.auth.modular.sso.service.impl;

import cn.dev33.satoken.sso.model.SaCheckTicketResult;
import cn.dev33.satoken.sso.processor.SaSsoClientProcessor;
import cn.dev33.satoken.sso.template.SaSsoClientUtil;
import cn.dev33.satoken.util.SaResult;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.modular.login.enums.AuthDeviceTypeEnum;
import vip.wqs.auth.modular.login.enums.AuthStrategyWhenNoUserWithPhoneOrEmailEnum;
import vip.wqs.auth.modular.login.service.AuthService;
import vip.wqs.auth.modular.sso.param.AuthGetSsoAuthUrlParam;
import vip.wqs.auth.modular.sso.param.AuthSsoTicketLoginParam;
import vip.wqs.auth.modular.sso.service.AuthSsoService;
import vip.wqs.common.exception.CommonException;

/**
 * 单点登录Service接口实现类
 *
 * @author jetox
 * @date 2022/8/30 9:21
 **/
@Service
public class AuthSsoServiceImpl implements AuthSsoService {

    @Resource
    private AuthService authService;

    @Override
    public String getSsoAuthUrl(AuthGetSsoAuthUrlParam authGetSsoAuthUrlParam, String type) {
        if(SaClientTypeEnum.B.getValue().equals(type)) {
            return SaSsoClientUtil.buildServerAuthUrl(authGetSsoAuthUrlParam.getRedirectUrl(), "");
        } else {
            throw new CommonException("不支持的客户端类型：{}", type);
        }
    }

    @Override
    public String doLoginByTicket(AuthSsoTicketLoginParam authSsoTicketLoginParam, String type) {
        if(SaClientTypeEnum.B.getValue().equals(type)) {
            SaCheckTicketResult saCheckTicketResult = SaSsoClientProcessor.instance.checkTicket(authSsoTicketLoginParam.getTicket());
            // 获取用户信息
            SaResult result = saCheckTicketResult.result;
            Object account = result.get("account");
            Object phone = result.get("phone");
            Object email = result.get("email");
            if(ObjectUtil.isNotEmpty(account)) {
                return authService.doLoginByAccount(Convert.toStr(account),
                        ObjectUtil.isEmpty(authSsoTicketLoginParam.getDevice()) ? AuthDeviceTypeEnum.PC.getValue() : authSsoTicketLoginParam.getDevice(),
                        SaClientTypeEnum.B.getValue());
            } else if(ObjectUtil.isNotEmpty(phone)) {
                return authService.doLoginByPhone(Convert.toStr(phone),
                        ObjectUtil.isEmpty(authSsoTicketLoginParam.getDevice()) ? AuthDeviceTypeEnum.PC.getValue() : authSsoTicketLoginParam.getDevice(),
                        SaClientTypeEnum.B.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.NOT_ALLOW_LOGIN.getValue());
            } else if(ObjectUtil.isNotEmpty(email)) {
                return authService.doLoginByEmail(Convert.toStr(email),
                        ObjectUtil.isEmpty(authSsoTicketLoginParam.getDevice()) ? AuthDeviceTypeEnum.PC.getValue() : authSsoTicketLoginParam.getDevice(),
                        SaClientTypeEnum.B.getValue(), AuthStrategyWhenNoUserWithPhoneOrEmailEnum.NOT_ALLOW_LOGIN.getValue());
            } else {
                throw new CommonException("登录失败，根据账号、手机号、邮箱未匹配到用户");
            }
        } else {
            throw new CommonException("不支持的客户端类型：{}", type);
        }
    }

    @Override
    public Object logoutCall(String type) {
        if(SaClientTypeEnum.B.getValue().equals(type)) {
            return SaSsoClientProcessor.instance.ssoLogoutCall();
        } else {
            throw new CommonException("不支持的客户端类型：{}", type);
        }
    }

    @Override
    public Object pushClient(String type) {
        if(SaClientTypeEnum.B.getValue().equals(type)) {
            return SaSsoClientProcessor.instance.ssoPushC();
        } else {
            throw new CommonException("不支持的客户端类型：{}", type);
        }
    }
}
