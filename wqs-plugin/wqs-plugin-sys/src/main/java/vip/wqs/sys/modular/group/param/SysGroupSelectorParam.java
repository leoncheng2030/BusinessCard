
package vip.wqs.sys.modular.group.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 用户组选择器参数
 *
 * @author yubaoshan
 * @date 2025/1/12 02:36
 **/
@Getter
@Setter
public class SysGroupSelectorParam {

    /** 名称关键词 */
    @Schema(description = "名称关键词")
    private String searchKey;
}
