
package vip.wqs.auth.modular.login.service;

import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.auth.modular.login.param.*;
import vip.wqs.auth.modular.login.result.AuthPicValidCodeResult;

/**
 * 登录Service接口
 *
 * @author jetox
 * @date 2021/12/23 21:51
 */
public interface AuthService {

    /**
     * 获取图片验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    AuthPicValidCodeResult getPicCaptcha(String type);

    /**
     * 获取手机登录验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String getPhoneValidCode(AuthGetPhoneValidCodeParam authGetPhoneValidCodeParam, String type);

    /**
     * 获取邮箱登录验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String getEmailValidCode(AuthGetEmailValidCodeParam authGetEmailValidCodeParam, String type);

    /**
     * 账号密码登录
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String doLogin(AuthAccountPasswordLoginParam authAccountPasswordLoginParam, String type);

    /**
     * 手机验证码登录
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String doLoginByPhone(AuthPhoneValidCodeLoginParam authPhoneValidCodeLoginParam, String type);

    /**
     * 邮箱验证码登录
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String doLoginByEmail(AuthEmailValidCodeLoginParam authEmailValidCodeLoginParam, String type);

    /**
     * 获取B端登录用户信息
     *
     * @author jetox
     * @date 2021/10/12 15:59
     **/
    SaBaseLoginUser getLoginUser();

    /**
     * 获取C端登录用户信息
     *
     * @author jetox
     * @date 2021/10/12 15:59
     **/
    SaBaseClientLoginUser getClientLoginUser();

    /**
     * 根据用户id和客户端类型登录，用于第三方登录
     *
     * @author jetox
     * @date 2022/7/9 14:44
     */
    String doLoginById(String userId, String device, String type);

    /**
     * 根据用户账号和客户端类型登录，用于第三方登录
     *
     * @author jetox
     * @date 2022/7/9 14:44
     */
    String doLoginByAccount(String account, String device, String type);

    /**
     * 手机号登录
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String doLoginByPhone(String phone, String device, String type, String strategy);

    /**
     * 邮箱登录
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    String doLoginByEmail(String email, String device, String type, String strategy);

    /**
     * C端注册
     *
     * @author jetox
     * @date 2022/7/9 14:44
     */
    void register(AuthRegisterParam authRegisterParam, String type);

    /**
     * B端动态口令登录
     *
     * @author jetox
     * @date 2021/10/15 13:12
     **/
    String doLoginByOtp(AuthOtpLoginParam authOtpLoginParam, String type);

    /**
     * 校验验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    void validValidCode(String phoneOrEmail, String validCode, String validCodeReqNo);

    /**
     * 通过第三方Token交换登录（用于iframe嵌入免登）
     *
     * @param authThirdTokenLoginParam 第三方Token登录参数
     * @return 本系统Token
     * @author yubaoshan
     * @date 2026/2/11
     */
    String doLoginByThirdToken(AuthThirdTokenLoginParam authThirdTokenLoginParam);

    /**
     * 获取B端验证码是否开启
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    boolean getDefaultCaptchaOpenForB();

    /**
     * 获取C端验证码是否开启
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    boolean getDefaultCaptchaOpenForC();
}
