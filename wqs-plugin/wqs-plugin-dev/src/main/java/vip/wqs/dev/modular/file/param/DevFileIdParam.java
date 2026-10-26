
package vip.wqs.dev.modular.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件Id参数
 *
 * @author jetox
 * @date 2022/7/31 10:24
 */
@Getter
@Setter
public class DevFileIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
