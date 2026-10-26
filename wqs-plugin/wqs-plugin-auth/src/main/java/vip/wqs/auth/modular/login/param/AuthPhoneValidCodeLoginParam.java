
package vip.wqs.auth.modular.login.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 手机验证码登录参数
 *
 * @author jetox
 * @date 2022/7/7 16:46
 **/
@Getter
@Setter
public class AuthPhoneValidCodeLoginParam {

    /** 手机号 */
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "phone不能为空")
    private String phone;

    /** 验证码 */
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "validCode不能为空")
    private String validCode;

    /** 验证码请求号 */
    @Schema(description = "验证码请求号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "validCodeReqNo不能为空")
    private String validCodeReqNo;

    /** 设备 */
    @Schema(description = "设备")
    private String device;
}
