
package vip.wqs.auth.modular.login.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 第三方系统Token交换配置属性（用于iframe嵌入免登）
 *
 * 对接规范：甲方提供一个用户信息接口，GET请求，Header传 Authorization: Bearer {accessToken}
 * 必须返回如下JSON格式：
 * {
 *     "code": 200,
 *     "data": {
 *         "account": "zhangsan",
 *         "email": "zhangsan@xxx.com",
 *         "phone": "13800138000"
 *     }
 * }
 *
 * @author yubaoshan
 * @date 2026/2/11
 **/
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "wqs.third-auth")
public class AuthThirdClientProperties {

    /** 第三方用户信息接口地址（甲方提供），配置了即代表开启，不配置即关闭 */
    private String userInfoUrl;
}
