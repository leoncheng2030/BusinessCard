
package vip.wqs.sys.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.user.entity.SysUserExt;

/**
 * 用户扩展Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface SysUserExtService extends IService<SysUserExt> {

    /**
     * 更新用户最新修改密码时间
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    void updatePasswordLastTime(String userId);

    /**
     * 插入扩展信息
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    SysUserExt createExtInfo(String userId, String sourceFromType);
}
