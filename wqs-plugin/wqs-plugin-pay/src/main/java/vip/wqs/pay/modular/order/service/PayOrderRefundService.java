
package vip.wqs.pay.modular.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.pay.api.pojo.PayRefundParam;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;
import vip.wqs.pay.modular.order.enums.PayOrderRefundStatusEnum;

import java.util.Date;

/**
 * 退款流水Service接口
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public interface PayOrderRefundService extends IService<PayOrderRefund> {

    /**
     * 发起退款
     * <p>支付宝退款同步返回成功，创建退款记录时立即发布 PayRefundSuccessEvent；
     * 微信退款异步回调，事件在收到回调时发布。</p>
     *
     * @param payRefundParam 退款参数
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void refund(PayRefundParam payRefundParam);

    /**
     * 根据退款单号查询退款流水
     *
     * @param refundNo 退款单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayOrderRefund getByRefundNo(String refundNo);

    /**
     * 更新退款流水状态（微信退款回调到达时调用）
     * <p>退款成功时发布 PayRefundSuccessEvent，并将支付流水状态置为 REFUNDED。</p>
     *
     * @param refundNo        退款单号
     * @param channelRefundNo 渠道退款单号
     * @param status          退款状态
     * @param refundTime      退款完成时间
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateRefundInfo(String refundNo, String channelRefundNo, PayOrderRefundStatusEnum status, Date refundTime);
}
