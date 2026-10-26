
package vip.wqs.sys.modular.user.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.common.util.CommonCryptogramUtil;
import vip.wqs.common.util.CommonOtpUtil;
import vip.wqs.sys.core.enums.SysYesOrNoEnum;
import vip.wqs.sys.modular.user.entity.SysUserExt;
import vip.wqs.sys.modular.user.enums.SysUserSourceFromTypeEnum;
import vip.wqs.sys.modular.user.mapper.SysUserExtMapper;
import vip.wqs.sys.modular.user.service.SysUserExtService;

/**
 * 用户扩展Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class SysUserExtServiceImpl extends ServiceImpl<SysUserExtMapper, SysUserExt> implements SysUserExtService {

    @Override
    public void updatePasswordLastTime(String userId) {
        SysUserExt sysUserExt = this.getOne(new LambdaQueryWrapper<SysUserExt>().eq(SysUserExt::getUserId, userId));
        if(ObjectUtil.isEmpty(sysUserExt)){
            sysUserExt = new SysUserExt();
            sysUserExt.setUserId(userId);
            sysUserExt.setSourceFromType(SysUserSourceFromTypeEnum.SYSTEM_ADD.getValue());
            sysUserExt.setPasswordUpdateTime(DateTime.now());
            this.save(sysUserExt);
        } else {
            sysUserExt.setPasswordUpdateTime(DateTime.now());
            this.updateById(sysUserExt);
        }
    }

    @Override
    public SysUserExt createExtInfo(String userId, String sourceFromType) {
        SysUserExt sysUserExt = new SysUserExt();
        sysUserExt.setUserId(userId);
        sysUserExt.setSourceFromType(sourceFromType);
        sysUserExt.setPasswordUpdateTime(DateTime.now());
        sysUserExt.setHasBindOtp(SysYesOrNoEnum.NO.getValue());
        String otpSecretKeyEncrypt = CommonCryptogramUtil.doSm4CbcEncrypt(CommonOtpUtil.generateSecretKey());
        sysUserExt.setOtpSecretKey(otpSecretKeyEncrypt);
        this.save(sysUserExt);
        return sysUserExt;
    }
}
