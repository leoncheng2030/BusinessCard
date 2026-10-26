
package vip.wqs.pay.modular.wx.controller;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.binarywang.wxpay.bean.notify.SignatureHeader;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.pay.core.consts.PayCommonConst;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.service.PayOrderService;
import vip.wqs.pay.modular.wx.param.PayWxCreatePayParam;
import vip.wqs.pay.modular.wx.service.PayWxService;

import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.Map;

/**
 * 微信支付控制器
 * <p>业务方完成 PayApi.createOrder 后，由前端调用本控制器接口拉起对应微信支付。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Tag(name = "微信支付控制器")
@RestController
@Validated
public class PayWxController {

    @Resource
    private PayWxService payWxService;

    @Resource
    private PayOrderService payOrderService;

    /**
     * 微信扫码支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信扫码支付下单")
    @CommonLog("微信扫码支付下单")
    @PostMapping("/pay/wx/createQrPay")
    public CommonResult<String> createQrPay(@RequestBody @Valid PayWxCreatePayParam payWxCreatePayParam) {
        return CommonResult.data(payWxService.createQrPay(getPayOrder(payWxCreatePayParam.getId())));
    }

    /**
     * 微信H5支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信H5支付下单")
    @CommonLog("微信H5支付下单")
    @PostMapping("/pay/wx/createH5Pay")
    public CommonResult<String> createH5Pay(@RequestBody @Valid PayWxCreatePayParam payWxCreatePayParam,
                                            HttpServletRequest request) {
        return CommonResult.data(payWxService.createH5Pay(getPayOrder(payWxCreatePayParam.getId()),
                getClientIp(request)));
    }

    /**
     * 微信JSAPI支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信JSAPI支付下单")
    @CommonLog("微信JSAPI支付下单")
    @PostMapping("/pay/wx/createJsPay")
    public CommonResult<Map<String, String>> createJsPay(@RequestBody @Valid PayWxCreatePayParam payWxCreatePayParam) {
        return CommonResult.data(payWxService.createJsPay(getPayOrder(payWxCreatePayParam.getId()),
                payWxCreatePayParam.getOpenid()));
    }

    /**
     * 微信付款码支付
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信付款码支付")
    @CommonLog("微信付款码支付")
    @PostMapping("/pay/wx/createCodePay")
    public CommonResult<String> createCodePay(@RequestBody @Valid PayWxCreatePayParam payWxCreatePayParam) {
        payWxService.createCodePay(getPayOrder(payWxCreatePayParam.getId()), payWxCreatePayParam.getAuthCode());
        return CommonResult.ok();
    }

    /**
     * 微信支付异步通知（无需业务方调用）
     * <p>微信服务端回调，验签成功后同步本地流水并发布支付成功事件。</p>
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信支付异步通知")
    @PostMapping("/pay/wx/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        try {
            String notifyData = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            payWxService.updateOrderInfo(notifyData, buildSignatureHeader(request));
            return PayCommonConst.WX_NOTIFY_SUCCESS_RESULT;
        } catch (Exception e) {
            log.error("微信支付异步通知处理失败：{}", e.getMessage(), e);
            return PayCommonConst.WX_NOTIFY_FAIL_RESULT;
        }
    }

    /**
     * 微信退款异步通知（无需业务方调用）
     * <p>微信服务端回调，验签成功后同步退款流水并发布退款成功事件。</p>
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "微信退款异步通知")
    @PostMapping("/pay/wx/refundNotifyUrl")
    public String refundNotifyUrl(HttpServletRequest request) {
        try {
            String notifyData = new String(request.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
            payWxService.updateRefundInfo(notifyData, buildSignatureHeader(request));
            return PayCommonConst.WX_NOTIFY_SUCCESS_RESULT;
        } catch (Exception e) {
            log.error("微信退款异步通知处理失败：{}", e.getMessage(), e);
            return PayCommonConst.WX_NOTIFY_FAIL_RESULT;
        }
    }

    /**
     * 根据主键获取支付流水
     *
     * @param id pay_order 主键
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private PayOrder getPayOrder(String id) {
        PayOrder payOrder = payOrderService.getById(id);
        if(ObjectUtil.isEmpty(payOrder)) {
            throw new CommonException("支付流水不存在，id：" + id);
        }
        return payOrder;
    }

    /**
     * 组装微信回调验签请求头
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private SignatureHeader buildSignatureHeader(HttpServletRequest request) {
        SignatureHeader signatureHeader = new SignatureHeader();
        signatureHeader.setTimeStamp(request.getHeader("Wechatpay-Timestamp"));
        signatureHeader.setNonce(request.getHeader("Wechatpay-Nonce"));
        signatureHeader.setSerial(request.getHeader("Wechatpay-Serial"));
        signatureHeader.setSignature(request.getHeader("Wechatpay-Signature"));
        return signatureHeader;
    }

    /**
     * 获取客户端IP
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private String getClientIp(HttpServletRequest request) {
        String clientIp = request.getHeader("X-Forwarded-For");
        if(StrUtil.isBlank(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        return StrUtil.isBlank(clientIp) ? "127.0.0.1" : clientIp;
    }
}
