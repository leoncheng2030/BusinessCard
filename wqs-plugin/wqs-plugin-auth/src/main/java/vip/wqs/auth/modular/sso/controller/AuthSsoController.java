
package vip.wqs.auth.modular.sso.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.modular.sso.param.AuthGetSsoAuthUrlParam;
import vip.wqs.auth.modular.sso.param.AuthSsoTicketLoginParam;
import vip.wqs.auth.modular.sso.service.AuthSsoService;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;

/**
 * 单点登录控制器
 *
 * @author jetox
 * @date 2022/8/30 9:20
 **/
@Tag(name = "单点登录控制器")
@RestController
@Validated
public class AuthSsoController {

    @Resource
    private AuthSsoService authSsoService;

    /**
     * B端获取认证中心地址
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "B端获取认证中心地址")
    @GetMapping("/auth/sso/b/getSsoAuthUrl")
    public CommonResult<String> getSsoAuthUrl(@Valid AuthGetSsoAuthUrlParam authGetSsoAuthUrlParam) {
        return CommonResult.data(authSsoService.getSsoAuthUrl(authGetSsoAuthUrlParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端根据ticket执行单点登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端根据ticket执行单点登录")
    @PostMapping("/auth/sso/b/doLoginByTicket")
    public CommonResult<String> doLoginByTicket(@RequestBody @Valid AuthSsoTicketLoginParam authSsoTicketLoginParam) {
        return CommonResult.data(authSsoService.doLoginByTicket(authSsoTicketLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端单点注销回调
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端单点注销回调")
    @RequestMapping("/auth/sso/b/logoutCall")
    public Object logoutCall() {
        return authSsoService.logoutCall(SaClientTypeEnum.B.getValue());
    }

    /**
     * B端推送客户端地址
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "推送客户端地址")
    @RequestMapping("/auth/sso/b/pushClient")
    public Object pushClient() {
        return authSsoService.pushClient(SaClientTypeEnum.B.getValue());
    }
}
