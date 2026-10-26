
package vip.wqs.pay.modular.order.enums;

import lombok.Getter;

/**
 * 支付交互明细类型枚举
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayOrderDetailsTypeEnum {

    /** 创建支付订单 */
    CREATE("CREATE"),

    /** 渠道下单 */
    ORDER("ORDER"),

    /** 渠道查询 */
    QUERY("QUERY"),

    /** 渠道回调 */
    NOTIFY("NOTIFY"),

    /** 渠道退款 */
    REFUND("REFUND");

    private final String value;

    PayOrderDetailsTypeEnum(String value) {
        this.value = value;
    }
}
