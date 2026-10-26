
package vip.wqs.pay.modular.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.pay.api.enums.PayPlatformEnum;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.pojo.PayCreateParam;
import vip.wqs.pay.api.pojo.PayCreateResult;
import vip.wqs.pay.api.pojo.PayOrderInfo;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.enums.PayOrderDetailsTypeEnum;
import vip.wqs.pay.modular.order.enums.PayOrderStatusEnum;
import vip.wqs.pay.modular.order.mapper.PayOrderMapper;
import vip.wqs.pay.modular.order.param.PayOrderPageParam;
import vip.wqs.pay.modular.order.service.PayOrderDetailsService;
import vip.wqs.pay.modular.order.service.PayOrderService;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水Service接口实现类
 * <p>PAY_ORDER 为 pay 插件内部支付流水表，业务方不得直接读写，统一通过 PayApi 操作。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Service
public class PayOrderServiceImpl extends ServiceImpl<PayOrderMapper, PayOrder> implements PayOrderService {

    @Resource
    private PayOrderDetailsService payOrderDetailsService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public PayCreateResult createOrder(PayCreateParam payCreateParam) {
        // 支付平台枚举校验，禁止散落魔法值
        boolean platformValid;
        try {
            platformValid = ObjectUtil.isNotEmpty(PayPlatformEnum.valueOf(payCreateParam.getPayPlatform()));
        } catch (IllegalArgumentException e) {
            platformValid = false;
        }
        if(StrUtil.isBlank(payCreateParam.getPayPlatform()) || !platformValid) {
            throw new CommonException("不支持的支付平台：{}，仅支持 ALIPAY / WXPAY", payCreateParam.getPayPlatform());
        }
        String outTradeNo = payCreateParam.getOutTradeNo();
        // 商户订单号可传可不传：传了用业务方的（须校验不重复），没传用雪花算法ID
        if(StrUtil.isNotBlank(outTradeNo)) {
            // 全局唯一校验：支付回调按订单号查询依赖唯一性，商户订单号全平台唯一
            if(ObjectUtil.isNotEmpty(this.getByOutTradeNo(outTradeNo))) {
                throw new CommonException("商户订单号已存在：{}，请勿重复创建支付订单", outTradeNo);
            }
        } else {
            outTradeNo = IdWorker.getIdStr();
        }
        PayOrder payOrder = new PayOrder();
        payOrder.setOutTradeNo(outTradeNo);
        payOrder.setSubject(payCreateParam.getSubject());
        payOrder.setBody(payCreateParam.getBody());
        payOrder.setOrderAmount(this.parseAmount(payCreateParam.getOrderAmount(), "订单金额"));
        payOrder.setPayPlatform(payCreateParam.getPayPlatform());
        payOrder.setStatus(PayOrderStatusEnum.UNPAID.getValue());
        this.save(payOrder);
        // 记录创建交互明细
        payOrderDetailsService.saveDetails(payOrder, PayOrderDetailsTypeEnum.CREATE,
                PayOrderStatusEnum.UNPAID.getValue(), "创建支付订单成功", null);
        return new PayCreateResult(payOrder.getId(), payOrder.getOutTradeNo());
    }

    @Override
    public PayOrder getByOutTradeNo(String outTradeNo) {
        if(StrUtil.isBlank(outTradeNo)) {
            return null;
        }
        return this.getOne(new LambdaQueryWrapper<PayOrder>().eq(PayOrder::getOutTradeNo, outTradeNo));
    }

    @Override
    public Page<PayOrder> page(PayOrderPageParam payOrderPageParam) {
        QueryWrapper<PayOrder> queryWrapper = new QueryWrapper<PayOrder>().checkSqlInjection();
        if(StrUtil.isNotBlank(payOrderPageParam.getSearchKey())) {
            queryWrapper.lambda().and(q -> q.like(PayOrder::getOutTradeNo, payOrderPageParam.getSearchKey()).or()
                    .like(PayOrder::getTradeNo, payOrderPageParam.getSearchKey()).or()
                    .like(PayOrder::getSubject, payOrderPageParam.getSearchKey()));
        }
        if(StrUtil.isNotBlank(payOrderPageParam.getPayPlatform())) {
            queryWrapper.lambda().eq(PayOrder::getPayPlatform, payOrderPageParam.getPayPlatform());
        }
        if(StrUtil.isNotBlank(payOrderPageParam.getStatus())) {
            queryWrapper.lambda().eq(PayOrder::getStatus, payOrderPageParam.getStatus());
        }
        if(ObjectUtil.isAllNotEmpty(payOrderPageParam.getSortField(), payOrderPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(payOrderPageParam.getSortOrder());
            queryWrapper.orderBy(true, payOrderPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(payOrderPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByDesc(PayOrder::getCreateTime);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Override
    public PayStatusEnum queryStatus(String outTradeNo) {
        PayOrder payOrder = this.getByOutTradeNo(outTradeNo);
        if(ObjectUtil.isEmpty(payOrder)) {
            return null;
        }
        return PayStatusEnum.valueOf(payOrder.getStatus());
    }

    @Override
    public PayOrderInfo getOrderInfo(String outTradeNo) {
        PayOrder payOrder = this.getByOutTradeNo(outTradeNo);
        if(ObjectUtil.isEmpty(payOrder)) {
            return null;
        }
        PayOrderInfo payOrderInfo = BeanUtil.toBean(payOrder, PayOrderInfo.class);
        payOrderInfo.setStatus(payOrder.getStatus());
        return payOrderInfo;
    }

    @Override
    public boolean updatePaid(String outTradeNo, String tradeNo, BigDecimal payAmount, Date payTime) {
        // 仅未支付状态可流转为已支付，重复回调直接跳过，保证幂等
        boolean updated = this.update(new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getOutTradeNo, outTradeNo)
                .eq(PayOrder::getStatus, PayOrderStatusEnum.UNPAID.getValue())
                .set(PayOrder::getTradeNo, tradeNo)
                .set(PayOrder::getPayAmount, payAmount)
                .set(PayOrder::getPayTime, payTime)
                .set(PayOrder::getStatus, PayOrderStatusEnum.PAID.getValue()));
        if(!updated) {
            // 未更新成功时校验订单是否已为已支付（重复通知幂等跳过），否则视为异常
            PayOrder payOrder = this.getByOutTradeNo(outTradeNo);
            if(ObjectUtil.isEmpty(payOrder)
                    || !PayOrderStatusEnum.PAID.getValue().equals(payOrder.getStatus())) {
                throw new CommonException("支付流水状态不为未支付或不存在：{}，无法流转为已支付", outTradeNo);
            }
        }
        return updated;
    }

    @Override
    public void updateClosed(String outTradeNo) {
        // 仅未支付状态可流转为已关闭，幂等
        this.update(new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getOutTradeNo, outTradeNo)
                .eq(PayOrder::getStatus, PayOrderStatusEnum.UNPAID.getValue())
                .set(PayOrder::getStatus, PayOrderStatusEnum.CLOSED.getValue()));
    }

    @Override
    public void updateRefunded(String outTradeNo) {
        // 仅已支付状态可流转为已发生退款，幂等
        this.update(new LambdaUpdateWrapper<PayOrder>()
                .eq(PayOrder::getOutTradeNo, outTradeNo)
                .eq(PayOrder::getStatus, PayOrderStatusEnum.PAID.getValue())
                .set(PayOrder::getStatus, PayOrderStatusEnum.REFUNDED.getValue()));
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
