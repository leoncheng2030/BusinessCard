
package vip.wqs.auth.core.protocol.base;

import lombok.Getter;
import lombok.Setter;

/**
 * 认证源基础配置
 *
 * @author jetox
 * @date 2024/5/31 10:06
 **/
@Getter
@Setter
public class AuthBaseJson {

    /** 关联源属性 */
    private String sourceProperty;

    /** 关联目标属性 */
    private String targetProperty;
}
