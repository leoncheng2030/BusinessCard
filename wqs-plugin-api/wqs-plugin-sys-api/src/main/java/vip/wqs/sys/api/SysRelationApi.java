
package vip.wqs.sys.api;

import java.util.List;

/**
 * 关系API
 *
 * @author jetox
 * @date 2022/6/6 11:41
 **/
public interface SysRelationApi {

    /**
     * 根据角色id集合获取角色下用户id集合
     *
     * @author jetox
     * @date 2022/6/6 11:43
     **/
    List<String> getUserIdListByRoleIdList(String initiator, List<String> roleIdList);

    /**
     * 根据用户组id集合获取用户组下用户id集合
     *
     * @author jetox
     * @date 2022/6/6 11:43
     **/
    List<String> getUserIdListByGroupIdList(String initiator, List<String> groupIdList);

    /**
     * 根据用户id集合获取用户授权的用户组id集合
     *
     * @author jetox
     * @date 2022/6/6 11:43
     **/
    List<String> getGroupIdListByUserIdList(List<String> userIdList);

    /**
     * 根据移动端菜单Id集合移除角色和移动端菜单关系
     *
     * @author jetox
     * @date 2023/1/31 9:54
     **/
    void removeRoleHasMobileMenuRelation(List<String> targetIdList);

    /**
     * 清除对应的角色与移动端菜单信息中的【授权的移动端按钮信息】
     *
     * @author jetox
     * @date 2023/1/31 9:54
     **/
    void removeRoleHasMobileButtonRelation(List<String> targetIdList, List<String> buttonIdList);

    /**
     * 保存关系（清空重写）
     *
     * @author wqs
     * @date 2026/9/3 15:00
     **/
    void saveRelationBatchWithClear(String objectId, List<String> targetIdList, String category, List<String> extJsonList);

    /**
     * 根据对象id与分类获取目标id集合（已授权回显）
     *
     * @author wqs
     * @date 2026/9/3 15:00
     **/
    List<String> getRelationTargetIdListByObjectIdAndCategory(String objectId, String category);

    /**
     * 根据目标id与分类获取对象id集合
     *
     * @author wqs
     * @date 2026/9/3 15:00
     **/
    List<String> getRelationObjectIdListByTargetIdAndCategory(String targetId, String category);

    /**
     * 根据对象id与分类获取关系列表（含目标id与扩展信息）
     *
     * @author wqs
     * @date 2026/9/3 15:00
     **/
    List<cn.hutool.json.JSONObject> getRelationListByObjectIdAndCategory(String objectId, String category);
}
