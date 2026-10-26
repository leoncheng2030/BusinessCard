
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限转移执行参数
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Getter
@Setter
public class SysUserTransferExecuteParam {

    @Schema(description = "被调整人ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "sourceUserId不能为空")
    private String sourceUserId;

    @Schema(description = "目标人ID（删除模式时可为空）")
    private String targetUserId;

    @Schema(description = "转移模式（TRANSFER/COPY/DELETE）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "transferMode不能为空")
    private String transferMode;

    @Schema(description = "转移内容列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "transferItems不能为空")
    private List<SysUserTransferItemParam> transferItems;
}
