
package vip.wqs.mobile.modular.resource.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.mobile.modular.resource.entity.MobileModule;
import vip.wqs.mobile.modular.resource.param.module.MobileModuleAddParam;
import vip.wqs.mobile.modular.resource.param.module.MobileModuleEditParam;
import vip.wqs.mobile.modular.resource.param.module.MobileModuleIdParam;
import vip.wqs.mobile.modular.resource.param.module.MobileModulePageParam;

import java.util.List;

/**
 * 移动端模块Service接口
 *
 * @author jetox
 * @date 2022/6/27 14:03
 **/
public interface MobileModuleService extends IService<MobileModule> {

    /**
     * 获取移动端模块分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<MobileModule> page(MobileModulePageParam mobileModulePageParam);

    /**
     * 添加移动端模块
     *
     * @author jetox
     * @date 2022/4/24 20:48
     */
    void add(MobileModuleAddParam mobileModuleAddParam);

    /**
     * 编辑移动端模块
     *
     * @author jetox
     * @date 2022/4/24 21:13
     */
    void edit(MobileModuleEditParam mobileModuleEditParam);

    /**
     * 删除移动端模块
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    void delete(List<MobileModuleIdParam> mobileModuleIdParamList);

    /**
     * 获取移动端模块详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    MobileModule detail(MobileModuleIdParam mobileModuleIdParam);

    /**
     * 获取移动端模块详情
     *
     * @author jetox
     * @date 2022/4/24 21:18
     */
    MobileModule queryEntity(String id);

    /**
     * 获取移动端模块选择器
     *
     * @author 每天一点
     * @date 2023/7/15 21:52
     */
    List<JSONObject> mobileModuleSelector();
}
