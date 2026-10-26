
package vip.wqs.client.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.client.modular.user.entity.ClientUserExt;

/**
 * C端用户扩展Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface ClientUserExtService extends IService<ClientUserExt> {

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
    ClientUserExt createExtInfo(String userId, String sourceFromType);
}
