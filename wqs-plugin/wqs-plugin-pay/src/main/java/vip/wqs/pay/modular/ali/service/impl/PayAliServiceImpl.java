
package vip.wqs.pay.modular.ali.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradePayRequest;
import com.alipay.api.request.AlipayTradePrecreateRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradePayResponse;
import com.alipay.api.response.AlipayTradePrecreateResponse;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.event.PaySuccessEvent;
import vip.wqs.pay.core.config.PayConfigure;
import vip.wqs.pay.modular.ali.service.PayAliService;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;
import vip.wqs.pay.modular.order.enums.PayOrderDetailsTypeEnum;
import vip.wqs.pay.modular.order.service.PayOrderDetailsService;
import vip.wqs.pay.modular.order.service.PayOrderService;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * 支付宝支付Service接口实现类
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Service
public class PayAliServiceImpl implements PayAliService {

    /** 支付宝交易状态：交易创建，等待买家付款 */
    private static final String ALI_TRADE_STATUS_WAIT_BUYER_PAY = "WAIT_BUYER_PAY";

    /** 支付宝交易状态：交易支付成功 */
    private static final String ALI_TRADE_STATUS_SUCCESS = "TRADE_SUCCESS";

    /** 支付宝交易状态：交易结束（不可退款） */
    private static final String ALI_TRADE_STATUS_FINISHED = "TRADE_FINISHED";

    /** 支付宝交易状态：交易关闭 */
    private static final String ALI_TRADE_STATUS_CLOSED = "TRADE_CLOSED";

    /** 支付宝签名类型：RSA2 */
    private static final String ALI_SIGN_TYPE = "RSA2";

    /** 支付宝回调验签字符集 */
    private static final String ALI_CHARSET = "UTF-8";

    @Resource
    private PayConfigure payConfigure;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayOrderDetailsService payOrderDetailsService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String createPcPay(PayOrder payOrder) {
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();
        request.setNotifyUrl(payConfigure.getAlipayNotifyUrl());
        request.setBizContent(buildTradeBizContent(payOrder, "FAST_INSTANT_TRADE_PAY").toString());
        try {
            // 返回PC支付页面表单，前端渲染后跳转支付宝收银台
            String form = payConfigure.getAlipayClient().pageExecute(request).getBody();
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "SUCCESS",
                    "支付宝PC网页支付下单成功", form);
            return form;
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "支付宝PC网页支付下单失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝PC网页支付下单失败：" + e.getErrMsg());
        }
    }

    @Override
    public String createWapPay(PayOrder payOrder) {
        AlipayTradeWapPayRequest request = new AlipayTradeWapPayRequest();
        request.setNotifyUrl(payConfigure.getAlipayNotifyUrl());
        request.setBizContent(buildTradeBizContent(payOrder, "QUICK_WAP_WAY").toString());
        try {
            // 返回H5支付页面表单，前端渲染后跳转支付宝收银台
            String form = payConfigure.getAlipayClient().pageExecute(request).getBody();
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "SUCCESS",
                    "支付宝H5支付下单成功", form);
            return form;
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "支付宝H5支付下单失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝H5支付下单失败：" + e.getErrMsg());
        }
    }

    @Override
    public String createQrPay(PayOrder payOrder) {
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();
        request.setNotifyUrl(payConfigure.getAlipayNotifyUrl());
        request.setBizContent(buildTradeBizContent(payOrder, null).toString());
        try {
            AlipayTradePrecreateResponse response = payConfigure.getAlipayClient().execute(request);
            if(response.isSuccess() && StrUtil.isNotBlank(response.getQrCode())) {
                payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "SUCCESS",
                        "支付宝扫码支付下单成功", response.getQrCode());
                return response.getQrCode();
            }
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "支付宝扫码支付下单失败：" + response.getSubMsg(), null);
            throw new CommonException("支付宝扫码支付下单失败：" + response.getSubMsg());
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "支付宝扫码支付下单失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝扫码支付下单失败：" + e.getErrMsg());
        }
    }

    @Override
    public void createCodePay(PayOrder payOrder, String authCode) {
        if(StrUtil.isBlank(authCode)) {
            throw new CommonException("付款码不能为空");
        }
        AlipayTradePayRequest request = new AlipayTradePayRequest();
        request.setNotifyUrl(payConfigure.getAlipayNotifyUrl());
        JSONObject bizContent = buildTradeBizContent(payOrder, null);
        bizContent.set("auth_code", authCode);
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradePayResponse response = payConfigure.getAlipayClient().execute(request);
            // 支付成功，同步更新本地流水并发布支付成功事件
            if(response.isSuccess() && StrUtil.isNotBlank(response.getTradeNo())) {
                BigDecimal payAmount = new BigDecimal(response.getBuyerPayAmount());
                Date payTime = ObjectUtil.isNotEmpty(response.getGmtPayment()) ? response.getGmtPayment() : new Date();
                if(payOrderService.updatePaid(payOrder.getOutTradeNo(), response.getTradeNo(), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, response.getTradeNo(), payTime);
                }
                payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "SUCCESS",
                        "支付宝付款码支付成功", JSONUtil.toJsonStr(response));
                return;
            }
            // 结果未明确，向渠道查询确认一次
            if(ObjectUtil.isNotNull(response.getSubCode())) {
                PayStatusEnum status = queryChannelStatus(payOrder);
                if(PayStatusEnum.PAID.equals(status)) {
                    return;
                }
            }
            throw new CommonException("支付宝付款码支付失败：" + response.getSubMsg());
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "支付宝付款码支付失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝付款码支付失败：" + e.getErrMsg());
        }
    }

    @Override
    public PayStatusEnum queryChannelStatus(PayOrder payOrder) {
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", payOrder.getOutTradeNo());
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeQueryResponse response = payConfigure.getAlipayClient().execute(request);
            String tradeStatus = response.getTradeStatus();
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.QUERY, tradeStatus,
                    response.getSubMsg(), JSONUtil.toJsonStr(response));
            if(ALI_TRADE_STATUS_SUCCESS.equals(tradeStatus) || ALI_TRADE_STATUS_FINISHED.equals(tradeStatus)) {
                BigDecimal payAmount = StrUtil.isNotBlank(response.getBuyerPayAmount())
                        ? new BigDecimal(response.getBuyerPayAmount()) : payOrder.getOrderAmount();
                Date payTime = ObjectUtil.isNotEmpty(response.getSendPayDate()) ? response.getSendPayDate() : new Date();
                if(payOrderService.updatePaid(payOrder.getOutTradeNo(), response.getTradeNo(), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, response.getTradeNo(), payTime);
                }
                return PayStatusEnum.PAID;
            } else if(ALI_TRADE_STATUS_CLOSED.equals(tradeStatus)) {
                payOrderService.updateClosed(payOrder.getOutTradeNo());
                return PayStatusEnum.CLOSED;
            }
            return PayStatusEnum.UNPAID;
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.QUERY, "FAIL",
                    "支付宝订单查询失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝订单查询失败：" + e.getErrMsg());
        }
    }

    @Override
    public void updateOrderInfo(Map<String, String> params) {
        try {
            // 公钥证书模式验签
            boolean checkResult = AlipaySignature.rsaCertCheckV1(params, payConfigure.getAlipayCertPath(),
                    ALI_CHARSET, ALI_SIGN_TYPE);
            if(!checkResult) {
                log.error("支付宝支付回调验签失败");
                throw new CommonException("支付宝支付回调验签失败");
            }
            String outTradeNo = params.get("out_trade_no");
            PayOrder payOrder = payOrderService.getByOutTradeNo(outTradeNo);
            if(ObjectUtil.isEmpty(payOrder)) {
                log.error("支付宝支付回调：本地不存在支付流水，outTradeNo：{}", outTradeNo);
                return;
            }
            // 支付回调无登录态：直接同步明细落库与状态更新
            String tradeStatus = params.get("trade_status");
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.NOTIFY, tradeStatus,
                    "支付宝支付回调", JSONUtil.toJsonStr(params));
            if(ALI_TRADE_STATUS_SUCCESS.equals(tradeStatus) || ALI_TRADE_STATUS_FINISHED.equals(tradeStatus)) {
                // 事件发布点：支付成功后同步本地流水并发布 PaySuccessEvent
                BigDecimal payAmount = StrUtil.isNotBlank(params.get("buyer_pay_amount"))
                        ? new BigDecimal(params.get("buyer_pay_amount")) : payOrder.getOrderAmount();
                Date payTime = StrUtil.isNotBlank(params.get("gmt_payment"))
                        ? DateUtil.parse(params.get("gmt_payment")) : new Date();
                if(payOrderService.updatePaid(outTradeNo, params.get("trade_no"), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, params.get("trade_no"), payTime);
                }
            } else if(ALI_TRADE_STATUS_CLOSED.equals(tradeStatus)) {
                payOrderService.updateClosed(outTradeNo);
            }
        } catch (AlipayApiException e) {
            log.error("支付宝支付回调处理失败：{}", e.getErrMsg(), e);
            throw new CommonException("支付宝支付回调处理失败：" + e.getErrMsg());
        }
    }

    @Override
    public void refund(PayOrder payOrder, PayOrderRefund payOrderRefund) {
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", payOrder.getOutTradeNo());
        bizContent.set("refund_amount", payOrderRefund.getRefundAmount().toPlainString());
        request.setBizContent(bizContent.toString());
        try {
            AlipayTradeRefundResponse response = payConfigure.getAlipayClient().execute(request);
            if(response.isSuccess()) {
                payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.REFUND, "SUCCESS",
                        "支付宝退款成功", JSONUtil.toJsonStr(response));
                return;
            }
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.REFUND, "FAIL",
                    "支付宝退款失败：" + response.getSubMsg(), JSONUtil.toJsonStr(response));
            throw new CommonException("支付宝退款失败：" + response.getSubMsg());
        } catch (AlipayApiException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.REFUND, "FAIL",
                    "支付宝退款失败：" + e.getErrMsg(), null);
            throw new CommonException("支付宝退款失败：" + e.getErrMsg());
        }
    }

    /**
     * 组装支付宝交易公共参数
     *
     * @param payOrder    支付流水
     * @param productCode 销售产品码（PC支付：FAST_INSTANT_TRADE_PAY，H5支付：QUICK_WAP_WAY，扫码/付款码：空）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private JSONObject buildTradeBizContent(PayOrder payOrder, String productCode) {
        JSONObject bizContent = new JSONObject();
        bizContent.set("out_trade_no", payOrder.getOutTradeNo());
        bizContent.set("total_amount", payOrder.getOrderAmount().toPlainString());
        bizContent.set("subject", payOrder.getSubject());
        if(StrUtil.isNotBlank(payOrder.getBody())) {
            bizContent.set("body", payOrder.getBody());
        }
        if(StrUtil.isNotBlank(productCode)) {
            bizContent.set("product_code", productCode);
        }
        return bizContent;
    }

    /**
     * 发布支付成功事件
     *
     * @param payOrder 支付流水
     * @param tradeNo  支付平台流水号
     * @param payTime  支付完成时间
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private void publishPaySuccessEvent(PayOrder payOrder, String tradeNo, Date payTime) {
        // 以流水表最新数据为准
        PayOrder latestOrder = payOrderService.getByOutTradeNo(payOrder.getOutTradeNo());
        applicationEventPublisher.publishEvent(new PaySuccessEvent(this, latestOrder.getOutTradeNo(), tradeNo,
                latestOrder.getPayAmount().toPlainString(), latestOrder.getPayPlatform(), payTime));
    }
}
