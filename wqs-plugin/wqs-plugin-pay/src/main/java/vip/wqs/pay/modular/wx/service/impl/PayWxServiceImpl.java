
package vip.wqs.pay.modular.wx.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import com.github.binarywang.wxpay.bean.notify.WxPayNotifyV3Result;
import com.github.binarywang.wxpay.bean.notify.WxPayRefundNotifyV3Result;
import com.github.binarywang.wxpay.bean.request.WxPayMicropayRequest;
import com.github.binarywang.wxpay.bean.request.WxPayRefundV3Request;
import com.github.binarywang.wxpay.bean.request.WxPayUnifiedOrderV3Request;
import com.github.binarywang.wxpay.bean.result.WxPayMicropayResult;
import com.github.binarywang.wxpay.bean.result.WxPayOrderQueryV3Result;
import com.github.binarywang.wxpay.bean.result.enums.TradeTypeEnum;
import com.github.binarywang.wxpay.exception.WxPayException;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import vip.wqs.common.exception.CommonException;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.event.PaySuccessEvent;
import vip.wqs.pay.core.config.PayConfigure;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;
import vip.wqs.pay.modular.order.enums.PayOrderDetailsTypeEnum;
import vip.wqs.pay.modular.order.enums.PayOrderRefundStatusEnum;
import vip.wqs.pay.modular.order.service.PayOrderDetailsService;
import vip.wqs.pay.modular.order.service.PayOrderRefundService;
import vip.wqs.pay.modular.order.service.PayOrderService;
import vip.wqs.pay.modular.wx.service.PayWxService;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 微信支付Service接口实现类
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Service
public class PayWxServiceImpl implements PayWxService {

    /** 微信交易状态：支付成功 */
    private static final String WX_TRADE_STATE_SUCCESS = "SUCCESS";

    /** 微信交易状态：已关闭 */
    private static final String WX_TRADE_STATE_CLOSED = "CLOSED";

    /** 微信交易状态：已撤销 */
    private static final String WX_TRADE_STATE_REVOKED = "REVOKED";

    /** 微信交易状态：支付失败 */
    private static final String WX_TRADE_STATE_PAYERROR = "PAYERROR";

    /** 微信付款码支付：用户支付中（需查询确认） */
    private static final String WX_MICROPAY_USERPAYING = "USERPAYING";

    /** 微信付款码支付：系统错误（需查询确认） */
    private static final String WX_MICROPAY_SYSTEMERROR = "SYSTEMERROR";

    /** 微信付款码支付：银行系统异常（需查询确认） */
    private static final String WX_MICROPAY_BANKERROR = "BANKERROR";

    @Resource
    private PayConfigure payConfigure;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayOrderDetailsService payOrderDetailsService;

    @Resource
    private PayOrderRefundService payOrderRefundService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Override
    public String createQrPay(PayOrder payOrder) {
        WxPayUnifiedOrderV3Request request = buildUnifiedOrderRequest(payOrder, null);
        try {
            String codeUrl = payConfigure.getWxPayService().createOrderV3(TradeTypeEnum.NATIVE, request);
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, WX_TRADE_STATE_SUCCESS,
                    "微信扫码支付下单成功", codeUrl);
            return codeUrl;
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "微信扫码支付下单失败：" + e.getErrCodeDes(), null);
            throw new CommonException("微信扫码支付下单失败：" + e.getErrCodeDes());
        }
    }

    @Override
    public String createH5Pay(PayOrder payOrder, String clientIp) {
        WxPayUnifiedOrderV3Request request = buildUnifiedOrderRequest(payOrder, null);
        // H5支付场景信息必填
        WxPayUnifiedOrderV3Request.SceneInfo sceneInfo = new WxPayUnifiedOrderV3Request.SceneInfo();
        sceneInfo.setPayerClientIp(clientIp);
        WxPayUnifiedOrderV3Request.H5Info h5Info = new WxPayUnifiedOrderV3Request.H5Info();
        h5Info.setType("Wap");
        sceneInfo.setH5Info(h5Info);
        request.setSceneInfo(sceneInfo);
        try {
            String h5Url = payConfigure.getWxPayService().createOrderV3(TradeTypeEnum.H5, request);
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, WX_TRADE_STATE_SUCCESS,
                    "微信H5支付下单成功", h5Url);
            return h5Url;
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "微信H5支付下单失败：" + e.getErrCodeDes(), null);
            throw new CommonException("微信H5支付下单失败：" + e.getErrCodeDes());
        }
    }

    @Override
    public Map<String, String> createJsPay(PayOrder payOrder, String openid) {
        if(StrUtil.isBlank(openid)) {
            throw new CommonException("openid不能为空");
        }
        WxPayUnifiedOrderV3Request request = buildUnifiedOrderRequest(payOrder, openid);
        try {
            String prepayId = payConfigure.getWxPayService().createOrderV3(TradeTypeEnum.JSAPI, request);
            // 手写RSA签名，组装前端调起支付所需参数
            Map<String, String> payParams = buildJsPayParams(prepayId);
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, WX_TRADE_STATE_SUCCESS,
                    "微信JSAPI支付下单成功", JSONUtil.toJsonStr(payParams));
            return payParams;
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "微信JSAPI支付下单失败：" + e.getErrCodeDes(), null);
            throw new CommonException("微信JSAPI支付下单失败：" + e.getErrCodeDes());
        } catch (Exception e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "微信JSAPI支付签名失败：" + e.getMessage(), null);
            throw new CommonException("微信JSAPI支付签名失败：" + e.getMessage());
        }
    }

    @Override
    public void createCodePay(PayOrder payOrder, String authCode) {
        if(StrUtil.isBlank(authCode)) {
            throw new CommonException("付款码不能为空");
        }
        WxPayMicropayRequest request = new WxPayMicropayRequest();
        request.setOutTradeNo(payOrder.getOutTradeNo());
        request.setBody(payOrder.getSubject());
        request.setTotalFee(orderAmountToFen(payOrder.getOrderAmount()));
        request.setAuthCode(authCode);
        try {
            WxPayMicropayResult result = payConfigure.getWxPayService().micropay(request);
            // 支付成功，同步更新本地流水并发布支付成功事件
            if(WX_TRADE_STATE_SUCCESS.equals(result.getReturnCode()) && WX_TRADE_STATE_SUCCESS.equals(result.getResultCode())) {
                BigDecimal payAmount = fenToYuan(result.getTotalFee());
                Date payTime = new Date();
                if(payOrderService.updatePaid(payOrder.getOutTradeNo(), result.getTransactionId(), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, result.getTransactionId(), payTime);
                }
                payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, WX_TRADE_STATE_SUCCESS,
                        "微信付款码支付成功", JSONUtil.toJsonStr(result));
                return;
            }
            // 用户支付中或系统异常，向渠道查询确认一次
            if(WX_MICROPAY_USERPAYING.equals(result.getErrCode()) || WX_MICROPAY_SYSTEMERROR.equals(result.getErrCode())
                    || WX_MICROPAY_BANKERROR.equals(result.getErrCode())) {
                PayStatusEnum status = queryChannelStatus(payOrder);
                if(PayStatusEnum.PAID.equals(status)) {
                    return;
                }
                throw new CommonException("微信付款码支付结果未确认，请用户重新出示付款码");
            }
            throw new CommonException("微信付款码支付失败：" + result.getErrCodeDes());
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.ORDER, "FAIL",
                    "微信付款码支付失败：" + e.getErrCodeDes(), null);
            throw new CommonException("微信付款码支付失败：" + e.getErrCodeDes());
        }
    }

    @Override
    public PayStatusEnum queryChannelStatus(PayOrder payOrder) {
        try {
            WxPayOrderQueryV3Result result = payConfigure.getWxPayService().queryOrderV3(null, payOrder.getOutTradeNo());
            String tradeState = result.getTradeState();
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.QUERY, tradeState,
                    result.getTradeStateDesc(), JSONUtil.toJsonStr(result));
            if(WX_TRADE_STATE_SUCCESS.equals(tradeState)) {
                BigDecimal payAmount = BigDecimal.ZERO;
                if(ObjectUtil.isNotNull(result.getAmount()) && ObjectUtil.isNotNull(result.getAmount().getPayerTotal())) {
                    payAmount = fenToYuan(result.getAmount().getPayerTotal());
                }
                Date payTime = parseWxTime(result.getSuccessTime());
                if(payOrderService.updatePaid(payOrder.getOutTradeNo(), result.getTransactionId(), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, result.getTransactionId(), payTime);
                }
                return PayStatusEnum.PAID;
            } else if(WX_TRADE_STATE_CLOSED.equals(tradeState) || WX_TRADE_STATE_REVOKED.equals(tradeState)
                    || WX_TRADE_STATE_PAYERROR.equals(tradeState)) {
                payOrderService.updateClosed(payOrder.getOutTradeNo());
                return PayStatusEnum.CLOSED;
            }
            return PayStatusEnum.UNPAID;
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.QUERY, "FAIL",
                    "微信订单查询失败：" + e.getErrCodeDes(), null);
            throw new CommonException("微信订单查询失败：" + e.getErrCodeDes());
        }
    }

    @Override
    public void updateOrderInfo(String notifyData, SignatureHeader signatureHeader) {
        try {
            WxPayNotifyV3Result notifyResult = payConfigure.getWxPayService().parseOrderNotifyV3Result(notifyData, signatureHeader);
            WxPayNotifyV3Result.DecryptNotifyResult result = notifyResult.getResult();
            PayOrder payOrder = payOrderService.getByOutTradeNo(result.getOutTradeNo());
            if(ObjectUtil.isEmpty(payOrder)) {
                log.error("微信支付回调：本地不存在支付流水，outTradeNo：{}", result.getOutTradeNo());
                return;
            }
            // 支付回调无登录态：直接同步明细落库与状态更新
            String tradeState = result.getTradeState();
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.NOTIFY, tradeState,
                    result.getTradeStateDesc(), notifyData);
            if(WX_TRADE_STATE_SUCCESS.equals(tradeState)) {
                // 事件发布点：支付成功后同步本地流水并发布 PaySuccessEvent
                BigDecimal payAmount = BigDecimal.ZERO;
                if(ObjectUtil.isNotNull(result.getAmount()) && ObjectUtil.isNotNull(result.getAmount().getPayerTotal())) {
                    payAmount = fenToYuan(result.getAmount().getPayerTotal());
                }
                Date payTime = parseWxTime(result.getSuccessTime());
                if(payOrderService.updatePaid(payOrder.getOutTradeNo(), result.getTransactionId(), payAmount, payTime)) {
                    publishPaySuccessEvent(payOrder, result.getTransactionId(), payTime);
                }
            } else if(WX_TRADE_STATE_CLOSED.equals(tradeState) || WX_TRADE_STATE_REVOKED.equals(tradeState)
                    || WX_TRADE_STATE_PAYERROR.equals(tradeState)) {
                payOrderService.updateClosed(payOrder.getOutTradeNo());
            }
        } catch (WxPayException e) {
            log.error("微信支付回调处理失败：{}", e.getMessage(), e);
            // 抛异常让微信重试，controller 捕获后返回失败报文
            throw new CommonException("微信支付回调处理失败：" + e.getMessage());
        }
    }

    @Override
    public void refund(PayOrder payOrder, PayOrderRefund payOrderRefund) {
        WxPayRefundV3Request request = new WxPayRefundV3Request();
        request.setOutTradeNo(payOrder.getOutTradeNo());
        request.setOutRefundNo(payOrderRefund.getRefundNo());
        request.setNotifyUrl(payConfigure.getWxRefundNotifyUrl());
        WxPayRefundV3Request.Amount amount = new WxPayRefundV3Request.Amount();
        amount.setRefund(orderAmountToFen(payOrderRefund.getRefundAmount()));
        amount.setTotal(orderAmountToFen(payOrder.getPayAmount()));
        amount.setCurrency("CNY");
        request.setAmount(amount);
        try {
            payConfigure.getWxPayService().refundV3(request);
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.REFUND, WX_TRADE_STATE_SUCCESS,
                    "微信退款申请成功", payOrderRefund.getRefundNo());
        } catch (WxPayException e) {
            payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.REFUND, "FAIL",
                    "微信退款申请失败：" + e.getErrCodeDes(), payOrderRefund.getRefundNo());
            throw new CommonException("微信退款申请失败：" + e.getErrCodeDes());
        }
    }

    @Override
    public void updateRefundInfo(String notifyData, SignatureHeader signatureHeader) {
        try {
            WxPayRefundNotifyV3Result notifyResult = payConfigure.getWxPayService().parseRefundNotifyV3Result(notifyData, signatureHeader);
            WxPayRefundNotifyV3Result.DecryptNotifyResult result = notifyResult.getResult();
            String refundStatus = result.getRefundStatus();
            PayOrderRefundStatusEnum statusEnum = WX_TRADE_STATE_SUCCESS.equals(refundStatus)
                    ? PayOrderRefundStatusEnum.SUCCESS : PayOrderRefundStatusEnum.FAIL;
            // 退款关闭/异常按失败处理；未明确结果则置失败，后续可通过查询对账
            Date refundTime = WX_TRADE_STATE_SUCCESS.equals(refundStatus) ? parseWxTime(result.getSuccessTime()) : null;
            // 支付回调无登录态：按退款单号查到退款流水后直接更新
            PayOrderRefund payOrderRefund = payOrderRefundService.getByRefundNo(result.getOutRefundNo());
            if(ObjectUtil.isEmpty(payOrderRefund)) {
                log.error("微信退款回调：本地不存在退款流水，refundNo：{}", result.getOutRefundNo());
                return;
            }
            payOrderRefundService.updateRefundInfo(result.getOutRefundNo(), result.getRefundId(), statusEnum, refundTime);
        } catch (WxPayException e) {
            log.error("微信退款回调处理失败：{}", e.getMessage(), e);
            throw new CommonException("微信退款回调处理失败：" + e.getMessage());
        }
    }

    /**
     * 组装微信统一下单请求
     * @param payOrder 支付流水
     * @param openid   用户openid（JSAPI支付必填，其余为空）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private WxPayUnifiedOrderV3Request buildUnifiedOrderRequest(PayOrder payOrder, String openid) {
        WxPayUnifiedOrderV3Request request = new WxPayUnifiedOrderV3Request();
        request.setOutTradeNo(payOrder.getOutTradeNo());
        request.setDescription(payOrder.getSubject());
        request.setNotifyUrl(payConfigure.getWxNotifyUrl());
        WxPayUnifiedOrderV3Request.Amount amount = new WxPayUnifiedOrderV3Request.Amount();
        amount.setTotal(orderAmountToFen(payOrder.getOrderAmount()));
        amount.setCurrency("CNY");
        request.setAmount(amount);
        if(StrUtil.isNotBlank(openid)) {
            WxPayUnifiedOrderV3Request.Payer payer = new WxPayUnifiedOrderV3Request.Payer();
            payer.setOpenid(openid);
            request.setPayer(payer);
        }
        return request;
    }

    /**
     * 组装JSAPI前端调起支付参数（手写RSA签名）
     * <p>签名串规则：appId
时间戳
随机串
prepay_id=xxx
，使用商户私钥 SHA256withRSA 签名。</p>
     *
     * @param prepayId 预支付交易会话标识
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private Map<String, String> buildJsPayParams(String prepayId) throws Exception {
        String appId = payConfigure.getWxAppId();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        String nonceStr = IdUtil.fastSimpleUUID();
        String packageValue = "prepay_id=" + prepayId;
        String message = appId + "\n" + timeStamp + "\n" + nonceStr + "\n" + packageValue + "\n";
        // 读取商户私钥（apiclient_key.pem，PKCS8格式）
        String privateKeyPem = FileUtil.readUtf8String(payConfigure.getWxPrivateKeyPath());
        String paySign = buildRsaSign(message, privateKeyPem);
        Map<String, String> payParams = new LinkedHashMap<>();
        payParams.put("appId", appId);
        payParams.put("timeStamp", timeStamp);
        payParams.put("nonceStr", nonceStr);
        payParams.put("packageValue", packageValue);
        payParams.put("signType", "RSA");
        payParams.put("paySign", paySign);
        return payParams;
    }

    /**
     * 使用RSA私钥（SHA256withRSA）签名
     *
     * @param message       待签名字符串
     * @param privateKeyPem 私钥PEM内容
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private String buildRsaSign(String message, String privateKeyPem) throws Exception {
        String keyContent = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] keyBytes = Base64.getDecoder().decode(keyContent);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(privateKey);
        signature.update(message.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(signature.sign());
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

    /**
     * 金额元转分
     *
     * @param amountYuan 金额（元）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private Integer orderAmountToFen(BigDecimal amountYuan) {
        return amountYuan.multiply(new BigDecimal("100")).intValue();
    }

    /**
     * 金额分转元
     *
     * @param amountFen 金额（分）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private BigDecimal fenToYuan(Integer amountFen) {
        return new BigDecimal(amountFen).divide(new BigDecimal("100"), 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 解析微信时间（RFC3339，如2018-06-08T10:34:56+08:00）
     *
     * @param successTime 微信时间字符串
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private Date parseWxTime(String successTime) {
        if(StrUtil.isBlank(successTime)) {
            return new Date();
        }
        try {
            return Date.from(OffsetDateTime.parse(successTime).toInstant());
        } catch (Exception e) {
            log.warn("微信时间解析失败：{}", successTime);
            return new Date();
        }
    }
}
