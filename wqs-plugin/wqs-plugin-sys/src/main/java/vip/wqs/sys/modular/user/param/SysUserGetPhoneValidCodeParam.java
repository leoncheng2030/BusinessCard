
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取手机验证码参数
 *
 * @author jetox
 * @date 2022/8/25 13:45
 **/
@Getter
@Setter
public class SysUserGetPhoneValidCodeParam {

    /** 手机号 */
    @Schema(description = "手机号")
    @NotBlank(message = "phone不能为空")
    private String phone;

    /** 验证码 */
    @Schema(description = "验证码")
    @NotBlank(message = "validCode不能为空")
    private String validCode;

    /** 验证码请求号 */
    @Schema(description = "验证码请求号")
    @NotBlank(message = "validCodeReqNo不能为空")
    private String validCodeReqNo;
}
