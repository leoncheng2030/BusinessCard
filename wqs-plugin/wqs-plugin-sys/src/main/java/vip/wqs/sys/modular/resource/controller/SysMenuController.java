
package vip.wqs.sys.modular.resource.controller;

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
import vip.wqs.sys.modular.resource.entity.SysMenu;
import vip.wqs.sys.modular.resource.entity.SysModule;
import vip.wqs.sys.modular.resource.param.menu.*;
import vip.wqs.sys.modular.resource.service.SysMenuService;

import javax.validation.Valid;
import java.util.List;

/**
 * 菜单控制器
 *
 * @author jetox
 * @date 2022/6/27 14:09
 **/
@Tag(name = "菜单控制器")
@RestController
@Validated
public class SysMenuController {

    @Resource
    private SysMenuService sysMenuService;

    /**
     * 获取菜单分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取菜单分页")
    @GetMapping("/sys/menu/page")
    public CommonResult<Page<SysMenu>> page(SysMenuPageParam sysMenuPageParam) {
        return CommonResult.data(sysMenuService.page(sysMenuPageParam));
    }

    /**
     * 获取菜单树
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取菜单树")
    @GetMapping("/sys/menu/tree")
    public CommonResult<List<Tree<String>>> tree(SysMenuTreeParam sysMenuTreeParam) {
        return CommonResult.data(sysMenuService.tree(sysMenuTreeParam));
    }

    /**
     * 添加菜单
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加菜单")
    @CommonLog("添加菜单")
    @SaCheckPermission("/sys/menu/add")
    @PostMapping("/sys/menu/add")
    public CommonResult<String> add(@RequestBody @Valid SysMenuAddParam sysMenuAddParam) {
        sysMenuService.add(sysMenuAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑菜单
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑菜单")
    @CommonLog("编辑菜单")
    @SaCheckPermission("/sys/menu/edit")
    @PostMapping("/sys/menu/edit")
    public CommonResult<String> edit(@RequestBody @Valid SysMenuEditParam sysMenuEditParam) {
        sysMenuService.edit(sysMenuEditParam);
        return CommonResult.ok();
    }

    /**
     * 更改菜单所属模块
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "更改菜单所属模块")
    @CommonLog("更改菜单所属模块")
    @SaCheckPermission("/sys/menu/changeModule")
    @PostMapping("/sys/menu/changeModule")
    public CommonResult<String> changeModule(@RequestBody @Valid SysMenuChangeModuleParam sysMenuChangeModuleParam) {
        sysMenuService.changeModule(sysMenuChangeModuleParam);
        return CommonResult.ok();
    }

    /**
     * 删除菜单
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除菜单")
    @CommonLog("删除菜单")
    @SaCheckPermission("/sys/menu/delete")
    @PostMapping("/sys/menu/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<SysMenuIdParam> sysMenuIdParamList) {
        sysMenuService.delete(sysMenuIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取菜单详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取菜单详情")
    @GetMapping("/sys/menu/detail")
    public CommonResult<SysMenu> detail(@Valid SysMenuIdParam sysMenuIdParam) {
        return CommonResult.data(sysMenuService.detail(sysMenuIdParam));
    }

    /* ====菜单部分所需要用到的选择器==== */

    /**
     * 获取模块选择器
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取模块选择器")
    @GetMapping("/sys/menu/moduleSelector")
    public CommonResult<List<SysModule>> moduleSelector(SysMenuSelectorModuleParam sysMenuSelectorModuleParam) {
        return CommonResult.data(sysMenuService.moduleSelector(sysMenuSelectorModuleParam));
    }

    /**
     * 获取菜单树选择器
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取菜单树选择器")
    @GetMapping("/sys/menu/menuTreeSelector")
    public CommonResult<List<Tree<String>>> menuTreeSelector(SysMenuSelectorMenuParam sysMenuSelectorMenuParam) {
        return CommonResult.data(sysMenuService.menuTreeSelector(sysMenuSelectorMenuParam));
    }
}
