
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限转移项参数
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Getter
@Setter
public class SysUserTransferItemParam {

    @Schema(description = "资源类型（SUBORDINATE/ROLE/TODO_TASK/DONE_TASK）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "resourceType不能为空")
    private String resourceType;

    @Schema(description = "是否全部转移")
    private Boolean transferAll;

    @Schema(description = "选中的资源ID列表（transferAll为false时使用）")
    private List<String> selectedIds;
}
