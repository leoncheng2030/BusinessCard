
package vip.wqs.pay.modular.order.provider;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.pay.api.PayApi;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.pojo.PayCreateParam;
import vip.wqs.pay.api.pojo.PayCreateResult;
import vip.wqs.pay.api.pojo.PayOrderInfo;
import vip.wqs.pay.api.pojo.PayRefundParam;
import vip.wqs.pay.modular.order.service.PayOrderRefundService;
import vip.wqs.pay.modular.order.service.PayOrderService;

/**
 * 支付API实现类
 * <p>对外 PayApi 的统一实现，业务插件注入 PayApi 即可完成创建支付、查询状态、退款等操作。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Service
public class PayApiProvider implements PayApi {

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayOrderRefundService payOrderRefundService;

    @Override
    public PayCreateResult createOrder(PayCreateParam payCreateParam) {
        return payOrderService.createOrder(payCreateParam);
    }

    @Override
    public PayStatusEnum queryStatus(String outTradeNo) {
        return payOrderService.queryStatus(outTradeNo);
    }

    @Override
    public PayOrderInfo getOrderInfo(String outTradeNo) {
        return payOrderService.getOrderInfo(outTradeNo);
    }

    @Override
    public void refund(PayRefundParam payRefundParam) {
        payOrderRefundService.refund(payRefundParam);
    }
}
