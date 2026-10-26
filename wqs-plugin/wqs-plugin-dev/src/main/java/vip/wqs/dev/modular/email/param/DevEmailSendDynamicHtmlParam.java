
package vip.wqs.dev.modular.email.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 邮件发送——本地HTML参数
 *
 * @author jetox
 * @date 2022/6/21 15:38
 **/
@Getter
@Setter
public class DevEmailSendDynamicHtmlParam {

    /** 接收人 */
    @Schema(description = "接收人邮箱地址，多个逗号拼接")
    @NotBlank(message = "receiveAccounts不能为空")
    private String receiveAccounts;

    /** 邮件主题 */
    @Schema(description = "邮件主题")
    @NotBlank(message = "subject不能为空")
    private String subject;

    /** 邮件正文 */
    @Schema(description = "邮件正文")
    @NotBlank(message = "content不能为空")
    private String content;
}
