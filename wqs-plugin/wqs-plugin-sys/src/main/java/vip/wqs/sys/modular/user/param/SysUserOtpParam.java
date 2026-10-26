
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 绑定/解绑动态口令参数
 *
 * @author jetox
 * @date 2022/7/26 16:04
 **/
@Getter
@Setter
public class SysUserOtpParam {

    /** 动态口令 */
    @Schema(description = "动态口令")
    @NotBlank(message = "otpCode不能为空")
    private String otpCode;
}
