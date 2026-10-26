
package vip.wqs.dev.modular.password.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 弱密码库Id参数
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
public class DevWeakPasswordIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
