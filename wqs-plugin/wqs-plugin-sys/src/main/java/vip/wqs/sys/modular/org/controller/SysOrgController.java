
package vip.wqs.sys.modular.org.controller;

import cn.hutool.json.JSONObject;
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
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.org.enums.SysOrgSourceFromTypeEnum;
import vip.wqs.sys.modular.org.param.*;
import vip.wqs.sys.modular.org.service.SysOrgService;
import vip.wqs.sys.modular.user.entity.SysUser;

import javax.validation.Valid;
import java.util.List;

/**
 * 组织控制器
 *
 * @author jetox
 * @date 2022/4/24 19:55
 */
@Tag(name = "组织控制器")
@RestController
@Validated
public class SysOrgController {

    @Resource
    private SysOrgService sysOrgService;

    /**
     * 获取组织分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取组织分页")
    @GetMapping("/sys/org/page")
    public CommonResult<Page<SysOrg>> page(SysOrgPageParam sysOrgPageParam) {
        return CommonResult.data(sysOrgService.page(sysOrgPageParam));
    }

    /**
     * 获取组织树（懒加载）
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取组织树（懒加载）")
    @GetMapping("/sys/org/tree")
    public CommonResult<List<JSONObject>> tree(SysOrgSelectorTreeParam sysOrgSelectorTreeParam) {
        return CommonResult.data(sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam));
    }

    /**
     * 添加组织
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加组织")
    @CommonLog("添加组织")
    @PostMapping("/sys/org/add")
    public CommonResult<String> add(@RequestBody @Valid SysOrgAddParam sysOrgAddParam) {
        sysOrgService.add(sysOrgAddParam, SysOrgSourceFromTypeEnum.SYSTEM_ADD.getValue());
        return CommonResult.ok();
    }

    /**
     * 编辑组织
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑组织")
    @CommonLog("编辑组织")
    @PostMapping("/sys/org/edit")
    public CommonResult<String> edit(@RequestBody @Valid SysOrgEditParam sysOrgEditParam) {
        sysOrgService.edit(sysOrgEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除组织
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除组织")
    @CommonLog("删除组织")
    @PostMapping("/sys/org/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<SysOrgIdParam> sysOrgIdParamList) {
        sysOrgService.delete(sysOrgIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取组织详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取组织详情")
    @GetMapping("/sys/org/detail")
    public CommonResult<SysOrg> detail(@Valid SysOrgIdParam sysOrgIdParam) {
        return CommonResult.data(sysOrgService.detail(sysOrgIdParam));
    }

    /**
     * 复制组织
     *
     * @author yubaoshan
     * @date 2025/12/24 01:10
     */
    @Operation(summary = "复制组织")
    @CommonLog("复制组织")
    @PostMapping("/sys/org/copy")
    public CommonResult<String> copy(@RequestBody @Valid SysOrgCopyParam sysOrgCopyParam) {
        sysOrgService.copy(sysOrgCopyParam);
        return CommonResult.ok();
    }

    /* ====组织部分所需要用到的选择器==== */

    /**
     * 获取组织树选择器（懒加载）
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取组织树选择器（懒加载）")
    @GetMapping("/sys/org/orgTreeSelector")
    public CommonResult<List<JSONObject>> orgTreeSelector(SysOrgSelectorTreeParam sysOrgSelectorTreeParam) {
        return CommonResult.data(sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam));
    }

    /**
     * 获取用户选择器
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取用户选择器")
    @GetMapping("/sys/org/userSelector")
    public CommonResult<Page<SysUser>> userSelector(SysOrgSelectorUserParam sysOrgSelectorUserParam) {
        return CommonResult.data(sysOrgService.userSelector(sysOrgSelectorUserParam));
    }

    /**
     * 根据orgId列表获取祖先路径节点（用于懒加载树回显）
     *
     * @author yubaoshan
     * @date 2026/3/22
     */
    @Operation(summary = "根据orgId列表获取祖先路径节点")
    @PostMapping("/sys/org/getAncestorNodes")
    public CommonResult<List<JSONObject>> getAncestorNodes(@RequestBody List<String> orgIdList) {
        return CommonResult.data(sysOrgService.getAncestorNodes(orgIdList));
    }
}
