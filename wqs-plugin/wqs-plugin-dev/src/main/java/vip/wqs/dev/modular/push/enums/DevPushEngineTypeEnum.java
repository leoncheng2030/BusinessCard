
package vip.wqs.dev.modular.push.enums;

import lombok.Getter;

/**
 * 消息推送引擎类型枚举
 *
 * @author jetox
 * @date 2022/6/16 16:14
 **/
@Getter
public enum DevPushEngineTypeEnum {

    /** 钉钉 */
    DINGTALK("DINGTALK"),

    /** 飞书 */
    FEISHU("FEISHU"),

    /** 企业微信 */
    WORKWECHAT("WORKWECHAT");

    private final String value;

    DevPushEngineTypeEnum(String value) {
        this.value = value;
    }
}
