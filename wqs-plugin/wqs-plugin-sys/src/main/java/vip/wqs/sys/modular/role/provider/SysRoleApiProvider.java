
package vip.wqs.sys.modular.role.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.sys.api.SysRoleApi;
import vip.wqs.sys.core.enums.SysBuildInEnum;
import vip.wqs.sys.modular.relation.entity.SysRelation;
import vip.wqs.sys.modular.relation.enums.SysRelationCategoryEnum;
import vip.wqs.sys.modular.relation.service.SysRelationService;
import vip.wqs.sys.modular.resource.entity.SysButton;
import vip.wqs.sys.modular.resource.entity.SysMenu;
import vip.wqs.sys.modular.resource.enums.SysResourceCategoryEnum;
import vip.wqs.sys.modular.resource.service.SysButtonService;
import vip.wqs.sys.modular.resource.service.SysMenuService;
import vip.wqs.sys.modular.role.entity.SysRole;
import vip.wqs.sys.modular.role.param.SysRoleGrantPermissionParam;
import vip.wqs.sys.modular.role.param.SysRoleGrantResourceParam;
import vip.wqs.sys.modular.role.param.SysRoleSelectorRoleParam;
import vip.wqs.sys.modular.role.service.SysRoleService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色API接口提供者
 *
 * @author jetox
 * @date 2022/7/22 14:57
 **/
@Service
public class SysRoleApiProvider implements SysRoleApi {

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysButtonService sysButtonService;

    @Resource
    private SysRelationService sysRelationService;

    @Override
    public boolean orgHasRole(List<String> orgIdList) {
        return sysRoleService.count(new LambdaQueryWrapper<SysRole>().in(SysRole::getOrgId, orgIdList)) > 0;
    }

    @SuppressWarnings("ALL")
    @Override
    public Page<JSONObject> roleSelector(String orgId, String category, String searchKey, List<String> dataScopeList, boolean excludeSuperAdmin) {
        SysRoleSelectorRoleParam sysRoleSelectorRoleParam = new SysRoleSelectorRoleParam();
        sysRoleSelectorRoleParam.setOrgId(orgId);
        sysRoleSelectorRoleParam.setCategory(category);
        sysRoleSelectorRoleParam.setSearchKey(searchKey);
        sysRoleSelectorRoleParam.setDataScopeList(dataScopeList);
        sysRoleSelectorRoleParam.setExcludeSuperAdmin(excludeSuperAdmin);
        return BeanUtil.toBean(sysRoleService.roleSelector(sysRoleSelectorRoleParam), Page.class);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grantForGenMenuAndButton(String menuId) {
        String superAdminRoleId = sysRoleService.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, SysBuildInEnum.BUILD_IN_ROLE_CODE.getValue())).getId();
        SysRoleGrantResourceParam sysRoleGrantResourceParam = new SysRoleGrantResourceParam();
        sysRoleGrantResourceParam.setId(superAdminRoleId);
        SysMenu sysMenu = sysMenuService.queryEntity(menuId);
        SysRoleGrantResourceParam.SysRoleGrantResource sysRoleGrantResource = new SysRoleGrantResourceParam.SysRoleGrantResource();
        sysRoleGrantResource.setMenuId(sysMenu.getId());
        List<String> buttonIdList = sysButtonService.list(new LambdaQueryWrapper<SysButton>().eq(SysButton::getParentId,
                sysMenu.getId()).eq(SysButton::getCategory, SysResourceCategoryEnum.BUTTON.getValue())).stream()
                .map(SysButton::getId).collect(Collectors.toList());
        sysRoleGrantResource.setButtonInfo(buttonIdList);
        sysRoleGrantResourceParam.setGrantInfoList(CollectionUtil.newArrayList(sysRoleGrantResource));

        List<String> menuIdList = sysRoleGrantResourceParam.getGrantInfoList().stream()
                .map(SysRoleGrantResourceParam.SysRoleGrantResource::getMenuId).collect(Collectors.toList());
        List<String> extJsonList = sysRoleGrantResourceParam.getGrantInfoList().stream()
                .map(JSONUtil::toJsonStr).collect(Collectors.toList());

        List<String> existMenuIdList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>().eq(SysMenu::getCategory,
                SysResourceCategoryEnum.MENU.getValue())).stream().map(SysMenu::getId).collect(Collectors.toList());
        if(ObjectUtil.isNotEmpty(existMenuIdList)) {
            sysRelationService.remove(new LambdaQueryWrapper<SysRelation>().eq(SysRelation::getObjectId, superAdminRoleId)
                    .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue()).notIn(SysRelation::getTargetId, existMenuIdList));
        }
        sysRelationService.saveRelationBatchWithAppend(superAdminRoleId, menuIdList, SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue(), extJsonList);

        // 刷新拥有该角色的所有在线用户的权限缓存
        sysRoleService.refreshRoleLoginUserCache(superAdminRoleId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void grantForGenPermission(String moduleName, String busName, String genType) {
        String superAdminRoleId = sysRoleService.getOne(new LambdaQueryWrapper<SysRole>()
                .eq(SysRole::getCode, SysBuildInEnum.BUILD_IN_ROLE_CODE.getValue())).getId();
        List<String> apiUrls = CollectionUtil.newArrayList(
                "/" + moduleName + "/" + busName + "/page",
                "/" + moduleName + "/" + busName + "/add",
                "/" + moduleName + "/" + busName + "/edit",
                "/" + moduleName + "/" + busName + "/delete",
                "/" + moduleName + "/" + busName + "/detail",
                "/" + moduleName + "/" + busName + "/downloadImportTemplate",
                "/" + moduleName + "/" + busName + "/importData",
                "/" + moduleName + "/" + busName + "/exportData"
        );
        if("TREE".equals(genType) || "LEFT_TREE_TABLE".equals(genType)) {
            apiUrls.add("/" + moduleName + "/" + busName + "/tree");
        }
        if("MASTER_DETAIL".equals(genType) || "LEFT_TREE_TABLE".equals(genType)) {
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/page");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/add");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/edit");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/delete");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/detail");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/downloadImportTemplate");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/importData");
            apiUrls.add("/" + moduleName + "/" + busName + "/sub/exportData");
        }
        List<SysRoleGrantPermissionParam.SysRoleGrantPermission> grantInfoList = apiUrls.stream().map(apiUrl -> {
            SysRoleGrantPermissionParam.SysRoleGrantPermission sysRoleGrantPermission = new SysRoleGrantPermissionParam.SysRoleGrantPermission();
            sysRoleGrantPermission.setApiUrl(apiUrl);
            sysRoleGrantPermission.setScopeCategory("ALL");
            sysRoleGrantPermission.setScopeDefineOrgIdList(CollectionUtil.newArrayList());
            return sysRoleGrantPermission;
        }).collect(Collectors.toList());

        SysRoleGrantPermissionParam sysRoleGrantPermissionParam = new SysRoleGrantPermissionParam();
        sysRoleGrantPermissionParam.setId(superAdminRoleId);
        sysRoleGrantPermissionParam.setGrantInfoList(grantInfoList);
        sysRoleService.grantPermissionWithAppend(sysRoleGrantPermissionParam);
    }

    @Override
    public List<JSONObject> resourceTreeSelector() {
        return sysRoleService.resourceTreeSelector().stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public List<String> permissionTreeSelector(String saasFilter) {
        return sysRoleService.permissionTreeSelector(saasFilter);
    }
}
