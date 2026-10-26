
package vip.wqs.pay.modular.ali.controller;

import cn.hutool.core.util.ObjectUtil;
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
import vip.wqs.pay.modular.ali.param.PayAliCreatePayParam;
import vip.wqs.pay.modular.ali.service.PayAliService;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.service.PayOrderService;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

/**
 * 支付宝支付控制器
 * <p>业务方完成 PayApi.createOrder 后，由前端调用本控制器接口拉起对应支付宝支付。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Tag(name = "支付宝支付控制器")
@RestController
@Validated
public class PayAliController {

    @Resource
    private PayAliService payAliService;

    @Resource
    private PayOrderService payOrderService;

    /**
     * 支付宝PC网页支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "支付宝PC网页支付下单")
    @CommonLog("支付宝PC网页支付下单")
    @PostMapping("/pay/ali/createPcPay")
    public CommonResult<String> createPcPay(@RequestBody @Valid PayAliCreatePayParam payAliCreatePayParam) {
        return CommonResult.data(payAliService.createPcPay(getPayOrder(payAliCreatePayParam.getId())));
    }

    /**
     * 支付宝H5/手机网页支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "支付宝H5支付下单")
    @CommonLog("支付宝H5支付下单")
    @PostMapping("/pay/ali/createWapPay")
    public CommonResult<String> createWapPay(@RequestBody @Valid PayAliCreatePayParam payAliCreatePayParam) {
        return CommonResult.data(payAliService.createWapPay(getPayOrder(payAliCreatePayParam.getId())));
    }

    /**
     * 支付宝扫码支付下单
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "支付宝扫码支付下单")
    @CommonLog("支付宝扫码支付下单")
    @PostMapping("/pay/ali/createQrPay")
    public CommonResult<String> createQrPay(@RequestBody @Valid PayAliCreatePayParam payAliCreatePayParam) {
        return CommonResult.data(payAliService.createQrPay(getPayOrder(payAliCreatePayParam.getId())));
    }

    /**
     * 支付宝付款码支付
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "支付宝付款码支付")
    @CommonLog("支付宝付款码支付")
    @PostMapping("/pay/ali/createCodePay")
    public CommonResult<String> createCodePay(@RequestBody @Valid PayAliCreatePayParam payAliCreatePayParam) {
        payAliService.createCodePay(getPayOrder(payAliCreatePayParam.getId()), payAliCreatePayParam.getAuthCode());
        return CommonResult.ok();
    }

    /**
     * 支付宝异步通知（无需业务方调用）
     * <p>支付宝服务端回调，验签成功后同步本地流水并发布支付成功事件。</p>
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @Operation(summary = "支付宝异步通知")
    @PostMapping("/pay/ali/notifyUrl")
    public String notifyUrl(HttpServletRequest request) {
        Map<String, String> params = new HashMap<>();
        request.getParameterMap().forEach((key, values) -> {
            if(values.length > 0) {
                params.put(key, values[0]);
            }
        });
        try {
            payAliService.updateOrderInfo(params);
            return PayCommonConst.ALI_NOTIFY_SUCCESS_RESULT;
        } catch (Exception e) {
            log.error("支付宝异步通知处理失败：{}", e.getMessage(), e);
            return PayCommonConst.ALI_NOTIFY_FAIL_RESULT;
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
}
