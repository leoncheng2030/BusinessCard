
package vip.wqs.sys.modular.city.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 系统城市实体（sys_system_city，全国省市区基础数据）
 *
 * @author wqs
 * @date 2026/9/3
 **/
@Getter
@Setter
@TableName("SYS_SYSTEM_CITY")
public class SysCity extends CommonEntity {

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 城市编码 */
    @Schema(description = "城市编码（同一层级内自增编号，父子关系经parentId关联）")
    private Integer cityId;

    /** 城市名称 */
    @Schema(description = "城市名称")
    private String name;

    /** 父级ID */
    @Schema(description = "父级ID（0为省级，指向父级cityId）")
    private Integer parentId;

    /** 层级（0省 1市 2区县） */
    @Schema(description = "层级（0省 1市 2区县）")
    private Integer level;

    /** 区号 */
    @Schema(description = "区号")
    private String areaCode;

    /** 合并名称 */
    @Schema(description = "合并名称（省市区全称拼接）")
    private String mergerName;

    /** 经度 */
    @Schema(description = "经度")
    private String lng;

    /** 纬度 */
    @Schema(description = "纬度")
    private String lat;

    /** 是否展示（0-不展示 1-展示） */
    @Schema(description = "是否展示（0-不展示 1-展示）")
    private Integer isShow;
}
