
package vip.wqs.auth.modular.login.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.auth.core.annotation.SaClientCheckLogin;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.auth.modular.login.param.*;
import vip.wqs.auth.modular.login.result.AuthPicValidCodeResult;
import vip.wqs.auth.modular.login.service.AuthService;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;

/**
 * C端登录控制器
 *
 * @author jetox
 * @date 2021/12/23 21:50
 */
@Tag(name = "C端登录控制器")
@RestController
@Validated
public class AuthClientController {

    @Resource
    private AuthService authService;

    /**
     * C端获取图片验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "C端获取图片验证码")
    @GetMapping("/auth/c/getPicCaptcha")
    public CommonResult<AuthPicValidCodeResult> getPicCaptcha() {
        return CommonResult.data(authService.getPicCaptcha(SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端获取手机登录验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "C端获取手机登录验证码")
    @GetMapping("/auth/c/getPhoneValidCode")
    public CommonResult<String> getPhoneValidCode(@Valid AuthGetPhoneValidCodeParam authGetPhoneValidCodeParam) {
        return CommonResult.data(authService.getPhoneValidCode(authGetPhoneValidCodeParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端获取邮箱登录验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "C端获取邮箱登录验证码")
    @GetMapping("/auth/c/getEmailValidCode")
    public CommonResult<String> getEmailValidCode(@Valid AuthGetEmailValidCodeParam authGetEmailValidCodeParam) {
        return CommonResult.data(authService.getEmailValidCode(authGetEmailValidCodeParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端账号密码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端账号密码登录")
    @PostMapping("/auth/c/doLogin")
    public CommonResult<String> doLogin(@RequestBody @Valid AuthAccountPasswordLoginParam authAccountPasswordLoginParam) {
        return CommonResult.data(authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端手机验证码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端手机验证码登录")
    @PostMapping("/auth/c/doLoginByPhone")
    public CommonResult<String> doLoginByPhone(@RequestBody @Valid AuthPhoneValidCodeLoginParam authPhoneValidCodeLoginParam) {
        return CommonResult.data(authService.doLoginByPhone(authPhoneValidCodeLoginParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端邮箱验证码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端邮箱验证码登录")
    @PostMapping("/auth/c/doLoginByEmail")
    public CommonResult<String> doLoginByEmail(@RequestBody @Valid AuthEmailValidCodeLoginParam authEmailValidCodeLoginParam) {
        return CommonResult.data(authService.doLoginByEmail(authEmailValidCodeLoginParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端退出
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端退出")
    @SaClientCheckLogin
    @GetMapping("/auth/c/doLogout")
    public CommonResult<String> doLogout() {
        StpClientUtil.logout();
        return CommonResult.ok();
    }

    /**
     * C端获取用户信息
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端获取用户信息")
    @SaClientCheckLogin
    @GetMapping("/auth/c/getLoginUser")
    public CommonResult<SaBaseClientLoginUser> getLoginUser() {
        return CommonResult.data(authService.getClientLoginUser());
    }

    /**
     * C端注册
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端注册")
    @PostMapping("/auth/c/register")
    public CommonResult<String> register(@RequestBody @Valid AuthRegisterParam authRegisterParam) {
        authService.register(authRegisterParam, SaClientTypeEnum.C.getValue());
        return CommonResult.ok();
    }

    /**
     * C端动态口令登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端动态口令登录")
    @PostMapping("/auth/c/doLoginByOtp")
    public CommonResult<String> doLoginByOtp(@RequestBody @Valid AuthOtpLoginParam authOtpLoginParam) {
        return CommonResult.data(authService.doLoginByOtp(authOtpLoginParam, SaClientTypeEnum.C.getValue()));
    }

    /**
     * C端判断是否登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "C端判断是否登录")
    @GetMapping("/auth/c/isLogin")
    public CommonResult<Boolean> isLogin() {
        return CommonResult.data(StpClientUtil.isLogin());
    }
}
