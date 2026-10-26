
package vip.wqs.dev.modular.dict.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
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
import vip.wqs.dev.modular.dict.entity.DevDict;
import vip.wqs.dev.modular.dict.param.*;
import vip.wqs.dev.modular.dict.service.DevDictService;

import javax.validation.Valid;
import java.util.List;

/**
 * 字典控制器
 *
 * @author jetox
 * @date 2022/6/21 14:58
 **/
@Tag(name = "字典控制器")
@RestController
@Validated
public class DevDictController {

    @Resource
    private DevDictService devDictService;

    /**
     * 获取字典分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取字典分页")
    @GetMapping("/dev/dict/page")
    public CommonResult<Page<DevDict>> page(DevDictPageParam devDictPageParam) {
        return CommonResult.data(devDictService.page(devDictPageParam));
    }

    /**
     * 获取字典列表
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取字典列表")
    @GetMapping("/dev/dict/list")
    public CommonResult<List<DevDict>> list(DevDictListParam devDictListParam) {
        return CommonResult.data(devDictService.list(devDictListParam));
    }

    /**
     * 获取字典树
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取字典树")
    @GetMapping("/dev/dict/tree")
    public CommonResult<List<Tree<String>>> tree(DevDictTreeParam devDictTreeParam) {
        return CommonResult.data(devDictService.tree(devDictTreeParam));
    }

    /**
     * 添加字典
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加字典")
    @CommonLog("添加字典")
    @SaCheckPermission("/dev/dict/add")
    @PostMapping("/dev/dict/add")
    public CommonResult<String> add(@RequestBody @Valid DevDictAddParam devDictAddParam) {
        devDictService.add(devDictAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑字典
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑字典")
    @CommonLog("编辑字典")
    @SaCheckPermission("/dev/dict/edit")
    @PostMapping("/dev/dict/edit")
    public CommonResult<String> edit(@RequestBody @Valid DevDictEditParam devDictEditParam) {
        devDictService.edit(devDictEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除字典
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除字典")
    @CommonLog("删除字典")
    @SaCheckPermission("/dev/dict/delete")
    @PostMapping("/dev/dict/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                           List<DevDictIdParam> devDictIdParamList) {
        devDictService.delete(devDictIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取字典详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取字典详情")
    @GetMapping("/dev/dict/detail")
    public CommonResult<DevDict> detail(@Valid DevDictIdParam devDictIdParam) {
        return CommonResult.data(devDictService.detail(devDictIdParam));
    }
}
