
package vip.wqs.pay.modular.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderDetails;
import vip.wqs.pay.modular.order.enums.PayOrderDetailsTypeEnum;

/**
 * 支付交互明细Service接口
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public interface PayOrderDetailsService extends IService<PayOrderDetails> {

    /**
     * 保存支付交互明细
     *
     * @param payOrder       支付流水
     * @param detailsType    交互类型
     * @param channelStatus  渠道返回状态
     * @param channelMessage 渠道返回信息
     * @param channelParams  渠道交互报文快照
     * @author wqs
     * @date 2026/9/3 14:30
     */
    void saveDetails(PayOrder payOrder, PayOrderDetailsTypeEnum detailsType, String channelStatus,
                     String channelMessage, String channelParams);
}
