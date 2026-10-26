
package vip.wqs.pay.core.enums;

import lombok.Getter;

/**
 * 支付插件通用是否枚举
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
public enum PayYesNoEnum {

    /** 是 */
    YES("Y"),

    /** 否 */
    NO("N");

    private final String value;

    PayYesNoEnum(String value) {
        this.value = value;
    }
}
