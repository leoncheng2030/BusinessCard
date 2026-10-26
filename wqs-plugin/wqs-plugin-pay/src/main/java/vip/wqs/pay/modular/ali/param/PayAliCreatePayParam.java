
package vip.wqs.pay.modular.ali.param;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付宝支付下单参数
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
public class PayAliCreatePayParam {

    /** pay_order 主键（业务方调 PayApi.createOrder 返回的 id） */
    @Schema(description = "支付订单主键（PayApi.createOrder 返回的 id）", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "id不能为空")
    private String id;

    /** 付款码（付款码支付必填） */
    @Schema(description = "付款码（付款码支付必填）")
    private String authCode;
}
