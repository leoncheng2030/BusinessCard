
package vip.wqs.sys.modular.org.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 用户数据范围API映射实体
 * <p>
 * 记录每个API对应的作用域KEY，多个API共享相同orgId集合时复用同一个SCOPE_KEY，
 * 大幅减少预计算表的数据量。
 * </p>
 *
 * @author yubaoshan
 * @date 2026/2/12
 **/
@Getter
@Setter
@TableName("SYS_USER_DATA_SCOPE_MAP")
public class SysUserDataScopeMap implements Serializable {

    /** 用户ID */
    @TableId
    @Schema(description = "用户ID")
    private String userId;

    /** API接口地址 */
    @Schema(description = "API接口地址")
    private String apiUrl;

    /** 作用域KEY（orgId集合的MD5摘要） */
    @Schema(description = "作用域KEY")
    private String scopeKey;
}
