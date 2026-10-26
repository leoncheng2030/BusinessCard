
package vip.wqs.dev.modular.sms.controller;

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
import vip.wqs.common.annotation.CommonNoRepeat;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.modular.sms.entity.DevSms;
import vip.wqs.dev.modular.sms.param.*;
import vip.wqs.dev.modular.sms.service.DevSmsService;

import javax.validation.Valid;
import java.util.List;

/**
 * 短信控制器
 *
 * @author jetox
 * @date 2022/2/23 18:26
 **/
@Tag(name = "短信控制器")
@RestController
@Validated
public class DevSmsController {

    @Resource
    private DevSmsService devSmsService;

    /**
     * 动态发送短信
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "动态发送短信")
    @CommonLog("动态发送短信")
    @CommonNoRepeat
    @PostMapping("/dev/sms/sendDynamic")
    public CommonResult<String> sendDynamic(@RequestBody @Valid DevSmsSendDynamicParam devSmsSendDynamicParam) {
        devSmsService.sendDynamic(devSmsSendDynamicParam);
        return CommonResult.ok();
    }

    /**
     * 发送短信——阿里云
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "发送阿里云短信")
    @CommonLog("发送阿里云短信")
    @CommonNoRepeat
    @PostMapping("/dev/sms/sendAliyun")
    public CommonResult<String> sendAliyun(@RequestBody @Valid DevSmsSendAliyunParam devSmsSendAliyunParam) {
        devSmsService.sendAliyun(devSmsSendAliyunParam);
        return CommonResult.ok();
    }

    /**
     * 发送短信——腾讯云
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    @Operation(summary = "发送腾讯云短信")
    @CommonLog("发送腾讯云短信")
    @CommonNoRepeat
    @PostMapping("/dev/sms/sendTencent")
    public CommonResult<String> sendTencent(@RequestBody @Valid DevSmsSendTencentParam devSmsSendTencentParam) {
        devSmsService.sendTencent(devSmsSendTencentParam);
        return CommonResult.ok();
    }

    /**
     * 获取短信分页
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取短信分页")
    @GetMapping("/dev/sms/page")
    public CommonResult<Page<DevSms>> page(DevSmsPageParam devSmsPageParam) {
        return CommonResult.data(devSmsService.page(devSmsPageParam));
    }

    /**
     * 删除短信
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "删除短信")
    @CommonLog("删除短信")
    @PostMapping("/dev/sms/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                       List<DevSmsIdParam> devSmsIdParamList) {
        devSmsService.delete(devSmsIdParamList);
        return CommonResult.ok();
    }

    /**
     * 获取短信详情
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取短信详情")
    @GetMapping("/dev/sms/detail")
    public CommonResult<DevSms> detail(@Valid DevSmsIdParam devSmsIdParam) {
        return CommonResult.data(devSmsService.detail(devSmsIdParam));
    }
}
