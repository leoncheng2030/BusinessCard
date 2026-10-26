
package vip.wqs.auth.modular.third.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthUser;
import vip.wqs.auth.modular.third.entity.AuthThirdUser;

/**
 * 第三方登录Service接口
 *
 * @author jetox
 * @date 2022/7/8 16:20
 **/
public interface AuthThirdUserService extends IService<AuthThirdUser> {

    /**
     * 保存三方用户
     *
     * @author jetox
     * @date 2022/7/9 14:58
     */
    void insertAuthThirdUser(String userId, AuthUser authUser, String platform);
}
