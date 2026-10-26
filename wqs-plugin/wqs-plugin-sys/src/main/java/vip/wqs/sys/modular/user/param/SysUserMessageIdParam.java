
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 站内信Id参数
 *
 * @author jetox
 * @date 2022/9/2 11:06
 */
@Getter
@Setter
public class SysUserMessageIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
