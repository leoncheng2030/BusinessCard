
package vip.wqs.dev.modular.message.enums;

import lombok.Getter;

/**
 * 分类枚举
 *
 * @author jetox
 * @date 2022/6/16 16:14
 **/
@Getter
public enum DevMessageCategoryEnum {

    /** 系统 */
    SYS("SYS"),

    /** 业务 */
    BIZ("BIZ");

    private final String value;

    DevMessageCategoryEnum(String value) {
        this.value = value;
    }
}
