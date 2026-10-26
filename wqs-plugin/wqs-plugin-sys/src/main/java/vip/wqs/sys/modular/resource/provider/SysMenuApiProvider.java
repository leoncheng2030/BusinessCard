
package vip.wqs.sys.modular.resource.provider;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysMenuApi;
import vip.wqs.sys.modular.resource.entity.SysMenu;
import vip.wqs.sys.modular.resource.param.menu.SysMenuSelectorMenuParam;
import vip.wqs.sys.modular.resource.service.SysMenuService;

import java.util.List;

/**
 * 菜单API接口实现类
 *
 * @author jetox
 * @date 2022/11/1 13:50
 **/
@Service
public class SysMenuApiProvider implements SysMenuApi {

    @Resource
    private SysMenuService sysMenuService;

    @Override
    public String addForGenMenu(String parentId, String busName, String module, String title, String path) {
        return sysMenuService.addForGenMenu(parentId, busName, title, module, path);
    }

    @Override
    public List<Tree<String>> menuTreeSelector(String module) {
        SysMenuSelectorMenuParam param = new SysMenuSelectorMenuParam();
        param.setModule(module);
        return sysMenuService.menuTreeSelector(param);
    }

    @Override
    public JSONObject queryEntity(String id) {
        SysMenu sysMenu = sysMenuService.getById(id);
        if(ObjectUtil.isNotEmpty(sysMenu)) {
            return JSONUtil.parseObj(sysMenu);
        }
        return null;
    }

    @Override
    public void addResourceGrantSuperAdmin(String moduleId, String id, String title, String parentId, String name, String code,  String category,
                                           String menuType, String path, String component, String icon, String visible,  Integer sortCode) {
        sysMenuService.addResourceGrantSuperAdmin(moduleId, id, title, parentId, name, code, category, menuType, path, component, icon, visible, sortCode);
    }
}
