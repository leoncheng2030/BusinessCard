
package vip.wqs.pay.api;

import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.pojo.PayCreateParam;
import vip.wqs.pay.api.pojo.PayCreateResult;
import vip.wqs.pay.api.pojo.PayOrderInfo;
import vip.wqs.pay.api.pojo.PayRefundParam;

/**
 * 支付API
 * <p>统一支付能力接口：创建支付订单、查询简化状态、获取订单只读视图、发起退款。
 * 业务插件只依赖本接口，不直接读写 pay_order 支付流水表；
 * 支付成功/退款成功分别通过 PaySuccessEvent / PayRefundSuccessEvent 事件订阅。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 **/
public interface PayApi {

    /**
     * 创建支付订单（支付流水）
     *
     * @param payCreateParam 创建参数，outTradeNo 可传可不传：传了用业务方的，没传自动生成
     * @return 支付订单主键与最终生效的商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     **/
    PayCreateResult createOrder(PayCreateParam payCreateParam);

    /**
     * 根据商户订单号查询简化状态
     *
     * @param outTradeNo 商户订单号
     * @return 简化状态：UNPAID 未支付 / PAID 已支付 / CLOSED 已关闭 / REFUNDED 已发生退款
     * @author wqs
     * @date 2026/9/3 14:30
     **/
    PayStatusEnum queryStatus(String outTradeNo);

    /**
     * 根据商户订单号获取订单只读视图
     *
     * @param outTradeNo 商户订单号
     * @return 订单只读视图，查不到返回 null
     * @author wqs
     * @date 2026/9/3 14:30
     **/
    PayOrderInfo getOrderInfo(String outTradeNo);

    /**
     * 发起退款
     * <p>支付宝退款同步返回成功；微信退款异步回调，退款成功均通过 PayRefundSuccessEvent 事件通知。</p>
     *
     * @param payRefundParam 退款参数
     * @author wqs
     * @date 2026/9/3 14:30
     **/
    void refund(PayRefundParam payRefundParam);
}
