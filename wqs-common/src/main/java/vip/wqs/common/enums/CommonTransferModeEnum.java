
package vip.wqs.common.enums;

import lombok.Getter;
import vip.wqs.common.exception.CommonException;

/**
 * 通用权限转移模式枚举
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Getter
public enum CommonTransferModeEnum {

    TRANSFER("TRANSFER", "转移"),

    COPY("COPY", "复制"),

    DELETE("DELETE", "删除");

    private final String value;

    private final String name;

    CommonTransferModeEnum(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public static void validate(String value) {
        boolean flag = TRANSFER.getValue().equals(value) || COPY.getValue().equals(value) || DELETE.getValue().equals(value);
        if(!flag) {
            throw new CommonException("不支持的转移模式：{}", value);
        }
    }
}
