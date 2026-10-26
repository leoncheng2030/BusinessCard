
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 用户Api
 *
 * @author jetox
 * @date 2022/6/6 11:33
 **/
public interface SysUserApi {

    /**
     * 根据用户id获取用户对象，没有则返回null
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    JSONObject getUserByIdWithoutException(String userId);

    /**
     * 根据用户id获取用户对象列表，没有的则为空，都没有则返回空集合
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    List<JSONObject> getUserListByIdListWithoutException(List<String> userIdList);

    /**
     * 根据用户id获取用户对象，没有则抛出异常
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    JSONObject getUserByIdWithException(String userId);

    /**
     * 根据用户id获取用户对象列表，只要有一个没有则抛出异常
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    List<JSONObject> getUserListByIdWithException(List<String> userIdList);

    /**
     * 获取用户拥有角色
     *
     * @author jetox
     * @date 2022/5/13 21:00
     */
    List<String> ownRole(String userId);

    /**
     * 给用户授权角色
     *
     * @author jetox
     * @date 2022/8/1 18:28
     */
    void grantRole(String userId, List<String> roleIdList);

    /**
     * 根据组织id集合获取组织下用户id集合（含兼职该机构的人）
     *
     * @author jetox
     * @date 2022/6/6 11:40
     **/
    List<String> getUserIdListByOrgIdList(String initiator, List<String> orgIdList);

    /**
     * 根据职位id集合获取职位下用户id集合（含兼职该职位的人）
     *
     * @author jetox
     * @date 2022/6/6 11:44
     **/
    List<String> getUserIdListByPositionIdList(String initiator, List<String> positionIdList);

    /**
     * 根据用户id和组织id和主管层级获取上级主管id
     *
     * @author jetox
     * @date 2022/6/6 14:50
     **/
    JSONObject getSupervisorIdBySupervisorLevel(String initiator, List<String> userIdList, String orgId, String supervisorLevel);

    /**
     * 根据用户id和组织id和终点主管层级获取上级主管id集合
     *
     * @author jetox
     * @date 2022/6/6 14:50
     **/
    List<String> getMulSupervisorIdListByEndLevel(String initiator, String orgId, String endLevel);

    /**
     * 获取用户选择器
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<JSONObject> userSelector(String orgId, String searchKey);

    /**
     * 获取用户列表（排除当前用户）
     *
     * @author chengchuanyao
     * @date 2024/7/19 9:54
     */
    List<JSONObject> listUserWithoutCurrent();

    /**
     * 获取用户的职位列表
     *
     * @author jetox
     * @date 2022/6/20 18:19
     **/
    List<JSONObject> getPositionListByUserId(String userId);

    /**
     * 获取用户扩展信息，没有则创建
     *
     * @author jetox
     * @date 2022/7/8 9:26
     **/
    JSONObject getOrCreateSysUserExt(String userId);
}
