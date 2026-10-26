
package vip.wqs.dev.modular.config.controller;

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
import vip.wqs.dev.modular.config.entity.DevConfig;
import vip.wqs.dev.modular.config.param.*;
import vip.wqs.dev.modular.config.service.DevConfigService;
import vip.wqs.sys.api.SysOrgApi;
import vip.wqs.sys.api.SysPositionApi;
import vip.wqs.sys.api.SysRoleApi;

import javax.validation.Valid;
import java.util.List;

/**
 * 配置控制器
 *
 * @author jetox
 * @date 2022/4/22 10:56
 **/
@Tag(name = "配置控制器")
@RestController
@Validated
public class DevConfigController {

    @Resource
    private DevConfigService devConfigService;

    @Resource
    private SysPositionApi sysPositionApi;

    @Resource
    private SysOrgApi sysOrgApi;

    @Resource
    private SysRoleApi sysRoleApi;

    /**
     * 获取配置分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取配置分页")
    @GetMapping("/dev/config/page")
    public CommonResult<Page<DevConfig>> page(DevConfigPageParam devConfigPageParam) {
        return CommonResult.data(devConfigService.page(devConfigPageParam));
    }

    /**
     * 获取系统基础配置
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取系统基础配置")
    @GetMapping("/dev/config/sysBaseList")
    public CommonResult<List<DevConfig>> sysBaseList() {
        return CommonResult.data(devConfigService.sysBaseList());
    }

    /**
     * 获取系统三方登录开关配置
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取系统三方登录开关配置")
    @GetMapping("/dev/config/sysThirdAllowFlagList")
    public CommonResult<List<DevConfig>> sysThirdAllowFlagList() {
        return CommonResult.data(devConfigService.sysThirdAllowFlagList());
    }

    /**
     * 获取配置列表
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取配置列表")
    @GetMapping("/dev/config/list")
    public CommonResult<List<DevConfig>> list(DevConfigListParam devConfigListParam) {
        return CommonResult.data(devConfigService.list(devConfigListParam));
    }

    /**
     * 添加配置
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加配置")
    @CommonLog("添加配置")
    @PostMapping("/dev/config/add")
    public CommonResult<String> add(@RequestBody @Valid DevConfigAddParam devConfigAddParam) {
        devConfigService.add(devConfigAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑配置
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑配置")
    @CommonLog("编辑配置")
    @PostMapping("/dev/config/edit")
    public CommonResult<String> edit(@RequestBody @Valid DevConfigEditParam devConfigEditParam) {
        devConfigService.edit(devConfigEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除配置
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除配置")
    @CommonLog("删除配置")
    @PostMapping("/dev/config/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                           List<DevConfigIdParam> devConfigIdParamList) {
        devConfigService.delete(devConfigIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取配置详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取配置详情")
    @GetMapping("/dev/config/detail")
    public CommonResult<DevConfig> detail(@Valid DevConfigIdParam devConfigIdParam) {
        return CommonResult.data(devConfigService.detail(devConfigIdParam));
    }

    /**
     * 配置批量更新
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "配置批量更新")
    @CommonLog("配置批量更新")
    @PostMapping("/dev/config/editBatch")
    public CommonResult<String> editBatch(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                      List<DevConfigBatchParam> devConfigBatchParamList) {
        devConfigService.editBatch(devConfigBatchParamList);
        return CommonResult.ok();
    }

    /**
     * 获取组织树选择器（懒加载）
     *
     * @author yubaoshan
     * @date 2025/4/23 20:00
     */
    @Operation(summary = "获取组织树选择器（懒加载）")
    @GetMapping("/dev/config/orgTreeSelector")
    public CommonResult<List<JSONObject>> orgTreeSelector(DevConfigSelectorOrgTreeParam devConfigSelectorOrgTreeParam) {
        return CommonResult.data(sysOrgApi.orgTreeSelector(devConfigSelectorOrgTreeParam.getParentId(), devConfigSelectorOrgTreeParam.getSearchKey()));
    }

    /**
     * 获取角色选择器
     *
     * @author yubaoshan
     * @date 2025/4/23 20:00
     */
    @Operation(summary = "获取角色选择器")
    @GetMapping("/dev/config/roleSelector")
    public CommonResult<Page<JSONObject>> roleSelector(DevConfigSelectorRoleParam devConfigSelectorRoleParam) {
        return CommonResult.data(sysRoleApi.roleSelector(devConfigSelectorRoleParam.getOrgId(), devConfigSelectorRoleParam.getCategory(),
                devConfigSelectorRoleParam.getSearchKey(), null , false));
    }

    /**
     * 获取机构选择器
     *
     * @author yubaoshan
     * @date 2025/4/23 20:00
     */
    @Operation(summary = "获取机构选择器")
    @GetMapping("/dev/config/orgSelector")
    public CommonResult<Page<JSONObject>> orgSelector(DevConfigSelectorOrgListParam devConfigSelectorOrgListParam) {
        return CommonResult.data(sysOrgApi.orgListSelector(devConfigSelectorOrgListParam.getParentId(), devConfigSelectorOrgListParam.getSearchKey()));
    }

    /**
     * 获取职位选择器
     *
     * @author yubaoshan
     * @date 2025/4/23 20:00
     */
    @Operation(summary = "获取职位选择器")
    @GetMapping("/dev/config/positionSelector")
    public CommonResult<Page<JSONObject>> positionSelector(@Valid DevConfigSelectorPositionParam devConfigSelectorPositionParam) {
        return CommonResult.data(sysPositionApi.positionSelector(devConfigSelectorPositionParam.getOrgId(), devConfigSelectorPositionParam.getSearchKey()));
    }

}
