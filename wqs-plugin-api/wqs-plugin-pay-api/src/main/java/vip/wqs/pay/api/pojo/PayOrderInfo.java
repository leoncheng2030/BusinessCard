
package vip.wqs.pay.api.pojo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付订单只读视图
 * <p>业务方通过 PayApi.getOrderInfo 获取，仅含必要字段，不暴露内部实现细节。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
public class PayOrderInfo {

    /** pay_order 主键 */
    @Schema(description = "支付订单主键")
    private String id;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /** 支付平台流水号 */
    @Schema(description = "支付平台流水号")
    private String tradeNo;

    /** 订单标题 */
    @Schema(description = "订单标题")
    private String subject;

    /** 订单金额（元） */
    @Schema(description = "订单金额（元）")
    private BigDecimal orderAmount;

    /** 实际支付金额（元） */
    @Schema(description = "实际支付金额（元）")
    private BigDecimal payAmount;

    /** 支付平台：ALIPAY / WXPAY */
    @Schema(description = "支付平台：ALIPAY / WXPAY")
    private String payPlatform;

    /** 简化状态：UNPAID / PAID / CLOSED / REFUNDED */
    @Schema(description = "简化状态：UNPAID / PAID / CLOSED / REFUNDED")
    private String status;

    /** 支付完成时间 */
    @Schema(description = "支付完成时间")
    private Date payTime;

    /** 创建时间 */
    @Schema(description = "创建时间")
    private Date createTime;
}
