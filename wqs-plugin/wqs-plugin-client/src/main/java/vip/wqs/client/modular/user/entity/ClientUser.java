
package vip.wqs.client.modular.user.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.dromara.core.trans.anno.Trans;
import org.dromara.core.trans.constant.TransType;
import vip.wqs.common.handler.CommonSm4CbcTypeHandler;
import vip.wqs.common.pojo.CommonEntity;

/**
 * C端用户实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName(value = "CLIENT_USER", autoResultMap = true)
public class ClientUser extends CommonEntity {

    /** id */
    @Schema(description = "id")
    private String id;

    /** 头像 */
    @Schema(description = "头像，图片base64")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String avatar;

    /** 账号 */
    @Schema(description = "账号")
    private String account;

    /** 密码 */
    @JsonIgnore
    @Schema(description = "密码")
    private String password;

    /** 姓名 */
    @Schema(description = "姓名")
    private String name;

    /** 昵称 */
    @Schema(description = "昵称")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String nickname;

    /** 性别 */
    @Schema(description = "性别")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    @Trans(type = TransType.DICTIONARY, key = "GENDER")
    private String gender;

    /** 年龄 */
    @Schema(description = "年龄")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String age;

    /** 出生日期 */
    @Schema(description = "出生日期")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String birthday;

    /** 手机 */
    @Schema(description = "手机")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS, typeHandler = CommonSm4CbcTypeHandler.class)
    private String phone;

    /** 邮箱 */
    @Schema(description = "邮箱")
    @TableField(insertStrategy = FieldStrategy.ALWAYS, updateStrategy = FieldStrategy.ALWAYS)
    private String email;

    /** 用户状态 */
    @Schema(description = "用户状态")
    private String userStatus;

    /** 排序码 */
    @Schema(description = "排序码")
    private Integer sortCode;

}
