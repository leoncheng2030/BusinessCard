package vip.wqs.dev.modular.dict.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.modular.dict.param.DevDictAddParam;
import vip.wqs.dev.modular.dict.param.DevDictEditParam;
import vip.wqs.dev.modular.dict.param.DevDictIdParam;
import vip.wqs.dev.modular.dict.service.BizDictService;

import java.util.List;

/**
 * 业务字典控制器（业务自助）
 *
 * <p>与数据字典（/dev/dict）互补：业务侧自助维护 BIZ 分类字典
 * （订单类型等业务类型值域），FRM 框架字典平台受控。</p>
 *
 * @author wqs
 * @date 2026/9/13 21:00
 */
@Tag(name = "业务字典控制器（业务自助）")
@RestController
@Validated
public class BizDictController {

    @Resource
    private BizDictService bizDictService;

    /**
     * 获取业务字典树（仅 BIZ 分类）
     */
    @Operation(summary = "获取业务字典树")
    @GetMapping("/dev/bizDict/tree")
    public CommonResult<List<Tree<String>>> tree() {
        return CommonResult.data(bizDictService.tree());
    }

    /**
     * 添加业务字典
     */
    @Operation(summary = "添加业务字典")
    @CommonLog("添加业务字典")
    @SaCheckPermission("/dev/bizDict/add")
    @PostMapping("/dev/bizDict/add")
    public CommonResult<String> add(@RequestBody @Valid DevDictAddParam devDictAddParam) {
        bizDictService.add(devDictAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑业务字典
     */
    @Operation(summary = "编辑业务字典")
    @CommonLog("编辑业务字典")
    @SaCheckPermission("/dev/bizDict/edit")
    @PostMapping("/dev/bizDict/edit")
    public CommonResult<String> edit(@RequestBody @Valid DevDictEditParam devDictEditParam) {
        bizDictService.edit(devDictEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除业务字典
     */
    @Operation(summary = "删除业务字典")
    @CommonLog("删除业务字典")
    @SaCheckPermission("/dev/bizDict/delete")
    @PostMapping("/dev/bizDict/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                               List<DevDictIdParam> devDictIdParamList) {
        bizDictService.delete(devDictIdParamList);
        return CommonResult.ok();
    }
}
