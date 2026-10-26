
package vip.wqs.dev.modular.dict.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 字典实体
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
@Getter
@Setter
@TableName("DEV_DICT")
public class DevDict extends CommonEntity {

    /** id */
    @Schema(description = "id")
    private String id;

    /** 父id */
    @Schema(description = "父id")
    private String parentId;

    /** 字典文字 */
    @Schema(description = "字典文字")
    private String dictLabel;

    /** 字典值 */
    @Schema(description = "字典值")
    private String dictValue;

    /** 字典颜色 */
    @Schema(description = "字典颜色")
    private String dictColor;

    /** 编码 */
    @Schema(description = "编码")
    private String code;

    /** 分类 */
    @Schema(description = "分类")
    private String category;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

    /** 扩展信息 */
    @Schema(description = "扩展信息")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String extJson;
}
