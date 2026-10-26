
package vip.wqs.dev.modular.config.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 配置Id参数
 *
 * @author jetox
 * @date 2022/7/30 17:52
 */
@Getter
@Setter
public class DevConfigIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
