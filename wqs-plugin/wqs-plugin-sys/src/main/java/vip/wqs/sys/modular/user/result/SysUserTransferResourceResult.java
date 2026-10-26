
package vip.wqs.sys.modular.user.result;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 权限转移资源结果
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Getter
@Setter
public class SysUserTransferResourceResult {

    @Schema(description = "资源类型")
    private String resourceType;

    @Schema(description = "资源类型名称")
    private String resourceTypeName;

    @Schema(description = "总数量")
    private Long totalCount;

    @Schema(description = "明细列定义")
    private List<JSONObject> detailColumns;
}
