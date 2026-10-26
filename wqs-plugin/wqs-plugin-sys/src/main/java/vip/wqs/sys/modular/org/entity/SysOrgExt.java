
package vip.wqs.sys.modular.org.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 组织扩展实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName("SYS_ORG_EXT")
public class SysOrgExt extends CommonEntity {

    /** id */
    @TableId
    @Schema(description = "id")
    private String id;

    /** 组织id */
    @Schema(description = "组织id")
    private String orgId;

    /** 来源类别 */
    @Schema(description = "来源类别")
    private String sourceFromType;

    /** 身份源ID */
    @Schema(description = "身份源ID")
    private String idSourceId;

    /** 身份源组织ID */
    @Schema(description = "身份源机构ID")
    private String idSourceOrgId;

}
