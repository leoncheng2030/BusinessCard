
package vip.wqs.sys.modular.resource.controller;

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
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.sys.modular.resource.entity.SysButton;
import vip.wqs.sys.modular.resource.param.button.SysButtonAddParam;
import vip.wqs.sys.modular.resource.param.button.SysButtonEditParam;
import vip.wqs.sys.modular.resource.param.button.SysButtonIdParam;
import vip.wqs.sys.modular.resource.param.button.SysButtonPageParam;
import vip.wqs.sys.modular.resource.service.SysButtonService;

import javax.validation.Valid;
import java.util.List;

/**
 * 按钮控制器
 *
 * @author jetox
 * @date 2022/6/27 13:56
 **/
@Tag(name = "按钮控制器")
@RestController
@Validated
public class SysButtonController {

    @Resource
    private SysButtonService sysButtonService;

    /**
     * 获取按钮分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取按钮分页")
    @GetMapping("/sys/button/page")
    public CommonResult<Page<SysButton>> page(SysButtonPageParam sysButtonPageParam) {
        return CommonResult.data(sysButtonService.page(sysButtonPageParam));
    }

    /**
     * 添加按钮
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加按钮")
    @CommonLog("添加按钮")
    @SaCheckPermission("/sys/button/add")
    @PostMapping("/sys/button/add")
    public CommonResult<String> add(@RequestBody @Valid SysButtonAddParam sysButtonAddParam) {
        sysButtonService.add(sysButtonAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑按钮
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑按钮")
    @CommonLog("编辑按钮")
    @SaCheckPermission("/sys/button/edit")
    @PostMapping("/sys/button/edit")
    public CommonResult<String> edit(@RequestBody @Valid SysButtonEditParam sysButtonEditParam) {
        sysButtonService.edit(sysButtonEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除按钮
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除按钮")
    @CommonLog("删除按钮")
    @SaCheckPermission("/sys/button/delete")
    @PostMapping("/sys/button/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")  List<SysButtonIdParam> sysButtonIdParamList) {
        sysButtonService.delete(sysButtonIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取按钮详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取按钮详情")
    @GetMapping("/sys/button/detail")
    public CommonResult<SysButton> detail(@Valid SysButtonIdParam sysButtonIdParam) {
        return CommonResult.data(sysButtonService.detail(sysButtonIdParam));
    }
}
