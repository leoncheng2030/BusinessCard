
package vip.wqs.pay.modular.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款流水表（PAY_ORDER_REFUND）
 * <p>每笔支付流水的退款记录，与业务方通过 outTradeNo / refundNo 关联。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
@TableName("PAY_ORDER_REFUND")
public class PayOrderRefund extends CommonEntity {

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 支付订单ID（PAY_ORDER.ID） */
    @Schema(description = "支付订单ID（PAY_ORDER.ID）")
    private String payOrderId;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /** 退款单号（本插件生成） */
    @Schema(description = "退款单号（本插件生成）")
    private String refundNo;

    /** 渠道退款单号 */
    @Schema(description = "渠道退款单号")
    private String channelRefundNo;

    /** 退款金额（元） */
    @Schema(description = "退款金额（元）")
    private BigDecimal refundAmount;

    /** 退款状态（REFUNDING-退款中 SUCCESS-退款成功 FAIL-退款失败） */
    @Schema(description = "退款状态（REFUNDING-退款中 SUCCESS-退款成功 FAIL-退款失败）")
    private String status;

    /** 退款完成时间 */
    @Schema(description = "退款完成时间")
    private Date refundTime;
}
