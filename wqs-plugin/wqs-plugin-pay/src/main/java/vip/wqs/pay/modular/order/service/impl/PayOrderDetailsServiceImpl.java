
package vip.wqs.pay.modular.order.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.pay.modular.order.entity.PayOrder;
import vip.wqs.pay.modular.order.entity.PayOrderDetails;
import vip.wqs.pay.modular.order.enums.PayOrderDetailsTypeEnum;
import vip.wqs.pay.modular.order.mapper.PayOrderDetailsMapper;
import vip.wqs.pay.modular.order.service.PayOrderDetailsService;

/**
 * 支付交互明细Service接口实现类
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Service
public class PayOrderDetailsServiceImpl extends ServiceImpl<PayOrderDetailsMapper, PayOrderDetails> implements PayOrderDetailsService {

    @Override
    public void saveDetails(PayOrder payOrder, PayOrderDetailsTypeEnum detailsType, String channelStatus,
                            String channelMessage, String channelParams) {
        PayOrderDetails payOrderDetails = new PayOrderDetails();
        payOrderDetails.setPayOrderId(payOrder.getId());
        payOrderDetails.setOutTradeNo(payOrder.getOutTradeNo());
        payOrderDetails.setDetailsType(detailsType.getValue());
        payOrderDetails.setChannelStatus(channelStatus);
        payOrderDetails.setChannelMessage(channelMessage);
        payOrderDetails.setChannelParams(channelParams);
        this.save(payOrderDetails);
    }
}
