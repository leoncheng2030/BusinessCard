
package vip.wqs.sys.modular.role.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.org.param.SysOrgSelectorTreeParam;
import vip.wqs.sys.modular.resource.entity.SysMenu;
import vip.wqs.sys.modular.role.entity.SysRole;
import vip.wqs.sys.modular.role.param.*;
import vip.wqs.sys.modular.role.result.*;
import vip.wqs.sys.modular.user.entity.SysUser;

import java.util.List;

/**
 * 角色Service接口
 *
 * @author jetox
 * @date 2022/4/21 18:35
 **/
public interface SysRoleService extends IService<SysRole> {

    /**
     * 获取角色分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<SysRole> page(SysRolePageParam sysRolePageParam);

    /**
     * 添加角色
     *
     * @author jetox
     * @date 2022/4/24 20:48
     */
    void add(SysRoleAddParam sysRoleAddParam);

    /**
     * 编辑角色
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(SysRoleEditParam sysRoleEditParam);

    /**
     * 删除角色
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<SysRoleIdParam> sysRoleIdParamList);

    /**
     * 获取角色详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    SysRole detail(SysRoleIdParam sysRoleIdParam);

    /**
     * 获取角色详情
     *
     * @author jetox
     * @date 2022/7/25 19:42
     **/
    SysRole queryEntity(String id);

    /**
     * 获取角色拥有资源
     *
     * @author jetox
     * @date 2022/5/13 20:51
     */
    SysRoleOwnResourceResult ownResource(SysRoleIdParam SysRoleIdParam);

    /**
     * 给角色授权资源
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void grantResource(SysRoleGrantResourceParam sysRoleGrantResourceParam);

    /**
     * 获取角色拥有移动端菜单
     *
     * @author jetox
     * @date 2022/5/13 20:51
     */
    SysRoleOwnMobileMenuResult ownMobileMenu(SysRoleIdParam sysRoleIdParam);

    /**
     * 给角色授权移动端菜单
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void grantMobileMenu(SysRoleGrantMobileMenuParam sysRoleGrantMobileMenuParam);

    /**
     * 获取角色拥有权限
     *
     * @author jetox
     * @date 2022/5/13 20:51
     */
    SysRoleOwnPermissionResult ownPermission(SysRoleIdParam SysRoleIdParam);

    /**
     * 给角色授权权限
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void grantPermission(SysRoleGrantPermissionParam sysRoleGrantPermissionParam);

    /**
     * 给角色授权权限（追加方式）
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void grantPermissionWithAppend(SysRoleGrantPermissionParam sysRoleGrantPermissionParam);

    /**
     * 获取角色下的用户
     *
     * @author jetox
     * @date 2022/8/22 13:56
     **/
    List<String> ownUser(SysRoleIdParam sysRoleIdParam);

    /**
     * 给角色授权用户
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void grantUser(SysRoleGrantUserParam sysRoleGrantUserParam);

    /**
     * 刷新拥有该角色的所有在线用户的权限缓存
     *
     * @author jetox
     * @date 2022/4/29 10:12
     **/
    void refreshRoleLoginUserCache(String roleId);

    /* ====角色部分所需要用到的选择器==== */

    /**
     * 获取组织树选择器（懒加载）
     *
     * @author jetox
     * @date 2022/4/24 20:00
     */
    List<JSONObject> orgTreeSelector(SysOrgSelectorTreeParam sysOrgSelectorTreeParam);

    /**
     * 获取资源授权树
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<SysRoleGrantResourceTreeResult> resourceTreeSelector();
    
    /**
     * 获取资源授权树（saasFilter=YES 时仅返回可授权使用的资源）
     *
     * @author wqs
     * @date 2026/9/3 20:00
     */
    List<SysRoleGrantResourceTreeResult> resourceTreeSelector(String saasFilter);

    /**
     * 获取资源授权树
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<SysRoleGrantResourceTreeResult> resourceTreeSelector(List<SysMenu> originDataList);

    /**
     * 获取移动端菜单授权树
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<SysRoleGrantMobileMenuTreeResult> mobileMenuTreeSelector();

    /**
     * 获取移动端菜单授权树
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<SysRoleGrantMobileMenuTreeResult> mobileMenuTreeSelector(List<JSONObject> originDataList);

    /**
     * 获取权限授权树
     *
     * @param saasFilter 是否SAAS过滤（YES时仅返回可授权使用的接口，套餐授权权限树用）
     * @author jetox
     * @date 2022/4/24 20:08
     */
    List<String> permissionTreeSelector(String saasFilter);

    /**
     * 获取角色选择器
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<SysRole> roleSelector(SysRoleSelectorRoleParam sysRoleSelectorRoleParam);

    /**
     * 获取用户选择器
     *
     * @author jetox
     * @date 2022/8/22 13:39
     **/
    Page<SysUser> userSelector(SysRoleSelectorUserParam sysRoleSelectorUserParam);
}
