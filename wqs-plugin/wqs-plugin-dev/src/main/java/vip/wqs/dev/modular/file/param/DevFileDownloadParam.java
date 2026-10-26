
package vip.wqs.dev.modular.file.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 文件下载参数
 *
 * @author jetox
 * @date 2025/6/26 10:24
 */
@Getter
@Setter
public class DevFileDownloadParam {

    /** id */
    @Schema(description = "文件id")
    @NotBlank(message = "id不能为空")
    private String id;

    /** 下载签名 */
    @Schema(description = "下载签名")
    private String sign;
}
