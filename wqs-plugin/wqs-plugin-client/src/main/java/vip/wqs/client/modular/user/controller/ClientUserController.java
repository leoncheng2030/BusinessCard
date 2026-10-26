
package vip.wqs.client.modular.user.controller;

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
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.enums.ClientUserSourceFromTypeEnum;
import vip.wqs.client.modular.user.param.ClientUserAddParam;
import vip.wqs.client.modular.user.param.ClientUserEditParam;
import vip.wqs.client.modular.user.param.ClientUserIdParam;
import vip.wqs.client.modular.user.param.ClientUserPageParam;
import vip.wqs.client.modular.user.service.ClientUserService;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;

import javax.validation.Valid;
import java.util.List;

/**
 * C端用户控制器
 *
 * @author jetox
 * @date 2022/4/22 9:34
 **/
@Tag(name = "C端用户控制器")
@RestController
@Validated
public class ClientUserController {

    @Resource
    private ClientUserService clientUserService;

    /**
     * 获取用户分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取用户分页")
    @GetMapping("/client/user/page")
    public CommonResult<Page<ClientUser>> page(ClientUserPageParam clientUserPageParam) {
        return CommonResult.data(clientUserService.page(clientUserPageParam));
    }

    /**
     * 添加用户
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "添加用户")
    @CommonLog("添加用户")
    @SaCheckPermission("/client/user/add")
    @PostMapping("/client/user/add")
    public CommonResult<String> add(@RequestBody @Valid ClientUserAddParam clientUserAddParam) {
        clientUserService.add(clientUserAddParam, ClientUserSourceFromTypeEnum.SYSTEM_ADD.getValue());
        return CommonResult.ok();
    }

    /**
     * 编辑用户
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "编辑用户")
    @CommonLog("编辑用户")
    @SaCheckPermission("/client/user/edit")
    @PostMapping("/client/user/edit")
    public CommonResult<String> edit(@RequestBody @Valid ClientUserEditParam clientUserEditParam) {
        clientUserService.edit(clientUserEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除用户
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除用户")
    @CommonLog("删除用户")
    @SaCheckPermission("/client/user/delete")
    @PostMapping("/client/user/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                       List<ClientUserIdParam> clientUserIdParamList) {
        clientUserService.delete(clientUserIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取用户详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取用户详情")
    @GetMapping("/client/user/detail")
    public CommonResult<ClientUser> detail(@Valid ClientUserIdParam clientUserIdParam) {
        return CommonResult.data(clientUserService.detail(clientUserIdParam));
    }

    /**
     * 禁用用户
     *
     * @author yubaoshan
     * @date 2026/3/30 23:50
     */
    @Operation(summary = "禁用用户")
    @CommonLog("禁用用户")
    @SaCheckPermission("/client/user/disableUser")
    @PostMapping("/client/user/disableUser")
    public CommonResult<String> disableUser(@RequestBody @Valid ClientUserIdParam clientUserIdParam) {
        clientUserService.disableUser(clientUserIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用用户
     *
     * @author yubaoshan
     * @date 2026/3/30 23:50
     */
    @Operation(summary = "启用用户")
    @CommonLog("启用用户")
    @SaCheckPermission("/client/user/enableUser")
    @PostMapping("/client/user/enableUser")
    public CommonResult<String> enableUser(@RequestBody @Valid ClientUserIdParam clientUserIdParam) {
        clientUserService.enableUser(clientUserIdParam);
        return CommonResult.ok();
    }
}
