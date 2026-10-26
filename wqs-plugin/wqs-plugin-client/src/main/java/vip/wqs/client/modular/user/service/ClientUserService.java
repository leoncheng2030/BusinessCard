
package vip.wqs.client.modular.user.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.entity.ClientUserExt;
import vip.wqs.client.modular.user.param.*;
import vip.wqs.client.modular.user.result.ClientLoginUser;
import vip.wqs.client.modular.user.result.ClientUserPicValidCodeResult;

import java.util.List;

/**
 * 用户Service接口
 *
 * @author jetox
 * @date 2022/4/21 18:35
 **/
public interface ClientUserService extends IService<ClientUser> {

    /**
     * 根据id获取用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    ClientLoginUser getUserById(String id);

    /**
     * 根据账户获取用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    ClientLoginUser getUserByAccount(String account);

    /**
     * 根据手机号获取用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    ClientLoginUser getUserByPhone(String phone);

    /**
     * 根据邮箱获取用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    ClientLoginUser getUserByEmail(String email);

    /**
     * 获取用户分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<ClientUser> page(ClientUserPageParam clientUserPageParam);

    /**
     * 添加用户
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    ClientUser add(ClientUserAddParam clientUserAddParam, String sourceFromType);

    /**
     * 编辑用户
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(ClientUserEditParam clientUserEditParam);

    /**
     * 删除用户
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<ClientUserIdParam> clientUserIdParamList);

    /**
     * 获取用户详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    ClientUser detail(ClientUserIdParam clientUserIdParam);

    /**
     * 禁用用户
     *
     * @author yubaoshan
     * @date 2026/3/30 23:50
     */
    void disableUser(ClientUserIdParam clientUserIdParam);

    /**
     * 启用用户
     *
     * @author yubaoshan
     * @date 2026/3/30 23:50
     */
    void enableUser(ClientUserIdParam clientUserIdParam);

    /**
     * 获取用户详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    ClientUser queryEntity(String id);

    /**
     * 获取图片验证码
     *
     * @author jetox
     * @date 2021/12/28 14:46
     **/
    ClientUserPicValidCodeResult getPicCaptcha();

    /**
     * 找回密码获取手机验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String findPasswordGetPhoneValidCode(ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam);

    /**
     * 找回密码获取邮箱验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String findPasswordGetEmailValidCode(ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam);

    /**
     * 通过手机号找回用户密码
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    void findPasswordByPhone(ClientUserFindPwdByPhoneParam clientUserFindPwdByPhoneParam);

    /**
     * 通过邮箱找回用户密码
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    void findPasswordByEmail(ClientUserFindPwdByEmailParam clientUserFindPwdByEmailParam);

    /**
     * 修改密码获取手机验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String updatePasswordGetPhoneValidCode(ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam);

    /**
     * 修改密码获取邮箱验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String updatePasswordGetEmailValidCode(ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam);

    /**
     * 通过验证旧密码修改用户密码
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    void updatePasswordByOld(ClientUserUpdatePwdByOldParam clientUserUpdatePwdByOldParam);

    /**
     * 通过验证手机号修改用户密码
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    void updatePasswordByPhone(ClientUserUpdatePwdByPhoneParam clientUserUpdatePwdByPhoneParam);

    /**
     * 通过验证邮箱修改用户密码
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    void updatePasswordByEmail(ClientUserUpdatePwdByEmailParam clientUserUpdatePwdByEmailParam);

    /**
     * 绑定手机号获取手机验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String bindPhoneGetPhoneValidCode(ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam);

    /**
     * 修改绑定手机号获取手机验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String updateBindPhoneGetPhoneValidCode(ClientUserGetPhoneValidCodeParam clientUserGetPhoneValidCodeParam);

    /**
     * 绑定手机号
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    void bindPhone(ClientUserBindPhoneParam clientUserBindPhoneParam);

    /**
     * 绑定邮箱获取邮箱验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String bindEmailGetEmailValidCode(ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam);

    /**
     * 修改绑定邮箱获取邮箱验证码
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    String updateBindEmailGetEmailValidCode(ClientUserGetEmailValidCodeParam clientUserGetEmailValidCodeParam);

    /**
     * 绑定邮箱
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    void bindEmail(ClientUserBindEmailParam clientUserBindEmailParam);

    /**
     * 修改用户头像返回base64
     *
     * @author jetox
     * @date 2022/4/22 15:53
     **/
    String updateAvatar(MultipartFile file);

    /**
     * 编辑个人信息
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void updateUserInfo(ClientUserUpdateInfoParam clientUserUpdateInfoParam);

    /**
     * 根据id获取头像
     *
     * @author jetox
     * @date 2023/8/28 10:10
     **/
    String getAvatarById(ClientUserIdParam clientUserIdParam);

    /**
     * 根据手机号创建用户
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    ClientUser createUserWithPhone(String phone);

    /**
     * 根据邮箱创建用户
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    ClientUser createUserWithEmail(String email);

    /**
     * 根据账号密码和名称创建用户
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    ClientUser createUserWithAccount(String account, String password, String name);

    /**
     * 判断当前用户密码是否过期
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    Boolean isUserPasswordExpired();

    /**
     * 判断当前用户是否需要绑定手机号
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    Boolean isUserNeedBindPhone();

    /**
     * 判断当前用户是否需要绑定邮箱
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    Boolean isUserNeedBindEmail();

    /**
     * 通知用户密码即将到期
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    void noticeUserPasswordAboutToExpired();

    /**
     * 执行注册
     *
     * @author jetox
     * @date 2022/8/25 15:16
     **/
    void doRegister(String account, String password);

    /**
     * 获取用户扩展信息，没有则创建
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    ClientUserExt getOrCreateClientUserExt(String userId);
}
