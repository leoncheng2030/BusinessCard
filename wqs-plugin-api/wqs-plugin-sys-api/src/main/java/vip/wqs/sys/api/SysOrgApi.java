
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 组织API
 *
 * @author jetox
 * @date 2022/6/6 11:35
 **/
public interface SysOrgApi {

    /**
     * 根据id获取名称
     *
     * @author jetox
     * @date 2022/8/4 10:12
     **/
    String getNameById(String orgId);

    /**
     * 根据组织id获取部门主管id
     *
     * @author jetox
     * @date 2022/6/6 14:50
     **/
    String getSupervisorIdByOrgId(String orgId);

    /**
     * 获取组织树选择器（懒加载）
     *
     * @author jetox
     * @date 2022/7/22 14:46
     **/
    List<JSONObject> orgTreeSelector(String parentId, String searchKey);

    /**
     * 获取组织列表选择器
     *
     * @author jetox
     * @date 2022/7/22 14:45
     **/
    Page<JSONObject> orgListSelector(String parentId, String searchKey);

    /**
     * 获取某组织的所有父级id集合
     *
     * @author yubaoshan
     * @date 2025/5/10 12:13
     */
    List<String> getParentIdListByOrgId(String orgId);

    /**
     * 根据组织id获取组织列表
     *
     * @author wangshuo
     * @date 2025/01/10 14:45
     **/
    List<JSONObject> getOrgListByIdListWithoutException(List<String> orgIdList);

    /**
     * 清除组织缓存
     *
     * @author yubaoshan
     * @date 2026/2/12
     **/
    void clearOrgCache();

    /**
     * 根据组织id获取其及所有下级组织id列表
     *
     * @author yubaoshan
     * @date 2026/3/23 14:00
     **/
    List<String> getChildOrgIdListById(String orgId);

    /**
     * 根据orgId列表获取祖先路径节点（用于懒加载树回显）
     *
     * @author jetox
     * @date 2026/4/22
     **/
    List<JSONObject> getAncestorNodes(List<String> orgIdList);
}
