
package vip.wqs.sys.modular.user.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 编辑个人工作台参数
 *
 * @author jetox
 * @date 2022/7/27 17:08
 **/
@Getter
@Setter
public class SysUserUpdateWorkbenchParam {

    /** 工作台数据 */
    @Schema(description = "工作台数据")
    @NotBlank(message = "workbenchData不能为空")
    private String workbenchData;
}
