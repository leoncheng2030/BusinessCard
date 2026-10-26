
package vip.wqs.auth.modular.sso.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 获取认证中心地址参数
 *
 * @author jetox
 * @date 2022/7/7 16:46
 **/
@Getter
@Setter
public class AuthGetSsoAuthUrlParam {

    /** 跳转地址 */
    @Schema(description = "跳转地址", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "redirectUrl不能为空")
    private String redirectUrl;
}
