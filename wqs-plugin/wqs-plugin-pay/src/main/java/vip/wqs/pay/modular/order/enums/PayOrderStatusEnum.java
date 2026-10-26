
package vip.wqs.pay.modular.order.enums;

import lombok.Getter;

/**
 * 支付流水状态枚举
 * <p>与对外 PayStatusEnum 一一对应，PAY_ORDER.STATUS 直接存本枚举值，
 * 支付宝/微信原生状态在回调与查询时统一映射到本枚举。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayOrderStatusEnum {

    /** 未支付（含下单未付、用户支付中） */
    UNPAID("UNPAID"),

    /** 已支付 */
    PAID("PAID"),

    /** 已关闭/撤销/失败 */
    CLOSED("CLOSED"),

    /** 已发生退款 */
    REFUNDED("REFUNDED");

    private final String value;

    PayOrderStatusEnum(String value) {
        this.value = value;
    }
}
