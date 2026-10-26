
package vip.wqs.sys.modular.user.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.user.entity.SysUserPassword;

import java.util.List;

/**
 * 用户密码Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface SysUserPasswordService extends IService<SysUserPassword> {

    /**
     * 追加用户历史密码信息
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    void insertUserPasswordHistory(String userId, String newPassword);

    /**
     * 获取用户前N个历史密码
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    List<String> getUserPasswordHistoryLimit(String userId, int limitValue);
}
