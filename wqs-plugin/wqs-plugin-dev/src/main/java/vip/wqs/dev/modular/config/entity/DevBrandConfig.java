
package vip.wqs.dev.modular.config.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 平台品牌配置实体（全局共享）
 *
 * <p>承载 logo/系统名称/版本/描述/版权等平台统一品牌信息，对应表 DEV_BRAND_CONFIG；
 * 与 DEV_CONFIG 解耦：品牌数据全局唯一，登录前（免登公开读接口）即可获取。</p>
 *
 * @author wqs
 * @date 2026/9/4 10:00
 **/
@Getter
@Setter
@TableName("DEV_BRAND_CONFIG")
public class DevBrandConfig extends CommonEntity {

    /** id */
    @Schema(description = "id")
    private String id;

    /** 配置键 */
    @Schema(description = "配置键")
    private String configKey;

    /** 配置值 */
    @Schema(description = "配置值")
    private String configValue;

    /** 备注 */
    @Schema(description = "备注")
    private String remark;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;
}
