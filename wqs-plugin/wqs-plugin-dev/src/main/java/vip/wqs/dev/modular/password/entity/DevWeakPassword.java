
package vip.wqs.dev.modular.password.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 弱密码库实体
 *
 * @author jetox
 * @date 2022/4/21 16:13
 **/
@Getter
@Setter
@TableName("DEV_WEAK_PASSWORD")
public class DevWeakPassword extends CommonEntity {

    /** id */
    @TableId
    @Schema(description = "id")
    private String id;

    /** 密码 */
    @Schema(description = "密码")
    private String password;

}
