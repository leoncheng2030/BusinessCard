
package vip.wqs.pay.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 支付订单创建返回
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PayCreateResult {

    /** pay_order 主键，给后续 controller 调起微信/支付宝下单接口用 */
    @Schema(description = "支付订单主键")
    private String id;

    /** 最终生效的商户订单号 */
    @Schema(description = "商户订单号")
    private String outTradeNo;
}
