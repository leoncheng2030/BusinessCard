
package vip.wqs.gen.modular.basic.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 代码生成基础编辑参数
 *
 * @author yubaoshan
 * @date 2022/10/25 22:33
 **/
@Getter
@Setter
public class GenBasicEditParam {

    /** id */
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "id不能为空")
    private String id;

    /** 主表名称 */
    @Schema(description = "主表名称", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "dbTable不能为空")
    private String dbTable;

    /** 主表主键 */
    @Schema(description = "主表主键", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "dbTableKey不能为空")
    private String dbTableKey;

    /** 插件名 */
    @Schema(description = "插件名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "pluginName不能为空")
    private String pluginName;

    /** 模块名 */
    @Schema(description = "模块名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "moduleName不能为空")
    private String moduleName;

    /** 表前缀移除 */
    @Schema(description = "表前缀移除", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "tablePrefix不能为空")
    private String tablePrefix;

    /** 生成方式 */
    @Schema(description = "生成方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "generateType不能为空")
    private String generateType;

    /** 所属模块 */
    @Schema(description = "所属模块", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "module不能为空")
    private String module;

    /** 上级目录 */
    @Schema(description = "上级目录", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "menuPid不能为空")
    private String menuPid;

    /** 移动端所属模块 */
    @Schema(description = "移动端所属模块", requiredMode = Schema.RequiredMode.REQUIRED)
    private String mobileModule;

    /** 功能名 */
    @Schema(description = "功能名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "functionName不能为空")
    private String functionName;

    /** 业务名 */
    @Schema(description = "业务名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "busName不能为空")
    private String busName;

    /** 类名 */
    @Schema(description = "类名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "className不能为空")
    private String className;

    /** 表单布局 */
    @Schema(description = "表单布局", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "formLayout不能为空")
    private String formLayout;

    /** 使用栅格 */
    @Schema(description = "使用栅格", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "gridWhether不能为空")
    private String gridWhether;

    /** 排序 */
    @Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;

    /** 作者名 */
    @Schema(description = "作者名", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "authorName不能为空")
    private String authorName;

    /** 包名 */
    @Schema(description = "包名 */", requiredMode = Schema.RequiredMode.REQUIRED)
    private String packageName;

    /** 生成类型：TABLE/TREE/LEFT_TREE_TABLE/MASTER_DETAIL */
    @Schema(description = "生成类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "genType不能为空")
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
