
package vip.wqs.client.modular.user.provider;

import cn.dev33.satoken.session.SaSession;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import vip.wqs.auth.api.SaBaseLoginUserApi;
import vip.wqs.auth.core.pojo.SaBaseClientLoginUser;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.auth.core.util.StpClientUtil;
import vip.wqs.client.modular.user.entity.ClientUser;
import vip.wqs.client.modular.user.result.ClientLoginUser;
import vip.wqs.client.modular.user.service.ClientUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * C端登录用户API接口实现类
 *
 * @author jetox
 * @date 2022/4/29 13:36
 **/
@Slf4j
@Service("clientLoginUserApi")
public class ClientLoginUserApiProvider implements SaBaseLoginUserApi {

    @Resource
    private ClientUserService clientUserService;

    /**
     * 不实现B端用户信息
     *
     * @author jetox
     * @date 2022/7/8 10:36
     **/
    @Override
    public SaBaseLoginUser getUserById(String id) {
        return null;
    }

    /**
     * 根据id获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2021/12/28 15:35
     **/
    @Override
    public SaBaseClientLoginUser getClientUserById(String id) {
        return clientUserService.getUserById(id);
    }

    /**
     * 不实现B端用户信息
     *
     * @author jetox
     * @date 2022/7/8 10:36
     **/
    @Override
    public SaBaseLoginUser getUserByAccount(String account) {
        return null;
    }

    /**
     * 根据账号获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2021/12/28 15:35
     **/
    @Override
    public ClientLoginUser getClientUserByAccount(String account) {
        return clientUserService.getUserByAccount(account);
    }

    /**
     * 不实现B端用户信息
     *
     * @author jetox
     * @date 2022/8/25 14:08
     **/
    @Override
    public SaBaseLoginUser getUserByPhone(String phone) {
        return null;
    }

    @Override
    public SaBaseLoginUser getUserByEmail(String email) {
        return null;
    }

    /**
     * 根据手机号获取C端用户信息，查不到则返回null
     *
     * @author jetox
     * @date 2022/8/25 14:08
     **/
    @Override
    public SaBaseClientLoginUser getClientUserByPhone(String phone) {
        return clientUserService.getUserByPhone(phone);
    }

    @Override
    public SaBaseClientLoginUser getClientUserByEmail(String email) {
        return clientUserService.getUserByEmail(email);
    }

    /**
     * 根据用户id获取用户集合
     *
     * @author jetox
     * @date 2022/4/27 22:53
     */
    @Override
    public List<JSONObject> listUserByUserIdList(List<String> userIdList) {
        return clientUserService.listByIds(userIdList).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    /**
     * 根据用户id获取角色集合
     *
     * @author jetox
     * @date 2022/4/27 22:53
     */
    @Override
    public List<JSONObject> getRoleListByUserId(String userId) {
        // TODO C端用户暂无角色
        return CollectionUtil.newArrayList();
    }

    /**
     * 根据角色id和用户id集合获取按钮码集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    @Override
    public List<String> getButtonCodeListListByUserAndRoleIdList(List<String> userAndRoleIdList) {
        // TODO C端用户暂无按钮码
        return CollectionUtil.newArrayList();
    }

    /**
     * 根据角色id和用户id集合获取移动端按钮码集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    @Override
    public List<String> getMobileButtonCodeListListByUserIdAndRoleIdList(List<String> userAndRoleIdList) {
        // TODO C端用户暂无移动端按钮码
        return CollectionUtil.newArrayList();
    }

    /**
     * 根据角色id和用户id集合获取权限集合
     *
     * @author jetox
     * @date 2022/4/27 22:54
     */
    @Override
    public List<JSONObject> getPermissionListByUserIdAndRoleIdList(List<String> userAndRoleIdList, String orgId) {
        // TODO C端用户暂无权限码
        return CollectionUtil.newArrayList();
    }

    @Override
    public SaBaseLoginUser createUserWithPhone(String phone) {
        return null;
    }

    @Override
    public SaBaseClientLoginUser createClientUserWithPhone(String phone) {
        ClientUser clientUser = clientUserService.createUserWithPhone(phone);
        return BeanUtil.copyProperties(clientUser, ClientLoginUser.class);
    }

    @Override
    public SaBaseLoginUser createUserWithEmail(String email) {
        // C端用户API不实现B端用户创建
        throw new UnsupportedOperationException("C端用户API不支持创建B端用户");
    }

    @Override
    public SaBaseClientLoginUser createClientUserWithEmail(String email) {
        ClientUser clientUser = clientUserService.createUserWithEmail(email);
        return BeanUtil.copyProperties(clientUser, ClientLoginUser.class);
    }

    @Override
    public SaBaseLoginUser createUserWithAccount(String account, String password, String name) {
        // C端用户API不实现B端用户创建
        throw new UnsupportedOperationException("C端用户API不支持创建B端用户");
    }

    @Override
    public SaBaseClientLoginUser createClientUserWithAccount(String account, String password, String name) {
        ClientUser clientUser = clientUserService.createUserWithAccount(account, password, name);
        return BeanUtil.copyProperties(clientUser, ClientLoginUser.class);
    }

    @Override
    public void doRegister(String account, String password) {
        clientUserService.doRegister(account, password);
    }

    /**
     * C端用户无数据范围，不实现
     *
     * @author yubaoshan
     * @date 2026/2/12
     **/
    @Override
    public void refreshUserDataScope(String userId, List<SaBaseLoginUser.DataScope> dataScopeList) {
        // C端用户无数据范围，无需刷新预计算表
    }

    @Override
    public void refreshOnlineUserPermission(String userId) {
        // 获取该用户所有在线token
        List<String> tokenList = StpClientUtil.getTokenValueListByLoginId(userId);
        if (ObjectUtil.isEmpty(tokenList)) {
            return;
        }
        // 获取用户基本信息
        SaBaseClientLoginUser saBaseClientLoginUser = this.getClientUserById(userId);
        if (ObjectUtil.isEmpty(saBaseClientLoginUser)) {
            return;
        }
        // 写入该用户的所有TokenSession
        for (String token : tokenList) {
            try {
                SaSession session = StpClientUtil.getTokenSessionByToken(token);
                if (session != null) {
                    session.set("loginUser", saBaseClientLoginUser);
                }
            } catch (Exception e) {
                log.warn(">>> 刷新C端用户权限缓存时跳过无效token，userId：{}，token：{}", userId, token);
            }
        }
        log.info(">>> 已刷新C端在线用户权限缓存，userId：{}，token数：{}", userId, tokenList.size());
    }
}
