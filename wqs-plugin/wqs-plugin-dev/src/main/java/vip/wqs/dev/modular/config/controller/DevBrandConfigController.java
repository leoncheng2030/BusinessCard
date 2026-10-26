
package vip.wqs.dev.modular.config.controller;

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
import vip.wqs.dev.modular.config.entity.DevBrandConfig;
import vip.wqs.dev.modular.config.param.DevConfigBatchParam;
import vip.wqs.dev.modular.config.service.DevBrandConfigService;

import javax.validation.Valid;
import java.util.List;

/**
 * 平台品牌配置控制器（品牌全局共享）
 *
 * <p>读取接口免登录公开（见 GlobalConfigure 放行清单），供登录页/注册页/找回密码页
 * 登录前直接展示平台品牌；编辑接口受「仅超管接口」(/dev/config/**)保护。</p>
 *
 * @author wqs
 * @date 2026/9/4 10:00
 **/
@Tag(name = "平台品牌配置控制器")
@RestController
@Validated
public class DevBrandConfigController {

    @Resource
    private DevBrandConfigService devBrandConfigService;

    /**
     * 获取平台品牌配置（登录页/注册页/找回密码页免登读取）
     **/
    @Operation(summary = "获取平台品牌配置")
    @GetMapping("/dev/config/brandList")
    public CommonResult<List<DevBrandConfig>> brandList() {
        return CommonResult.data(devBrandConfigService.brandList());
    }

    /**
     * 编辑平台品牌配置
     **/
    @Operation(summary = "编辑平台品牌配置")
    @CommonLog("编辑平台品牌配置")
    @PostMapping("/dev/config/brandEdit")
    public CommonResult<String> brandEdit(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                      List<DevConfigBatchParam> devConfigBatchParamList) {
        devBrandConfigService.editBatch(devConfigBatchParamList);
        return CommonResult.ok();
    }
}
