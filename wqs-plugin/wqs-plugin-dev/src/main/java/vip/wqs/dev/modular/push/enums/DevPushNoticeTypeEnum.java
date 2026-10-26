
package vip.wqs.dev.modular.push.enums;

import lombok.Getter;

/**
 * 消息推送通知类型枚举
 *
 * @author jetox
 * @date 2022/6/16 16:14
 **/
@Getter
public enum DevPushNoticeTypeEnum {

    /** 无通知 */
    NONE("NONE"),

    /** 通知指定手机号 */
    PHONE("PHONE"),

    /** 通知所有人 */
    ALL("ALL");

    private final String value;

    DevPushNoticeTypeEnum(String value) {
        this.value = value;
    }
}
