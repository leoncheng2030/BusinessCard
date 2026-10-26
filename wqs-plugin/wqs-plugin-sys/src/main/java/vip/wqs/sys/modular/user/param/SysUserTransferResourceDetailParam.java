
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 权限转移资源明细参数
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Getter
@Setter
public class SysUserTransferResourceDetailParam {

    @Schema(description = "被调整人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "sourceUserId不能为空")
    private String sourceUserId;

    @Schema(description = "资源类型", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "resourceType不能为空")
    private String resourceType;
}
