
package vip.wqs.common.prop;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 通用基础配置
 *
 * @author jetox
 * @date 2022/1/2 17:03
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "wqs.config.common")
public class CommonProperties {

    /**
     * 前端地址
     */
    private String frontUrl;

    /**
     * 后端地址
     */
    private String backendUrl;

    /**
     * 后端路由
     */
    private List<Map<String, String>> backendPaths;

    /**
     * 文件下载签名密钥（wqs.config.common.file-download-sign-secret）：
     * 公开文件直链下载（/dev/file/download）的 HMAC 签名密钥，缺省回落历史默认值兼容存量签名链接
     */
    private String fileDownloadSignSecret;

}
