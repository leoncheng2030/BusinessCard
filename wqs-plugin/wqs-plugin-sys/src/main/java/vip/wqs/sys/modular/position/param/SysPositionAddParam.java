
package vip.wqs.sys.modular.position.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * 职位添加参数
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
public class SysPositionAddParam {

    /** 组织id */
    @Schema(description = "组织id")
    @NotBlank(message = "orgId不能为空")
    private String orgId;

    /** 名称 */
    @Schema(description = "名称")
    @NotBlank(message = "name不能为空")
    private String name;

    /** 编码 */
    @Schema(description = "编码")
    @NotBlank(message = "code不能为空")
    private String code;

    /** 分类 */
    @Schema(description = "分类")
    @NotBlank(message = "category不能为空")
    private String category;

    /** 排序码 */
    @Schema(description = "排序码")
    @NotNull(message = "sortCode不能为空")
    private Integer sortCode;

    /** 扩展JSON */
    @Schema(description = "扩展JSON")
    private String extJson;
}
