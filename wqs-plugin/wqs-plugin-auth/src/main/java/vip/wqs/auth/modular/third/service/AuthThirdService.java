
package vip.wqs.auth.modular.third.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhyd.oauth.model.AuthCallback;
import vip.wqs.auth.modular.third.entity.AuthThirdUser;
import vip.wqs.auth.modular.third.param.AuthThirdBindAccountParam;
import vip.wqs.auth.modular.third.param.AuthThirdCallbackParam;
import vip.wqs.auth.modular.third.param.AuthThirdRenderParam;
import vip.wqs.auth.modular.third.result.AuthThirdRenderResult;

import java.util.List;

/**
 * 第三方登录Service接口
 *
 * @author jetox
 * @date 2022/7/8 16:20
 **/
public interface AuthThirdService extends IService<AuthThirdUser> {

    /**
     * 第三方登录页面渲染，返回授权结果
     *
     * @author jetox
     * @date 2022/7/8 16:37
     **/
    AuthThirdRenderResult render(AuthThirdRenderParam authThirdRenderParam);

    /**
     * 第三方登录授权回调，返回登录token
     *
     * @author jetox
     * @date 2022/7/8 16:42
     **/
    String callback(AuthThirdCallbackParam authThirdCallbackParam, AuthCallback authCallback);

    /**
     * 第三方登录绑定账号
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    String bindAccount(AuthThirdBindAccountParam authThirdBindAccountParam);

    /**
     * 根据用户id删除三方用户
     *
     * @author jetox
     * @date 2022/7/9 14:58
     */
    void deleteByUserIdList(List<String> userIdList);
}
