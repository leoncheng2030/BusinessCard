
package vip.wqs.sys.modular.user.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 用户密码实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName("SYS_USER_PASSWORD")
public class SysUserPassword extends CommonEntity {

    /** id */
    @TableId
    @Schema(description = "id")
    private String id;

    /** 用户id */
    @Schema(description = "用户id")
    private String userId;

    /** 密码 */
    @Schema(description = "密码")
    private String password;

}
