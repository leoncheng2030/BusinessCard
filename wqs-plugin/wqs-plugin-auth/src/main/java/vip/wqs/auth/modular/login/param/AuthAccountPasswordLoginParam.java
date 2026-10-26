
package vip.wqs.auth.modular.login.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 账号密码登录参数
 *
 * @author jetox
 * @date 2022/7/7 16:46
 **/
@Getter
@Setter
public class AuthAccountPasswordLoginParam {

    /** 账号 */
    @Schema(description = "账号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "account不能为空")
    private String account;

    /** 密码 */
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "password不能为空")
    private String password;

    /** 设备 */
    @Schema(description = "设备")
    private String device;

    /** 验证码 */
    @Schema(description = "验证码")
    private String validCode;

    /** 验证码请求号 */
    @Schema(description = "验证码请求号")
    private String validCodeReqNo;
}
