
package vip.wqs.sys.modular.org.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户数据范围预计算实体
 * <p>
 * 通过SCOPE_KEY将相同orgId集合的API分组，避免重复存储，大幅减少数据量。
 * </p>
 *
 * @author yubaoshan
 * @date 2026/2/12
 **/
@Getter
@Setter
@TableName("SYS_USER_DATA_SCOPE")
public class SysUserDataScope implements Serializable {

    /** 用户ID */
    @TableId
    @Schema(description = "用户ID")
    private String userId;

    /** 作用域KEY（orgId集合的MD5摘要） */
    @Schema(description = "作用域KEY")
    private String scopeKey;

    /** 机构ID */
    @Schema(description = "机构ID")
    private String orgId;
}
