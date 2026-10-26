
package vip.wqs.sys.modular.user.enums;

import lombok.Getter;

/**
 * 邮件发送引擎类型枚举
 *
 * @author jetox
 * @date 2022/6/16 16:14
 **/
@Getter
public enum SysEmailEngineTypeEnum {

    /** 本地 */
    LOCAL("LOCAL"),

    /** 阿里云 */
    ALIYUN("ALIYUN"),

    /** 腾讯云 */
    TENCENT("TENCENT");

    private final String value;

    SysEmailEngineTypeEnum(String value) {
        this.value = value;
    }
}
