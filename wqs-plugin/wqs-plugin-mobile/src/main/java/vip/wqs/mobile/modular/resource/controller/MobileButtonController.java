
package vip.wqs.mobile.modular.resource.controller;

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
import vip.wqs.mobile.modular.resource.entity.MobileButton;
import vip.wqs.mobile.modular.resource.param.button.MobileButtonAddParam;
import vip.wqs.mobile.modular.resource.param.button.MobileButtonEditParam;
import vip.wqs.mobile.modular.resource.param.button.MobileButtonIdParam;
import vip.wqs.mobile.modular.resource.param.button.MobileButtonPageParam;
import vip.wqs.mobile.modular.resource.service.MobileButtonService;

import javax.validation.Valid;
import java.util.List;

/**
 * 移动端按钮控制器
 *
 * @author jetox
 * @date 2022/6/27 13:56
 **/
@Tag(name = "移动端按钮控制器")
@RestController
@Validated
public class MobileButtonController {

    @Resource
    private MobileButtonService mobileButtonService;

    /**
     * 获取移动端按钮分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取移动端按钮分页")
    @GetMapping("/mobile/button/page")
    public CommonResult<Page<MobileButton>> page(MobileButtonPageParam mobileButtonPageParam) {
        return CommonResult.data(mobileButtonService.page(mobileButtonPageParam));
    }

    /**
     * 添加移动端按钮
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加移动端按钮")
    @CommonLog("添加移动端按钮")
    @SaCheckPermission("/mobile/button/add")
    @PostMapping("/mobile/button/add")
    public CommonResult<String> add(@RequestBody @Valid MobileButtonAddParam mobileButtonAddParam) {
        mobileButtonService.add(mobileButtonAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑移动端按钮
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑移动端按钮")
    @CommonLog("编辑移动端按钮")
    @SaCheckPermission("/mobile/button/edit")
    @PostMapping("/mobile/button/edit")
    public CommonResult<String> edit(@RequestBody @Valid MobileButtonEditParam mobileButtonEditParam) {
        mobileButtonService.edit(mobileButtonEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除移动端按钮
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除移动端按钮")
    @CommonLog("删除移动端按钮")
    @SaCheckPermission("/mobile/button/delete")
    @PostMapping("/mobile/button/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空") List<MobileButtonIdParam> mobileButtonIdParamList) {
        mobileButtonService.delete(mobileButtonIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取移动端按钮详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取移动端按钮详情")
    @GetMapping("/mobile/button/detail")
    public CommonResult<MobileButton> detail(@Valid MobileButtonIdParam mobileButtonIdParam) {
        return CommonResult.data(mobileButtonService.detail(mobileButtonIdParam));
    }
}
