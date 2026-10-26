
package vip.wqs.auth.core.enums;

import lombok.Getter;
import vip.wqs.common.exception.CommonException;

/**
 * 第三方登录平台枚举
 *
 * @author jetox
 * @date 2021/10/11 14:02
 **/
@Getter
public enum AuthPlatformEnum {

    /** 微信 */
    WECHAT("WECHAT", "微信"),

    /** 微信小程序 */
    WECHAT_MINI("WECHAT_MINI", "微信小程序");

    private final String value;

    private final String description;

    AuthPlatformEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static void validate(String value) {
        boolean flag = WECHAT.getValue().equals(value) ||
                WECHAT_MINI.getValue().equals(value);
        if(!flag) {
            throw new CommonException("不支持的认证源平台：{}", value);
        }
    }
}
