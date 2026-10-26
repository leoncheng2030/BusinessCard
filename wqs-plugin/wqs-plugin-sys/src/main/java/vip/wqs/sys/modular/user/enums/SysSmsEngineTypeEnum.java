
package vip.wqs.sys.modular.user.enums;

import lombok.Getter;

/**
 * 短信发送引擎类型枚举
 *
 * @author jetox
 * @date 2022/6/16 16:14
 **/
@Getter
public enum SysSmsEngineTypeEnum {

    /** 阿里云 */
    ALIYUN("ALIYUN"),

    /** 腾讯云 */
    TENCENT("TENCENT");

    private final String value;

    SysSmsEngineTypeEnum(String value) {
        this.value = value;
    }
}
