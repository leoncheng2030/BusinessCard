
package vip.wqs.sys.modular.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.util.Date;

/**
 * 用户扩展实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName("SYS_USER_EXT")
public class SysUserExt extends CommonEntity {

    /** id */
    @TableId
    @Schema(description = "id")
    private String id;

    /** 用户id */
    @Schema(description = "用户id")
    private String userId;

    /** 来源类别 */
    @Schema(description = "来源类别")
    private String sourceFromType;

    /** 密码修改日期 */
    @Schema(description = "密码修改日期")
    private Date passwordUpdateTime;

    /** 身份源ID */
    @Schema(description = "身份源ID")
    private String idSourceId;

    /** 身份源用户ID */
    @Schema(description = "身份源用户ID")
    private String idSourceUserId;

    /** OTP密钥 */
    @Schema(description = "OTP密钥")
    private String otpSecretKey;

    /** OTP绑定状态 */
    @Schema(description = "OTP绑定状态")
    private String hasBindOtp;

}
