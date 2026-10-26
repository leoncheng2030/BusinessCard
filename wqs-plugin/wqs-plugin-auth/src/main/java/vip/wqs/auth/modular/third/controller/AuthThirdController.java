
package vip.wqs.auth.modular.third.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import me.zhyd.oauth.model.AuthCallback;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import vip.wqs.auth.modular.third.param.AuthThirdBindAccountParam;
import vip.wqs.auth.modular.third.param.AuthThirdCallbackParam;
import vip.wqs.auth.modular.third.param.AuthThirdRenderParam;
import vip.wqs.auth.modular.third.result.AuthThirdRenderResult;
import vip.wqs.auth.modular.third.service.AuthThirdService;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;

/**
 * 第三方登录控制器
 *
 * @author jetox
 * @date 2022/7/8 16:18
 **/
@Tag(name = "三方登录控制器")
@RestController
@Validated
public class AuthThirdController {

    @Resource
    private AuthThirdService authThirdService;

    /**
     * 第三方登录页面渲染
     *
     * @author jetox
     * @date 2022/7/8 16:19
     **/
    @Operation(summary = "第三方登录页面渲染")
    @GetMapping("/auth/third/render")
    public CommonResult<AuthThirdRenderResult> render(@Valid AuthThirdRenderParam authThirdRenderParam) {
        return CommonResult.data(authThirdService.render(authThirdRenderParam));
    }

    /**
     * 第三方登录授权回调
     *
     * @author jetox
     * @date 2022/7/8 16:42
     **/
    @Operation(summary = "第三方登录授权回调")
    @GetMapping("/auth/third/callback")
    public CommonResult<String> callback(@Valid AuthThirdCallbackParam authThirdCallbackParam, AuthCallback authCallback) {
        return CommonResult.data(authThirdService.callback(authThirdCallbackParam, authCallback));
    }

    /**
     * 第三方登录授权回调（POST）
     *
     * @author jetox
     * @date 2022/7/8 16:42
     **/
    @Operation(summary = "第三方登录授权回调（POST）")
    @PostMapping("/auth/third/callback")
    public CommonResult<String> callbackPost(@Valid AuthThirdCallbackParam authThirdCallbackParam, @RequestBody AuthCallback authCallback) {
        return CommonResult.data(authThirdService.callback(authThirdCallbackParam, authCallback));
    }

    /**
     * 第三方登录绑定账号
     *
     * @author jetox
     * @date 2022/7/8 16:42
     **/
    @Operation(summary = "第三方登录绑定账号")
    @PostMapping("/auth/third/bindAccount")
    public CommonResult<String> bindAccount(@RequestBody @Valid AuthThirdBindAccountParam authThirdBindAccountParam) {
        return CommonResult.data(authThirdService.bindAccount(authThirdBindAccountParam));
    }
}
