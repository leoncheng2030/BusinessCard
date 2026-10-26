
package vip.wqs.sys.modular.org.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 组织复制参数
 *
 * @author jetox
 * @date 2022/8/15 14:32
 **/
@Getter
@Setter
public class SysOrgCopyParam {

    /** 目标父id */
    @Schema(description = "目标父id")
    @NotBlank(message = "targetParentId不能为空")
    private String targetParentId;

    /** 组织id集合 */
    @Schema(description = "组织id集合")
    @NotEmpty(message = "ids不能为空")
    private List<String> ids;
}
