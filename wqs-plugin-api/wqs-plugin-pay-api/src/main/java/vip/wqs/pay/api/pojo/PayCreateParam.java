
package vip.wqs.pay.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付订单创建参数
 * <p>金额单位统一为元，字符串类型，如 "0.01"、"29.90"。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
public class PayCreateParam {

    /** 商户订单号（可选，不传由 pay 插件自动生成） */
    @Schema(description = "商户订单号（可选，不传由 pay 插件自动生成）")
    private String outTradeNo;

    /** 订单标题 */
    @Schema(description = "订单标题", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "subject不能为空")
    private String subject;

    /** 订单描述 */
    @Schema(description = "订单描述")
    private String body;

    /** 订单金额（元，字符串） */
    @Schema(description = "订单金额（元，字符串）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "orderAmount不能为空")
    private String orderAmount;

    /** 支付平台：ALIPAY / WXPAY */
    @Schema(description = "支付平台：ALIPAY / WXPAY", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "payPlatform不能为空")
    private String payPlatform;
}
