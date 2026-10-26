
package vip.wqs.auth.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 认证鉴权API
 *
 * @author yubaoshan
 * @date 2024/7/18 17:35
 */
public interface AuthApi {

    /**
     * 获取基础登录业务数据，b端在线用户，c端在线用户
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    JSONObject getUserSessionCount();

    /**
     * 获取三方用户总量
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    Long getThirdUserCount();

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

    /**
     * 校验验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    void validValidCode(String phoneOrEmail, String validCode, String validCodeReqNo);

    /**
     * B端账号密码登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginForB(String account, String password, String validCode, String validCodeReqNo);

    /**
     * C端账号密码登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginForC(String account, String password, String validCode, String validCodeReqNo);

    /**
     * B端用户id登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByIdForB(String userId, String device);

    /**
     * C端用户id登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByIdForC(String userId, String device);

    /**
     * B端账号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByAccountForB(String account, String device);

    /**
     * C端账号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByAccountForC(String account, String device);

    /**
     * B端手机号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByPhoneForB(String phone, String device);

    /**
     * C端手机号登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByPhoneForC(String phone, String device);

    /**
     * B端邮箱登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByEmailForB(String email, String device);

    /**
     * C端邮箱登录
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     */
    String doLoginByEmailForC(String email, String device);

    /**
     * 根据用户id删除三方用户
     *
     * @author jetox
     * @date 2022/7/9 14:58
     */
    void removeThirdUserByUserIdList(List<String> userIdList);
}
