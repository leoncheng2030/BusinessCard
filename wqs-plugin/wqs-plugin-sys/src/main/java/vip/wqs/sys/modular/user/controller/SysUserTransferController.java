
package vip.wqs.sys.modular.user.controller;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.sys.modular.org.param.SysOrgSelectorTreeParam;
import vip.wqs.sys.modular.org.service.SysOrgService;
import vip.wqs.sys.modular.user.entity.SysUser;
import vip.wqs.sys.modular.user.param.SysUserSelectorUserParam;
import vip.wqs.sys.modular.user.param.SysUserTransferExecuteParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceDetailParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceListParam;
import vip.wqs.sys.modular.user.result.SysUserTransferResourceResult;
import vip.wqs.sys.modular.user.service.SysUserService;
import vip.wqs.sys.modular.user.service.SysUserTransferService;

import java.util.List;

/**
 * 权限转移控制器
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Tag(name = "权限转移控制器")
@RestController
@Validated
public class SysUserTransferController {

    @Resource
    private SysUserTransferService sysUserTransferService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysUserService sysUserService;

    @Operation(summary = "获取用户可转移资源列表")
    @GetMapping("/sys/user/transfer/resourceList")
    public CommonResult<List<SysUserTransferResourceResult>> resourceList(@Valid SysUserTransferResourceListParam sysUserTransferResourceListParam) {
        return CommonResult.data(sysUserTransferService.resourceList(sysUserTransferResourceListParam));
    }

    @Operation(summary = "获取资源明细列表")
    @GetMapping("/sys/user/transfer/resourceDetail")
    public CommonResult<List<JSONObject>> resourceDetail(@Valid SysUserTransferResourceDetailParam sysUserTransferResourceDetailParam) {
        return CommonResult.data(sysUserTransferService.resourceDetail(sysUserTransferResourceDetailParam));
    }

    @Operation(summary = "执行权限转移")
    @CommonLog("执行权限转移")
    @PostMapping("/sys/user/transfer/execute")
    public CommonResult<String> execute(@RequestBody @Valid SysUserTransferExecuteParam sysUserTransferExecuteParam) {
        sysUserTransferService.execute(sysUserTransferExecuteParam);
        return CommonResult.ok();
    }

    @Operation(summary = "获取机构树选择器")
    @GetMapping("/sys/user/transfer/orgTreeSelector")
    public CommonResult<List<JSONObject>> orgTreeSelector(SysOrgSelectorTreeParam sysOrgSelectorTreeParam) {
        return CommonResult.data(sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam));
    }

    @Operation(summary = "获取用户选择器")
    @GetMapping("/sys/user/transfer/userSelector")
    public CommonResult<Page<SysUser>> userSelector(SysUserSelectorUserParam sysUserSelectorUserParam) {
        return CommonResult.data(sysUserService.userSelector(sysUserSelectorUserParam));
    }
}
