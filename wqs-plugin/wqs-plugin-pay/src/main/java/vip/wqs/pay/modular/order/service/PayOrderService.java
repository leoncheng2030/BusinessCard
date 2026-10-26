
package vip.wqs.pay.modular.order.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.pay.api.enums.PayStatusEnum;
import vip.wqs.pay.api.pojo.PayCreateParam;
import vip.wqs.pay.api.pojo.PayCreateResult;
import vip.wqs.pay.api.pojo.PayOrderInfo;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.param.PayOrderPageParam;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 支付流水Service接口
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public interface PayOrderService extends IService<PayOrder> {

    /**
     * 创建支付订单（支付流水）
     *
     * @param payCreateParam 创建参数
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayCreateResult createOrder(PayCreateParam payCreateParam);

    /**
     * 根据商户订单号查询支付流水
     *
     * @param outTradeNo 商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayOrder getByOutTradeNo(String outTradeNo);

    /**
     * 获取支付流水分页（管理后台）
     *
     * @param payOrderPageParam 分页参数
     * @author wqs
     * @date 2026/9/3 14:30
     */
    Page<PayOrder> page(PayOrderPageParam payOrderPageParam);

    /**
     * 查询简化状态
     *
     * @param outTradeNo 商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayStatusEnum queryStatus(String outTradeNo);

    /**
     * 获取订单只读视图
     *
     * @param outTradeNo 商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    PayOrderInfo getOrderInfo(String outTradeNo);

    /**
     * 更新支付流水为已支付（仅未支付状态可流转，幂等）
     *
     * @param outTradeNo 商户订单号
     * @param tradeNo    支付平台流水号
     * @param payAmount  实际支付金额（元）
     * @param payTime    支付完成时间
     * @return true 本次更新成功（调用方需发布支付成功事件）；false 已处理过（重复通知，幂等跳过）
     * @author wqs
     * @date 2026/9/3 14:30
     */
    boolean updatePaid(String outTradeNo, String tradeNo, BigDecimal payAmount, Date payTime);

    /**
     * 更新支付流水为已关闭（仅未支付状态可流转，幂等）
     *
     * @param outTradeNo 商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateClosed(String outTradeNo);

    /**
     * 更新支付流水为已发生退款（仅已支付状态可流转，幂等）
     *
     * @param outTradeNo 商户订单号
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void updateRefunded(String outTradeNo);
}
