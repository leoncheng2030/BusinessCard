
package vip.wqs.client.core.enums;

import lombok.Getter;

/**
 * 是否枚举
 *
 * @author yubaoshan
 * @date 2024/9/17 00:14
 **/
@Getter
public enum ClientYesOrNoEnum {

    /** 是 */
    YES("YES"),

    /** 否 */
    NO("NO");

    private final String value;

    ClientYesOrNoEnum(String value) {
        this.value = value;
    }
}
