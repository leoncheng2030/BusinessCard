
package vip.wqs.common.enums;

import lombok.Getter;

/**
 * 通用性别枚举
 *
 * @author jetox
 * @date 2022/7/13 17:48
 **/
@Getter
public enum CommonGenderEnum {

    /** 未知 */
    UNKNOWN("未知"),

    /** 男 */
    MAN("男"),

    /** 女 */
    WOMAN("女");

    private final String value;

    CommonGenderEnum(String value) {
        this.value = value.toLowerCase();
    }
}
