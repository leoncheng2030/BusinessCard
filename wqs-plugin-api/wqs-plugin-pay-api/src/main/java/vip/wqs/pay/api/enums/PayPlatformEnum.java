
package vip.wqs.pay.api.enums;

import lombok.Getter;

/**
 * 支付平台枚举
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayPlatformEnum {

    /** 支付宝 */
    ALIPAY("ALIPAY"),

    /** 微信支付 */
    WXPAY("WXPAY");

    private final String value;

    PayPlatformEnum(String value) {
        this.value = value;
    }
}
