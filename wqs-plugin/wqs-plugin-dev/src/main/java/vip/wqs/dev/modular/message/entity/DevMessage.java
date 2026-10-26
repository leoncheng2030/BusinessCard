
package vip.wqs.dev.modular.message.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 站内信实体
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
@Getter
@Setter
@TableName("DEV_MESSAGE")
public class DevMessage extends CommonEntity {

    /** id */
    @Schema(description = "主键")
    private String id;

    /** 分类 */
    @Schema(description = "分类")
    private String category;

    /** 主题 */
    @Schema(description = "主题")
    private String subject;

    /** 正文 */
    @Schema(description = "正文")
    private String content;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    private String extJson;
}
