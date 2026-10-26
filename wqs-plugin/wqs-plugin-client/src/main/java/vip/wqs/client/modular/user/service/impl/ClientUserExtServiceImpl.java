
package vip.wqs.client.modular.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.client.core.enums.ClientYesOrNoEnum;
import vip.wqs.client.modular.user.entity.ClientUserExt;
import vip.wqs.client.modular.user.enums.ClientUserSourceFromTypeEnum;
import vip.wqs.client.modular.user.mapper.ClientUserExtMapper;
import vip.wqs.client.modular.user.service.ClientUserExtService;
import vip.wqs.common.util.CommonCryptogramUtil;
import vip.wqs.common.util.CommonOtpUtil;

/**
 * C端用户扩展Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class ClientUserExtServiceImpl extends ServiceImpl<ClientUserExtMapper, ClientUserExt> implements ClientUserExtService {

    @Override
    public void updatePasswordLastTime(String userId) {
        ClientUserExt clientUserExt = this.getOne(new LambdaQueryWrapper<ClientUserExt>().eq(ClientUserExt::getUserId, userId));
        if(ObjectUtil.isEmpty(clientUserExt)){
            clientUserExt = new ClientUserExt();
            clientUserExt.setUserId(userId);
            clientUserExt.setSourceFromType(ClientUserSourceFromTypeEnum.SYSTEM_ADD.getValue());
            clientUserExt.setPasswordUpdateTime(DateTime.now());
            this.save(clientUserExt);
        } else {
            clientUserExt.setPasswordUpdateTime(DateTime.now());
            this.updateById(clientUserExt);
        }
    }

    @Override
    public ClientUserExt createExtInfo(String userId, String sourceFromType) {
        ClientUserExt clientUserExt = new ClientUserExt();
        clientUserExt.setUserId(userId);
        clientUserExt.setSourceFromType(sourceFromType);
        clientUserExt.setPasswordUpdateTime(DateTime.now());
        clientUserExt.setHasBindOtp(ClientYesOrNoEnum.NO.getValue());
        String otpSecretKeyEncrypt = CommonCryptogramUtil.doSm4CbcEncrypt(CommonOtpUtil.generateSecretKey());
        clientUserExt.setOtpSecretKey(otpSecretKeyEncrypt);
        this.save(clientUserExt);
        return clientUserExt;
    }
}
