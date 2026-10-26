
package vip.wqs.pay.modular.ali.service;

import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;

import java.util.Map;

/**
 * 支付宝支付Service接口
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public interface PayAliService {

    /**
     * 支付宝PC网页支付下单
     *
     * @param payOrder 支付流水
     * @return PC支付页面表单（前端渲染后跳转支付宝收银台）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    String createPcPay(PayOrder payOrder);

    /**
     * 支付宝H5/手机网页支付下单
     *
     * @param payOrder 支付流水
     * @return H5支付页面表单（前端渲染后跳转支付宝收银台）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    String createWapPay(PayOrder payOrder);

    /**
     * 支付宝扫码支付（预下单）下单
     *
     * @param payOrder 支付流水
     * @return 二维码链接
     * @author wqs
     * @date 2026/9/3 14:30
     */
    String createQrPay(PayOrder payOrder);

    /**
     * 支付宝付款码支付（同步，支付成功时同步发布支付成功事件）
     *
     * @param payOrder 支付流水
     * @param authCode 付款码
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void createCodePay(PayOrder payOrder, String authCode);

    /**
     * 查询支付宝渠道订单状态并同步本地流水
     *
     * @param payOrder 支付流水
     * @return 最新简化状态
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayStatusEnum queryChannelStatus(PayOrder payOrder);

    /**
     * 处理支付宝支付回调（事件发布点）
     * <p>支付宝回调到达后验签，同步本地流水为已支付并发布 PaySuccessEvent。</p>
     *
     * @param params 回调参数
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateOrderInfo(Map<String, String> params);

    /**
     * 支付宝渠道申请退款（同步返回结果）
     *
     * @param payOrder       支付流水
     * @param payOrderRefund 退款流水
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void refund(PayOrder payOrder, PayOrderRefund payOrderRefund);
}
