
package vip.wqs.pay.modular.order.param;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * 支付流水分页查询参数
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Getter
@Setter
public class PayOrderPageParam {

    /** 当前页 */
    @Schema(description = "当前页码")
    private Integer current;

    /** 每页条数 */
    @Schema(description = "每页条数")
    private Integer size;

    /** 排序字段 */
    @Schema(description = "排序字段，字段驼峰名称，如：createTime")
    private String sortField;

    /** 排序方式 */
    @Schema(description = "排序方式，升序：ASCEND；降序：DESCEND")
    private String sortOrder;

    /** 关键字（商户订单号/支付平台流水号/订单标题模糊搜索） */
    @Schema(description = "关键字（商户订单号/支付平台流水号/订单标题模糊搜索）")
    private String searchKey;

    /** 支付平台（ALIPAY-支付宝 WXPAY-微信支付） */
    @Schema(description = "支付平台（ALIPAY-支付宝 WXPAY-微信支付）")
    private String payPlatform;

    /** 简化状态（UNPAID-未支付 PAID-已支付 CLOSED-已关闭 REFUNDED-已发生退款） */
    @Schema(description = "简化状态（UNPAID-未支付 PAID-已支付 CLOSED-已关闭 REFUNDED-已发生退款）")
    private String status;
}
