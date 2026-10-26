
package vip.wqs.sys.modular.resource.param.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单Id参数
 *
 * @author jetox
 * @date 2022/7/27 18:40
 **/
@Getter
@Setter
public class SysMenuIdParam {

    /** id */
    @Schema(description = "id")
    @NotBlank(message = "id不能为空")
    private String id;
}
