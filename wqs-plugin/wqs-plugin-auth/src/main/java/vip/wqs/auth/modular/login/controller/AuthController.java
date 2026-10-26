
package vip.wqs.auth.modular.login.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import cn.dev33.satoken.stp.StpUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.auth.core.enums.SaClientTypeEnum;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.auth.modular.login.param.*;
import vip.wqs.auth.modular.login.result.AuthPicValidCodeResult;
import vip.wqs.auth.modular.login.service.AuthService;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;

/**
 * B端登录控制器
 *
 * @author jetox
 * @date 2021/12/23 21:50
 */
@Tag(name = "B端登录控制器")
@RestController
@Validated
public class AuthController {

    @Resource
    private AuthService authService;

    /**
     * B端获取图片验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "B端获取图片验证码")
    @GetMapping("/auth/b/getPicCaptcha")
    public CommonResult<AuthPicValidCodeResult> getPicCaptcha() {
        return CommonResult.data(authService.getPicCaptcha(SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端获取手机登录验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "B端获取手机登录验证码")
    @GetMapping("/auth/b/getPhoneValidCode")
    public CommonResult<String> getPhoneValidCode(@Valid AuthGetPhoneValidCodeParam authGetPhoneValidCodeParam) {
        return CommonResult.data(authService.getPhoneValidCode(authGetPhoneValidCodeParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端获取邮箱登录验证码
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    @Operation(summary = "B端获取邮箱登录验证码")
    @GetMapping("/auth/b/getEmailValidCode")
    public CommonResult<String> getEmailValidCode(@Valid AuthGetEmailValidCodeParam authGetEmailValidCodeParam) {
        return CommonResult.data(authService.getEmailValidCode(authGetEmailValidCodeParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端账号密码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端账号密码登录")
    @PostMapping("/auth/b/doLogin")
    public CommonResult<String> doLogin(@RequestBody @Valid AuthAccountPasswordLoginParam authAccountPasswordLoginParam) {
        return CommonResult.data(authService.doLogin(authAccountPasswordLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端手机验证码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端手机验证码登录")
    @PostMapping("/auth/b/doLoginByPhone")
    public CommonResult<String> doLoginByPhone(@RequestBody @Valid AuthPhoneValidCodeLoginParam authPhoneValidCodeLoginParam) {
        return CommonResult.data(authService.doLoginByPhone(authPhoneValidCodeLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端邮箱验证码登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端邮箱验证码登录")
    @PostMapping("/auth/b/doLoginByEmail")
    public CommonResult<String> doLoginByEmail(@RequestBody @Valid AuthEmailValidCodeLoginParam authEmailValidCodeLoginParam) {
        return CommonResult.data(authService.doLoginByEmail(authEmailValidCodeLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端退出
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端退出")
    @SaCheckLogin
    @GetMapping("/auth/b/doLogout")
    public CommonResult<String> doLogout() {
        StpUtil.logout();
        return CommonResult.ok();
    }

    /**
     * B端获取用户信息
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端获取用户信息")
    @SaCheckLogin
    @GetMapping("/auth/b/getLoginUser")
    public CommonResult<SaBaseLoginUser> getLoginUser() {
        return CommonResult.data(authService.getLoginUser());
    }

    /**
     * B端注册
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端注册")
    @PostMapping("/auth/b/register")
    public CommonResult<String> register(@RequestBody @Valid AuthRegisterParam authRegisterParam) {
        authService.register(authRegisterParam, SaClientTypeEnum.B.getValue());
        return CommonResult.ok();
    }

    /**
     * B端动态口令登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端动态口令登录")
    @PostMapping("/auth/b/doLoginByOtp")
    public CommonResult<String> doLoginByOtp(@RequestBody @Valid AuthOtpLoginParam authOtpLoginParam) {
        return CommonResult.data(authService.doLoginByOtp(authOtpLoginParam, SaClientTypeEnum.B.getValue()));
    }

    /**
     * B端第三方Token交换登录（用于iframe嵌入免登）
     *
     * @author yubaoshan
     * @date 2026/2/11
     **/
    @Operation(summary = "B端第三方Token交换登录（iframe嵌入免登）")
    @PostMapping("/auth/b/doLoginByThirdToken")
    public CommonResult<String> doLoginByThirdToken(@RequestBody @Valid AuthThirdTokenLoginParam authThirdTokenLoginParam) {
        return CommonResult.data(authService.doLoginByThirdToken(authThirdTokenLoginParam));
    }

    /**
     * B端判断是否登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    @Operation(summary = "B端判断是否登录")
    @GetMapping("/auth/b/isLogin")
    public CommonResult<Boolean> isLogin() {
        return CommonResult.data(StpUtil.isLogin());
    }
}
