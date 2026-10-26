
package vip.wqs.pay.modular.wx.service;

import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;

import java.util.Map;

/**
 * 微信支付Service接口
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public interface PayWxService {

    /**
     * 微信扫码支付（Native）下单
     *
     * @param payOrder 支付流水
     * @return 二维码链接（codeUrl）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    String createQrPay(PayOrder payOrder);

    /**
     * 微信H5支付下单
     *
     * @param payOrder 支付流水
     * @param clientIp 终端IP
     * @return H5支付跳转链接（h5Url）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    String createH5Pay(PayOrder payOrder, String clientIp);

    /**
     * 微信JSAPI（公众号内）支付下单
     *
     * @param payOrder 支付流水
     * @param openid   用户openid
     * @return 前端调起支付所需参数（appId/timeStamp/nonceStr/packageValue/signType/paySign）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    Map<String, String> createJsPay(PayOrder payOrder, String openid);

    /**
     * 微信付款码支付（同步，支付成功时同步发布支付成功事件）
     *
     * @param payOrder 支付流水
     * @param authCode 付款码
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void createCodePay(PayOrder payOrder, String authCode);

    /**
     * 查询微信渠道订单状态并同步本地流水
     *
     * @param payOrder 支付流水
     * @return 最新简化状态
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayStatusEnum queryChannelStatus(PayOrder payOrder);

    /**
     * 处理微信支付回调（事件发布点）
     * <p>微信回调到达后解析验签，同步本地流水为已支付并发布 PaySuccessEvent。</p>
     *
     * @param notifyData      回调报文
     * @param signatureHeader 回调请求头签名
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateOrderInfo(String notifyData, SignatureHeader signatureHeader);

    /**
     * 微信渠道申请退款（异步，退款结果经退款回调通知）
     *
     * @param payOrder       支付流水
     * @param payOrderRefund 退款流水
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void refund(PayOrder payOrder, PayOrderRefund payOrderRefund);

    /**
     * 处理微信退款回调（异步退款结果到达时调用）
     *
     * @param notifyData      回调报文
     * @param signatureHeader 回调请求头签名
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateRefundInfo(String notifyData, SignatureHeader signatureHeader);
}
