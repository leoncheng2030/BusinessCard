
package vip.wqs.pay.api.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * 退款成功事件
 * <p>支付宝退款是同步返回成功的，事件在创建退款记录时立即发布；
 * 微信退款异步回调，事件在收到回调时发布。两种情况都会触发同一个事件类。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public class PayRefundSuccessEvent extends ApplicationEvent {

    /** 商户订单号（关联业务方自己的订单） */
    private final String outTradeNo;

    /** 退款单号 */
    private final String refundNo;

    /** 退款金额（元，字符串） */
    private final String refundAmount;

    /** 退款完成时间 */
    private final Date refundTime;

    /**
     * @param source       事件源对象
     * @param outTradeNo   商户订单号
     * @param refundNo     退款单号
     * @param refundAmount 退款金额（元）
     * @param refundTime   退款完成时间
     **/
    public PayRefundSuccessEvent(Object source, String outTradeNo, String refundNo, String refundAmount, Date refundTime) {
        super(source);
        this.outTradeNo = outTradeNo;
        this.refundNo = refundNo;
        this.refundAmount = refundAmount;
        this.refundTime = refundTime;
    }
}
