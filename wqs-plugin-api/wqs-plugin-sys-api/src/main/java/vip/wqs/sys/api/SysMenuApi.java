
package vip.wqs.sys.api;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 菜单API
 *
 * @author jetox
 * @date 2022/11/1 13:44
 **/
public interface SysMenuApi {

    /**
     * 代码生成菜单插入
     *
     * @author jetox
     * @date 2022/11/1 13:48
     **/
    String addForGenMenu(String parentId, String busName, String module, String title, String path);

    /**
     * 获取所有菜单树包括未授权的
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     **/
    List<Tree<String>> menuTreeSelector(String module);

    /**
     * 通过ID获得菜单详情
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     **/
    JSONObject queryEntity(String id);

    /**
     * 增加资源并授权于超管
     *
     * @author yubaoshan
     * @date 2025/5/5 04:30
     */
    void addResourceGrantSuperAdmin(String moduleId, String id, String title, String parentId, String name, String code, String category,
                                    String menuType, String path, String component, String icon, String visible, Integer sortCode);

}
