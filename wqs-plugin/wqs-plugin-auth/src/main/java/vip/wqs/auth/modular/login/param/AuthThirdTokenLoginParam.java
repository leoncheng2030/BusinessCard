
package vip.wqs.auth.modular.login.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 第三方Token交换登录参数（用于iframe嵌入免登）
 *
 * @author yubaoshan
 * @date 2026/2/11
 **/
@Getter
@Setter
public class AuthThirdTokenLoginParam {

    /** 第三方系统的accessToken */
    @Schema(description = "第三方系统的accessToken", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "accessToken不能为空")
    private String accessToken;
}
