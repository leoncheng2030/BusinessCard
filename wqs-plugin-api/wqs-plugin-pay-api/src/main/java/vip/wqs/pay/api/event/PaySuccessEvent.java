
package vip.wqs.pay.api.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.Date;

/**
 * 支付成功事件
 * <p>支付宝/微信回调到达后，pay 插件自动发布本事件。业务方用 @EventListener 订阅，
 * 默认同步执行，处理较重可叠加 @Async（需开 @EnableAsync）。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public class PaySuccessEvent extends ApplicationEvent {

    /** 商户订单号（关联业务方自己的订单） */
    private final String outTradeNo;

    /** 支付平台流水号 */
    private final String tradeNo;

    /** 实际支付金额（元，字符串） */
    private final String payAmount;

    /** 支付平台：ALIPAY / WXPAY */
    private final String payPlatform;

    /** 支付完成时间 */
    private final Date payTime;

    /**
     * @param source      事件源对象
     * @param outTradeNo  商户订单号
     * @param tradeNo     支付平台流水号
     * @param payAmount   实际支付金额（元）
     * @param payPlatform 支付平台
     * @param payTime     支付完成时间
     **/
    public PaySuccessEvent(Object source, String outTradeNo, String tradeNo, String payAmount, String payPlatform, Date payTime) {
        super(source);
        this.outTradeNo = outTradeNo;
        this.tradeNo = tradeNo;
        this.payAmount = payAmount;
        this.payPlatform = payPlatform;
        this.payTime = payTime;
    }
}
