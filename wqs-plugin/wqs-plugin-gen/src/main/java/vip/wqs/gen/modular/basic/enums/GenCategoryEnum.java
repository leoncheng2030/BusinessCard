
package vip.wqs.gen.modular.basic.enums;

import lombok.Getter;
import vip.wqs.common.exception.CommonException;

/**
 * 代码生成类型枚举
 *
 * @author jetox
 * @date 2026/04/08
 **/
@Getter
public enum GenCategoryEnum {

    /** 单表格 */
    TABLE("TABLE", "单表格"),

    /** 单树表 */
    TREE("TREE", "单树表"),

    /** 左树右表（双表） */
    LEFT_TREE_TABLE("LEFT_TREE_TABLE", "左树右表"),

    /** 主子表（双表） */
    MASTER_DETAIL("MASTER_DETAIL", "主子表");

    private final String value;

    private final String description;

    GenCategoryEnum(String value, String description) {
        this.value = value;
        this.description = description;
    }

    public static void validate(String value) {
        boolean flag = TABLE.getValue().equals(value) || TREE.getValue().equals(value)
                || LEFT_TREE_TABLE.getValue().equals(value) || MASTER_DETAIL.getValue().equals(value);
        if (!flag) {
            throw new CommonException("不支持的代码生成类型：{}", value);
        }
    }

    /**
     * 是否为双表类型
     */
    public static boolean isDualTable(String value) {
        return LEFT_TREE_TABLE.getValue().equals(value) || MASTER_DETAIL.getValue().equals(value);
    }

    /**
     * 是否为树类型（单树表或左树右表）
     */
    public static boolean isTreeType(String value) {
        return TREE.getValue().equals(value) || LEFT_TREE_TABLE.getValue().equals(value);
    }
}
