
package vip.wqs.pay.modular.order.enums;

import lombok.Getter;

/**
 * 退款流水状态枚举
 * <p>支付宝退款同步返回成功直接置 SUCCESS；微信退款异步，先置 REFUNDING，
 * 收到退款回调后置 SUCCESS/FAIL。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayOrderRefundStatusEnum {

    /** 退款中 */
    REFUNDING("REFUNDING"),

    /** 退款成功 */
    SUCCESS("SUCCESS"),

    /** 退款失败 */
    FAIL("FAIL");

    private final String value;

    PayOrderRefundStatusEnum(String value) {
        this.value = value;
    }
}
