
package vip.wqs.auth.modular.login.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取邮箱验证码参数
 *
 * @author jetox
 * @date 2022/8/25 13:45
 **/
@Getter
@Setter
public class AuthGetEmailValidCodeParam {

    /** 邮箱 */
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "email不能为空")
    private String email;

    /** 验证码 */
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "validCode不能为空")
    private String validCode;

    /** 验证码请求号 */
    @Schema(description = "验证码请求号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "validCodeReqNo不能为空")
    private String validCodeReqNo;
}
