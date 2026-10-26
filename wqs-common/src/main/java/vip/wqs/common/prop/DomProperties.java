
package vip.wqs.common.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 数据建模相关配置
 *
 * @author jetox
 * @date 2022/1/2 17:03
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "wqs.config.dom")
public class DomProperties {

    /**
     * 数据建模，授权数据库表前缀，逗号分割
     */
    private String authTableNamePrefixes;

}
