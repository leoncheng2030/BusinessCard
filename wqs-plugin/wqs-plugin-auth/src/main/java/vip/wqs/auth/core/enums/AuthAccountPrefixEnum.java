
package vip.wqs.auth.core.enums;

import lombok.Getter;

/**
 * 认证账号前缀枚举
 *
 * @author yubaoshan
 * @date 2026/06/27
 **/
@Getter
public enum AuthAccountPrefixEnum {

    /** SSO单点登录自动创建的账号前缀 */
    SSO("sso_", "SSO单点登录");

    private final String value;
    private final String description;

    AuthAccountPrefixEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }
}
