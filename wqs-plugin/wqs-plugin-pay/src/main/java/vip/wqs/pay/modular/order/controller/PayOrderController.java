
package vip.wqs.pay.modular.order.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.param.PayOrderPageParam;
import vip.wqs.pay.modular.order.service.PayOrderService;

/**
 * 支付流水控制器（管理后台）
 * <p>pay_order 为 pay 插件内部支付流水表，本控制器仅供管理后台对账排查使用，业务方不调用。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Tag(name = "支付流水控制器")
@RestController
@Validated
public class PayOrderController {

    @Resource
    private PayOrderService payOrderService;

    /**
     * 获取支付流水分页
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "获取支付流水分页")
    @CommonLog("获取支付流水分页")
    @GetMapping("/pay/order/page")
    public CommonResult<Page<PayOrder>> page(PayOrderPageParam payOrderPageParam) {
        return CommonResult.data(payOrderService.page(payOrderPageParam));
    }
}
