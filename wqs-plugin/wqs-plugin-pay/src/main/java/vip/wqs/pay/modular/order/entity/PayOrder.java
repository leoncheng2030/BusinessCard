
package vip.wqs.pay.modular.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水主表（PAY_ORDER）
 * <p>本表是 pay 插件内部的支付流水表，不是业务订单表。业务方拥有自己的业务订单表时，
 * 用 OUT_TRADE_NO（商户订单号）作为关联键，不直接读写本表，所有操作通过 PayApi。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
@TableName("PAY_ORDER")
public class PayOrder extends CommonEntity {

    /** 主键 */
    @Schema(description = "主键")
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

    /** 订单描述 */
    @Schema(description = "订单描述")
    private String body;

    /** 订单金额（元） */
    @Schema(description = "订单金额（元）")
    private BigDecimal orderAmount;

    /** 实际支付金额（元） */
    @Schema(description = "实际支付金额（元）")
    private BigDecimal payAmount;

    /** 支付平台（ALIPAY-支付宝 WXPAY-微信支付） */
    @Schema(description = "支付平台（ALIPAY-支付宝 WXPAY-微信支付）")
    private String payPlatform;

    /** 简化状态（UNPAID-未支付 PAID-已支付 CLOSED-已关闭 REFUNDED-已发生退款） */
    @Schema(description = "简化状态（UNPAID-未支付 PAID-已支付 CLOSED-已关闭 REFUNDED-已发生退款）")
    private String status;

    /** 支付完成时间 */
    @Schema(description = "支付完成时间")
    private Date payTime;
}
