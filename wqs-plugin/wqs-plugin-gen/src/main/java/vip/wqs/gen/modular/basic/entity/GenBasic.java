
package vip.wqs.gen.modular.basic.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 代码生成基础
 *
 * @author yubaoshan
 * @date 2022/10/25 22:33
 **/
@Getter
@Setter
@TableName("GEN_BASIC")
public class GenBasic extends CommonEntity {

    /** id */
    @Schema(description = "id")
    private String id;

    /** 主表名称 */
    @Schema(description = "主表名称")
    private String dbTable;

    /** 主表主键 */
    @Schema(description = "主表主键")
    private String dbTableKey;

    /** 插件名 */
    @Schema(description = "插件名")
    private String pluginName;

    /** 模块名 */
    @Schema(description = "模块名")
    private String moduleName;

    /** 表前缀移除 */
    @Schema(description = "表前缀移除")
    private String tablePrefix;

    /** 生成方式 */
    @Schema(description = "生成方式")
    private String generateType;

    /** 所属模块 */
    @Schema(description = "所属模块")
    private String module;

    /** 上级目录 */
    @Schema(description = "上级目录")
    private String menuPid;

    /** 移动端所属模块 */
    @Schema(description = "移动端所属模块")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String mobileModule;

    /** 功能名 */
    @Schema(description = "功能名")
    private String functionName;

    /** 业务名 */
    @Schema(description = "业务名")
    private String busName;

    /** 类名 */
    @Schema(description = "类名")
    private String className;

    /** 表单布局 */
    @Schema(description = "表单布局")
    private String formLayout;

    /** 使用栅格 */
    @Schema(description = "使用栅格")
    private String gridWhether;

    /** 排序 */
    @Schema(description = "排序")
    private Integer sortCode;

    /** 包名 */
    @Schema(description = "包名")
    private String packageName;

    /** 作者 */
    @Schema(description = "作者")
    private String authorName;

    /** 生成类型：TABLE/TREE/LEFT_TREE_TABLE/MASTER_DETAIL */
    @Schema(description = "生成类型")
    private String genType;

    /** 树父级字段名（单树表专用） */
    @Schema(description = "树父级字段名")
    private String treeParentField;

    /** 树显示名称字段名（单树表专用） */
    @Schema(description = "树显示名称字段名")
    private String treeNameField;

    /** 子表表名（双表专用） */
    @Schema(description = "子表表名")
    private String subDbTable;

    /** 子表主键（双表专用） */
    @Schema(description = "子表主键")
    private String subDbTableKey;

    /** 子表关联外键（双表专用） */
    @Schema(description = "子表关联外键")
    private String subForeignKey;

    /** 子表类名（双表专用） */
    @Schema(description = "子表类名")
    private String subClassName;

    /** 子表功能名（双表专用） */
    @Schema(description = "子表功能名")
    private String subFunctionName;

    /** 子表业务名（双表专用） */
    @Schema(description = "子表业务名")
    private String subBusName;
}
