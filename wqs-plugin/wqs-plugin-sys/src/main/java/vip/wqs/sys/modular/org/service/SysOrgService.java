
package vip.wqs.sys.modular.org.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.org.param.*;
import vip.wqs.sys.modular.user.entity.SysUser;

import java.util.List;

/**
 * 组织Service接口
 *
 * @author jetox
 * @date 2022/4/21 18:35
 **/
public interface SysOrgService extends IService<SysOrg> {

    /**
     * 获取组织分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<SysOrg> page(SysOrgPageParam sysOrgPageParam);

    /**
     * 添加组织
     *
     * @author jetox
     * @date 2022/4/24 20:48
     */
    void add(SysOrgAddParam sysOrgAddParam, String sourceFromType);

    /**
     * 编辑组织
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(SysOrgEditParam sysOrgEditParam);

    /**
     * 删除组织
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<SysOrgIdParam> sysOrgIdParamList);

    /**
     * 获取组织详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    SysOrg detail(SysOrgIdParam sysOrgIdParam);

    /**
     * 复制组织
     *
     * @author yubaoshan
     * @date 2025/12/24 01:10
     */
    void copy(SysOrgCopyParam sysOrgCopyParam);

    /**
     * 获取组织详情
     *
     * @author jetox
     * @date 2022/7/25 19:42
     **/
    SysOrg queryEntity(String id);

    /**
     * 获取所有组织
     *
     * @author jetox
     * @date 2022/7/25 19:42
     **/
    List<SysOrg> getAllOrgList();

    /**
     * 获取所有组织ID列表（从缓存获取，避免每次stream转换）
     *
     * @author yubaoshan
     * @date 2026/2/12
     **/
    List<String> getAllOrgIdList();

    /**
     * 根据组织全名称获取组织id，有则返回，无则创建
     *
     * @author jetox
     * @date 2023/3/7 15:44
     **/
    String getOrgIdByOrgFullNameWithCreate(String orgFullName);

    /**
     * 根据id获取父子数据列表
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    List<SysOrg> getParentAndChildListById(List<SysOrg> originDataList, String id, boolean includeSelf);

    /**
     * 根据id获取所有的子数据列表
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    List<SysOrg> getChildListById(List<SysOrg> originDataList, String id, boolean includeSelf);

    /**
     * 根据id获取所有的父数据列表
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    List<SysOrg> getParentListById(List<SysOrg> originDataList, String id, boolean includeSelf);

    /**
     * 根据id获取数据
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    SysOrg getById(List<SysOrg> originDataList, String id);

    /**
     * 根据id获取父数据
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    SysOrg getParentById(List<SysOrg> originDataList, String id);

    /**
     * 根据id获取子数据
     *
     * @author jetox
     * @date 2022/8/15 14:55
     **/
    SysOrg getChildById(List<SysOrg> originDataList, String id);

    /**
     * 获取组织树选择器（懒加载），支持搜索，搜索为空字符串走全量
     *
     * @author jetox
     * @date 2022/4/21 16:13
     **/
    List<JSONObject> orgTreeSelector(SysOrgSelectorTreeParam sysOrgSelectorTreeParam);

    /**
     * 获取组织列表选择器
     *
     * @author jetox
     * @date 2022/7/22 13:34
     **/
    Page<SysOrg> orgListSelector(SysOrgSelectorOrgListParam sysOrgSelectorOrgListParam);

    /**
     * 获取用户选择器
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<SysUser> userSelector(SysOrgSelectorUserParam sysOrgSelectorUserParam);

    /**
     * 获取某组织的所有父级id集合
     *
     * @author yubaoshan
     * @date 2025/5/10 12:13
     */
    List<String> getParentIdListByOrgId(String orgId);

    /**
     * 清除组织缓存（本地内存缓存 + Redis缓存）
     * 当其他模块修改了SYS_ORG表数据时，需要调用此方法同步清除缓存
     *
     * @author yubaoshan
     * @date 2026/2/12
     */
    void clearOrgCache();

    /**
     * 根据orgId列表获取其祖先路径节点（扁平列表，含节点自身及所有祖先）
     * 用于懒加载树的回显场景：需要将选中节点的祖先链路全部展开
     *
     * @author yubaoshan
     * @date 2026/3/22
     */
    List<JSONObject> getAncestorNodes(List<String> orgIdList);
}
