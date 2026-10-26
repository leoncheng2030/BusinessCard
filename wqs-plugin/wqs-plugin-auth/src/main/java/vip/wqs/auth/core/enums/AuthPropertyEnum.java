
package vip.wqs.auth.core.enums;

import lombok.Getter;
import vip.wqs.common.exception.CommonException;

/**
 * 认证源属性枚举
 *
 * @author jetox
 * @date  2024/11/20 15:47
 **/
@Getter
public enum AuthPropertyEnum {

    /** ID */
    ID("ID"),

    /** 账号 */
    ACCOUNT("ACCOUNT"),

    /** 手机 */
    PHONE("PHONE"),

    /** 邮箱 */
    EMAIL("EMAIL");

    private final String value;

    AuthPropertyEnum(String value) {
        this.value = value;
    }

    public static void validate(String value) {
        boolean flag = ID.getValue().equals(value) || ACCOUNT.getValue().equals(value)
                || PHONE.getValue().equals(value)|| EMAIL.getValue().equals(value);
        if(!flag) {
            throw new CommonException("不支持的认证源属性：{}", value);
        }
    }
}
