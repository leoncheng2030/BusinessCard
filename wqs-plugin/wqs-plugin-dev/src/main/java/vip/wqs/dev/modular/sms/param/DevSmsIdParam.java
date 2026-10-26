
package vip.wqs.dev.modular.sms.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 短信Id参数
 *
 * @author jetox
 * @date 2022/7/31 15:25
 */
@Getter
@Setter
public class DevSmsIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
