
package vip.wqs.sys.modular.org.enums;

import lombok.Getter;

/**
 * 组织来源类型枚举
 *
 * @author jetox
 * @date 2022/4/21 19:56
 **/
@Getter
public enum SysOrgSourceFromTypeEnum {

    /** 系统自建 */
    SYSTEM_ADD("SYSTEM_ADD"),

    /** 身份源 */
    ID_SOURCE("ID_SOURCE");

    private final String value;

    SysOrgSourceFromTypeEnum(String value) {
        this.value = value;
    }
}
