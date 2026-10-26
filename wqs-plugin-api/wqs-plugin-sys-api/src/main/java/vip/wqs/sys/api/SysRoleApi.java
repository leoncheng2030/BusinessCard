
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 角色API
 *
 * @author jetox
 * @date 2022/6/6 11:36
 **/
public interface SysRoleApi {

    /**
     * 判断组织下是否存在角色
     *
     * @author jetox
     * @date 2022/8/2 11:16
     */
    boolean orgHasRole(List<String> orgIdList);

    /**
     * 获取角色选择器
     *
     * @author jetox
     * @date 2022/7/22 14:49
     **/
    Page<JSONObject> roleSelector(String orgId, String category, String searchKey, List<String> dataScopeList, boolean excludeSuperAdmin);

    /**
     * 代码生成菜单按钮授权
     *
     * @author jetox
     * @date 2022/11/1 15:58
     **/
    void grantForGenMenuAndButton(String menuId);

    /**
     * 代码生成权限授权
     *
     * @author jetox
     * @date 2022/11/1 15:58
     **/
    void grantForGenPermission(String moduleName, String busName, String genType);

    /**
     * 获取资源授权树
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<JSONObject> resourceTreeSelector();

    /**
     * 获取权限授权树
     *
     * @param saasFilter 是否SAAS过滤（YES时仅返回可授权使用的接口，套餐授权权限树用）
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<String> permissionTreeSelector(String saasFilter);
}
