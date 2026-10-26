
package vip.wqs.auth.api;

import cn.hutool.json.JSONObject;
import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;

import java.util.List;

/**
 * 登录用户API，由其他模块实现
 *
 * @author jetox
 * @date 2021/12/23 21:48
 */
public interface SaBaseLoginUserApi {

    /**
     * 根据id获取B端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserById(String id);

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserById(String id);

    /**
     * 根据账号获取B端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByAccount(String account);

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByAccount(String account);

    /**
     * 根据手机号获取B端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByPhone(String phone);

    /**
     * 根据邮箱获取B端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser getUserByEmail(String email);

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByPhone(String phone);

    /**
     * 根据邮箱获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser getClientUserByEmail(String email);

    /**
     * 根据用户id获取用户集合
     *
     * @author jetox
     * @date 2022/4/27 22:53
     */
    List<JSONObject> listUserByUserIdList(List<String> userIdList);

    /**
     * 根据用户id获取角色集合
     *
     * @author jetox
     * @date 2022/4/27 22:53
     */
    List<JSONObject> getRoleListByUserId(String userId);

    /**
     * 根据角色id和用户id集合获取按钮码集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    List<String> getButtonCodeListListByUserAndRoleIdList(List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取移动端按钮码集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    List<String> getMobileButtonCodeListListByUserIdAndRoleIdList(List<String> userAndRoleIdList);

    /**
     * 根据角色id和用户id集合获取权限集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    List<JSONObject> getPermissionListByUserIdAndRoleIdList(List<String> userAndRoleIdList, String orgId);

    /**
     * 更新用户的登录时间和登录ip等信息
     *
     * @author jetox
     * @date 2022/4/27 22:57
     */
    default void updateUserLoginInfo(String userId, String device) {
        // 默认空实现：B端用户需记录登录轨迹，C端用户表已无登录轨迹列，无需记录
    }

    /**
     * 使用手机号创建B端用户
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser createUserWithPhone(String phone);

    /**
     * 使用手机号创建C端用户
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser createClientUserWithPhone(String phone);

    /**
     * 使用邮箱创建B端用户
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseLoginUser createUserWithEmail(String email);

    /**
     * 使用邮箱创建C端用户
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    SaBaseClientLoginUser createClientUserWithEmail(String email);

    /**
     * 使用账号、密码和名称创建B端用户
     *
     * @author yubaoshan
     * @date 2026/6/25
     **/
    SaBaseLoginUser createUserWithAccount(String account, String password, String name);

    /**
     * 使用账号、密码和名称创建C端用户
     *
     * @author yubaoshan
     * @date 2026/6/26
     **/
    SaBaseClientLoginUser createClientUserWithAccount(String account, String password, String name);

    /**
     * 执行注册
     *
     * @author jetox
     * @date 2022/3/10 16:14
     **/
    void doRegister(String account, String password);

    /**
     * 刷新用户数据范围预计算表
     *
     * @param userId 用户ID
     * @param dataScopeList 用户的数据范围集合（per-API维度）
     * @author yubaoshan
     * @date 2026/2/12
     */
    void refreshUserDataScope(String userId, List<SaBaseLoginUser.DataScope> dataScopeList);

    /**
     * 刷新在线用户的权限缓存（Session），权限变更后调用，确保实时生效。
     * 如果用户不在线则跳过。
     *
     * @param userId 用户ID
     * @author jetox
     * @date 2026/3/12
     */
    void refreshOnlineUserPermission(String userId);
}
