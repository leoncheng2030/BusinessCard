
package vip.wqs.pay.api.enums;

import lombok.Getter;

/**
 * 支付简化状态枚举
 * <p>业务方只需理解这 4 个状态，pay 插件内部会把支付宝/微信原生状态映射过来。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayStatusEnum {

    /** 未支付（含下单未付、用户支付中） */
    UNPAID("UNPAID"),

    /** 已支付 */
    PAID("PAID"),

    /** 已关闭/撤销/失败 */
    CLOSED("CLOSED"),

    /** 已发生退款 */
    REFUNDED("REFUNDED");

    private final String value;

    PayStatusEnum(String value) {
        this.value = value;
    }
}
