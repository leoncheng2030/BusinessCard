
package vip.wqs.sys.modular.resource.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.resource.entity.SysModule;
import vip.wqs.sys.modular.resource.param.module.SysModuleAddParam;
import vip.wqs.sys.modular.resource.param.module.SysModuleEditParam;
import vip.wqs.sys.modular.resource.param.module.SysModuleIdParam;
import vip.wqs.sys.modular.resource.param.module.SysModulePageParam;

import java.util.List;

/**
 * 模块Service接口
 *
 * @author jetox
 * @date 2022/6/27 14:03
 **/
public interface SysModuleService extends IService<SysModule> {

    /**
     * 获取模块分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<SysModule> page(SysModulePageParam sysModulePageParam);

    /**
     * 添加模块
     *
     * @author jetox
     * @date 2022/4/24 20:48
     */
    void add(SysModuleAddParam sysModuleAddParam);

    /**
     * 编辑模块
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(SysModuleEditParam sysModuleEditParam);

    /**
     * 删除模块
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<SysModuleIdParam> sysModuleIdParamList);

    /**
     * 获取所有模块
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     */
    List<JSONObject> moduleSelector();

    /**
     * 获取模块详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    SysModule detail(SysModuleIdParam sysModuleIdParam);

    /**
     * 获取模块详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    SysModule queryEntity(String id);

    /**
     * 删除模块以及所有的资源
     *
     * @author yubaoshan
     * @date 2025/5/5 04:30
     */
    void deleteModuleByCode(String moduleCode, List<String> resourceIdList);
}
