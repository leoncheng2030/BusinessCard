
package vip.wqs.dev.modular.email.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 邮件Id参数
 *
 * @author jetox
 * @date 2022/6/21 15:38
 **/
@Getter
@Setter
public class DevEmailIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
