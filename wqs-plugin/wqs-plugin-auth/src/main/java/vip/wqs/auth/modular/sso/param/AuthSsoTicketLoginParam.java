
package vip.wqs.auth.modular.sso.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * ticket单点登录登录参数
 *
 * @author jetox
 * @date 2022/7/7 16:46
 **/
@Getter
@Setter
public class AuthSsoTicketLoginParam {

    /** ticket */
    @Schema(description = "ticket", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "ticket不能为空")
    private String ticket;

    /** 设备 */
    @Schema(description = "设备")
    private String device;
}
