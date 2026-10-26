
package vip.wqs.pay.modular.order.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import vip.wqs.common.pojo.CommonEntity;

/**
 * 支付交互明细表（PAY_ORDER_DETAILS）
 * <p>记录支付流水与渠道的每次交互（创建/下单/查询/回调/退款），
 * 渠道原生状态与报文快照均留存于此，用于对账与问题排查。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
@TableName("PAY_ORDER_DETAILS")
public class PayOrderDetails extends CommonEntity {

    /** 主键 */
    @Schema(description = "主键")
    private String id;

    /** 支付订单ID（PAY_ORDER.ID） */
    @Schema(description = "支付订单ID（PAY_ORDER.ID）")
    private String payOrderId;

    /** 商户订单号 */
    @Schema(description = "商户订单号")
    private String outTradeNo;

    /** 交互类型（CREATE-创建订单 ORDER-渠道下单 QUERY-渠道查询 NOTIFY-渠道回调 REFUND-渠道退款） */
    @Schema(description = "交互类型（CREATE-创建订单 ORDER-渠道下单 QUERY-渠道查询 NOTIFY-渠道回调 REFUND-渠道退款）")
    private String detailsType;

    /** 渠道返回状态 */
    @Schema(description = "渠道返回状态")
    private String channelStatus;

    /** 渠道返回信息 */
    @Schema(description = "渠道返回信息")
    private String channelMessage;

    /** 渠道交互报文快照 */
    @Schema(description = "渠道交互报文快照")
    private String channelParams;
}
