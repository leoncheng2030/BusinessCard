
package vip.wqs.auth.modular.third.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 第三方登录回调参数
 *
 * @author jetox
 * @date 2022/7/8 20:38
 */
@Getter
@Setter
public class AuthThirdCallbackParam {

    /** 第三方平台标识 */
    @Schema(description = "第三方平台标识", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "platform不能为空")
    private String platform;

    /** 客户端类型（允许回调时直接传入） */
    @Schema(description = "客户端类型")
    private String clientType;
}
