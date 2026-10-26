
package vip.wqs.dev.modular.monitor.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.modular.monitor.result.DevMonitorServerResult;
import vip.wqs.dev.modular.monitor.service.DevMonitorService;

/**
 * 监控控制器
 *
 * @author jetox
 * @date 2022/6/21 14:57
 **/
@Tag(name = "监控控制器")
@RestController
@Validated
public class DevMonitorController {

    @Resource
    private DevMonitorService devMonitorService;

    /**
     * 获取服务器监控信息
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    @Operation(summary = "获取服务器监控信息")
    @GetMapping("/dev/monitor/serverInfo")
    public CommonResult<DevMonitorServerResult> serverInfo() {
        return CommonResult.data(devMonitorService.serverInfo());
    }

    /**
     * 获取服务器网络情况
     *
     * @author diantu
     * @date 2023/7/27
     */
    @Operation(summary = "获取服务器网络情况")
    @GetMapping("/dev/monitor/networkInfo")
    public CommonResult<DevMonitorServerResult> networkInfo() {
        return CommonResult.data(devMonitorService.networkInfo());
    }
}
