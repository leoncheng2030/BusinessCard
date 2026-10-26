
package vip.wqs.pay.modular.order.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.pay.api.enums.PayPlatformEnum;
import vip.wqs.pay.api.event.PayRefundSuccessEvent;
import vip.wqs.pay.api.pojo.PayRefundParam;
import vip.wqs.pay.modular.ali.service.PayAliService;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderRefund;
import vip.wqs.pay.modular.order.enums.PayOrderRefundStatusEnum;
import vip.wqs.pay.modular.order.enums.PayOrderStatusEnum;
import vip.wqs.pay.modular.order.mapper.PayOrderRefundMapper;
import vip.wqs.pay.modular.order.service.PayOrderRefundService;
import vip.wqs.pay.modular.order.service.PayOrderService;
import vip.wqs.pay.modular.wx.service.PayWxService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 退款流水Service接口实现类
 * <p>事件发布点：支付宝退款同步返回成功，创建退款记录时立即发布 PayRefundSuccessEvent；
 * 微信退款异步回调，事件在收到回调（updateRefundInfo）时发布。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Service
public class PayOrderRefundServiceImpl extends ServiceImpl<PayOrderRefundMapper, PayOrderRefund> implements PayOrderRefundService {

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private PayWxService payWxService;

    @Resource
    private PayAliService payAliService;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void refund(PayRefundParam payRefundParam) {
        String outTradeNo = payRefundParam.getOutTradeNo();
        if(StrUtil.isBlank(outTradeNo)) {
            throw new CommonException("商户订单号不能为空");
        }
        PayOrder payOrder = payOrderService.getByOutTradeNo(outTradeNo);
        if(ObjectUtil.isEmpty(payOrder)) {
            throw new CommonException("支付流水不存在：{}", outTradeNo);
        }
        if(!PayOrderStatusEnum.PAID.getValue().equals(payOrder.getStatus())) {
            throw new CommonException("支付流水状态为{}，不可发起退款", payOrder.getStatus());
        }
        BigDecimal refundAmount = this.parseAmount(payRefundParam.getRefundAmount(), "退款金额");
        if(refundAmount.compareTo(payOrder.getPayAmount()) > 0) {
            throw new CommonException("退款金额不能大于实际支付金额：{}", payOrder.getPayAmount().toPlainString());
        }
        // 创建退款流水
        PayOrderRefund payOrderRefund = new PayOrderRefund();
        payOrderRefund.setPayOrderId(payOrder.getId());
        payOrderRefund.setOutTradeNo(outTradeNo);
        payOrderRefund.setRefundNo(IdWorker.getIdStr());
        payOrderRefund.setRefundAmount(refundAmount);
        payOrderRefund.setStatus(PayOrderRefundStatusEnum.REFUNDING.getValue());
        this.save(payOrderRefund);
        // 按支付平台调用对应渠道退款
        try {
            if(PayPlatformEnum.WXPAY.getValue().equals(payOrder.getPayPlatform())) {
                // 微信退款异步，结果经退款回调通知，事件在回调到达时发布
                payWxService.refund(payOrder, payOrderRefund);
            } else if(PayPlatformEnum.ALIPAY.getValue().equals(payOrder.getPayPlatform())) {
                // 支付宝退款同步返回成功，创建退款记录时立即发布事件
                payAliService.refund(payOrder, payOrderRefund);
                Date refundTime = new Date();
                payOrderRefund.setChannelRefundNo(payOrder.getTradeNo());
                payOrderRefund.setStatus(PayOrderRefundStatusEnum.SUCCESS.getValue());
                payOrderRefund.setRefundTime(refundTime);
                this.updateById(payOrderRefund);
                payOrderService.updateRefunded(outTradeNo);
                publishRefundSuccessEvent(payOrderRefund, refundTime);
            } else {
                throw new CommonException("不支持的支付平台：{}", payOrder.getPayPlatform());
            }
        } catch (Exception e) {
            // 渠道退款失败时整体事务回滚，本地不保留退款流水，由调用方捕获异常提示用户
            throw e;
        }
    }

    @Override
    public PayOrderRefund getByRefundNo(String refundNo) {
        if(StrUtil.isBlank(refundNo)) {
            return null;
        }
        return this.getOne(new LambdaQueryWrapper<PayOrderRefund>().eq(PayOrderRefund::getRefundNo, refundNo));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateRefundInfo(String refundNo, String channelRefundNo, PayOrderRefundStatusEnum status, Date refundTime) {
        PayOrderRefund payOrderRefund = this.getByRefundNo(refundNo);
        if(ObjectUtil.isEmpty(payOrderRefund)) {
            log.error("微信退款回调：本地不存在退款流水，refundNo：{}", refundNo);
            return;
        }
        payOrderRefund.setChannelRefundNo(channelRefundNo);
        payOrderRefund.setStatus(status.getValue());
        payOrderRefund.setRefundTime(refundTime);
        this.updateById(payOrderRefund);
        if(PayOrderRefundStatusEnum.SUCCESS.equals(status)) {
            // 事件发布点：微信退款回调成功后发布 PayRefundSuccessEvent
            payOrderService.updateRefunded(payOrderRefund.getOutTradeNo());
            publishRefundSuccessEvent(payOrderRefund, refundTime);
        }
    }

    /**
     * 发布退款成功事件
     *
     * @param payOrderRefund 退款流水
     * @param refundTime     退款完成时间
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private void publishRefundSuccessEvent(PayOrderRefund payOrderRefund, Date refundTime) {
        applicationEventPublisher.publishEvent(new PayRefundSuccessEvent(this, payOrderRefund.getOutTradeNo(),
                payOrderRefund.getRefundNo(), payOrderRefund.getRefundAmount().toPlainString(), refundTime));
    }

    /**
     * 金额字符串转BigDecimal（单位元，非法格式抛业务异常）
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private BigDecimal parseAmount(String amount, String fieldName) {
        try {
            BigDecimal result = new BigDecimal(amount);
            if(result.compareTo(BigDecimal.ZERO) <= 0) {
                throw new CommonException("{}必须大于0", fieldName);
            }
            return result;
        } catch (NumberFormatException e) {
            throw new CommonException("{}格式不正确：{}，金额单位为元，如 \"0.01\"、\"29.90\"", fieldName, amount);
        }
    }
}
