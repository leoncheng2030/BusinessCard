
package vip.wqs.auth.modular.monitor.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.auth.modular.monitor.param.AuthExitSessionParam;
import vip.wqs.auth.modular.monitor.param.AuthExitTokenParam;
import vip.wqs.auth.modular.monitor.param.AuthSessionPageParam;
import vip.wqs.auth.modular.monitor.result.AuthSessionAnalysisResult;
import vip.wqs.auth.modular.monitor.result.AuthSessionPageResult;
import vip.wqs.auth.modular.monitor.service.AuthSessionService;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;
import java.util.List;

/**
 * 会话治理控制器
 *
 * @author jetox
 * @date 2022/6/24 15:20
 **/
@Tag(name = "会话治理控制器")
@RestController
@Validated
public class AuthSessionController {

    @Resource
    private AuthSessionService authSessionService;

    /**
     * 会话统计
     *
     * @author jetox
     * @date 2022/6/24 22:28
     */
    @Operation(summary = "会话统计")
    @GetMapping("/auth/session/analysis")
    public CommonResult<AuthSessionAnalysisResult> analysis() {
        return CommonResult.data(authSessionService.analysis());
    }

    /**
     * 查询B端会话
     *
     * @author jetox
     * @date 2022/6/24 22:28
     */
    @Operation(summary = "查询B端会话")
    @GetMapping("/auth/session/b/page")
    public CommonResult<Page<AuthSessionPageResult>> pageForB(AuthSessionPageParam authSessionPageParam) {
        return CommonResult.data(authSessionService.pageForB(authSessionPageParam));
    }

    /**
     * 查询C端会话
     *
     * @author jetox
     * @date 2022/6/24 22:28
     */
    @Operation(summary = "查询C端会话")
    @GetMapping("/auth/session/c/page")
    public CommonResult<Page<AuthSessionPageResult>> pageForC(AuthSessionPageParam authSessionPageParam) {
        return CommonResult.data(authSessionService.pageForC(authSessionPageParam));
    }

    /**
     * 强退B端会话
     *
     * @author jetox
     * @date 2021/10/12 10:25
     **/
    @Operation(summary = "强退B端会话")
    @CommonLog("强退B端会话")
    @SaCheckPermission("/auth/session/b/exit")
    @PostMapping("/auth/session/b/exit")
    public CommonResult<String> exitSessionForB(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                            List<AuthExitSessionParam> authExitSessionParamList) {
        authSessionService.exitSessionForB(authExitSessionParamList);
        return CommonResult.ok();
    }

    /**
     * 强退C端会话
     *
     * @author jetox
     * @date 2021/10/12 10:25
     **/
    @Operation(summary = "强退C端会话")
    @CommonLog("强退C端会话")
    @SaCheckPermission("/auth/session/c/exit")
    @PostMapping("/auth/session/c/exit")
    public CommonResult<String> exitSessionForC(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                List<AuthExitSessionParam> authExitSessionParamList) {
        authSessionService.exitSessionForC(authExitSessionParamList);
        return CommonResult.ok();
    }

    /**
     * 强退B端token
     *
     * @author jetox
     * @date 2021/10/12 10:25
     **/
    @Operation(summary = "强退B端token")
    @CommonLog("强退B端token")
    @SaCheckPermission("/auth/token/b/exit")
    @PostMapping("/auth/token/b/exit")
    public CommonResult<String> exitTokenForB(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                            List<AuthExitTokenParam> authExitTokenParamList) {
        authSessionService.exitTokenForB(authExitTokenParamList);
        return CommonResult.ok();
    }

    /**
     * 强退C端token
     *
     * @author jetox
     * @date 2021/10/12 10:25
     **/
    @Operation(summary = "强退C端token")
    @CommonLog("强退C端token")
    @SaCheckPermission("/auth/token/c/exit")
    @PostMapping("/auth/token/c/exit")
    public CommonResult<String> exitTokenForC(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                            List<AuthExitTokenParam> authExitTokenParamList) {
        authSessionService.exitTokenForC(authExitTokenParamList);
        return CommonResult.ok();
    }
}
