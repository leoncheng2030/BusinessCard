
package vip.wqs.pay.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 退款参数
 * <p>金额单位统一为元，字符串类型。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
public class PayRefundParam {

    /** 商户订单号 */
    @Schema(description = "商户订单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "outTradeNo不能为空")
    private String outTradeNo;

    /** 退款金额（元，字符串） */
    @Schema(description = "退款金额（元，字符串）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "refundAmount不能为空")
    private String refundAmount;
}
