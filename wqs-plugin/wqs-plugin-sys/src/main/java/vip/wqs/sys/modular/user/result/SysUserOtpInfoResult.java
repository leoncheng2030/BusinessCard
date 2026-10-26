
package vip.wqs.sys.modular.user.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 动口令信息结果
 *
 * @author jetox
 * @date 2022/7/8 9:28
 **/
@Getter
@Setter
@Builder
public class SysUserOtpInfoResult {

    /** 动态口令信息，Base64 */
    @Schema(description = "动态口令信息，Base64")
    private String otpInfoBase64;

    /** 动态口令信息，JSON */
    @Schema(description = "动态口令信息，JSON")
    private OtpInfo otpInfo;

    /**
     * 动态口令信息类
     *
     * @author jetox
     * @date 2022/4/28 23:19
     */
    @Getter
    @Setter
    @Builder
    public static class OtpInfo {

        /** 发行者 */
        @Schema(description = "发行者")
        private String issuer;

        /** 账号 */
        @Schema(description = "账号")
        private String account;

        /** 密钥 */
        @Schema(description = "密钥")
        private String secretKey;

        /** 算法 */
        @Schema(description = "算法")
        private String algorithm;

        /** 位数 */
        @Schema(description = "位数")
        private String digits;

        /** 周期 */
        @Schema(description = "周期")
        private String period;
    }
}
