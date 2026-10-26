
package vip.wqs.sys.modular.resource.param.menu;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 菜单树查询参数
 *
 * @author jetox
 * @date 2022/7/27 18:40
 **/
@Getter
@Setter
public class SysMenuTreeParam {

    /** 模块 */
    @Schema(description = "模块")
    private String module;

    /** 名称关键词 */
    @Schema(description = "名称关键词")
    private String searchKey;

    /** SAAS过滤（YES：只返回可授权使用的菜单，SAAS_FLAG=NO 的系统菜单不返回，套餐授权树使用） */
    @Schema(description = "SAAS过滤")
    private String saasFilter;
}
