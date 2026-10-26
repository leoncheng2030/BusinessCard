
package vip.wqs.sys.core.enums;

import lombok.Getter;

/**
 * 系统模块数据类型枚举
 *
 * @author jetox
 * @date 2023/3/3 10:40
 **/
@Getter
public enum SysDataTypeEnum {

    /**
     * 组织
     */
    ORG("ORG"),

    /**
     * 职位
     */
    POSITION("POSITION"),

    /**
     * 资源
     */
    RESOURCE("RESOURCE"),

    /**
     * 角色
     */
    ROLE("ROLE"),

    /**
     * 用户
     */
    USER("USER"),

    /**
     * 用户组
     */
    GROUP("GROUP");

    private final String value;

    SysDataTypeEnum(String value) {
        this.value = value;
    }

}
