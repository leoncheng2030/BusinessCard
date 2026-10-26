
package vip.wqs.sys.modular.resource.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.listener.CommonDataChangeEventCenter;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.sys.core.enums.SysBuildInEnum;
import vip.wqs.sys.core.enums.SysDataTypeEnum;
import vip.wqs.sys.modular.relation.entity.SysRelation;
import vip.wqs.sys.modular.relation.enums.SysRelationCategoryEnum;
import vip.wqs.sys.modular.relation.service.SysRelationService;
import vip.wqs.sys.modular.resource.entity.SysMenu;
import vip.wqs.sys.modular.resource.entity.SysModule;
import vip.wqs.sys.modular.resource.enums.SysResourceCategoryEnum;
import vip.wqs.sys.modular.resource.mapper.SysModuleMapper;
import vip.wqs.sys.modular.resource.param.module.SysModuleAddParam;
import vip.wqs.sys.modular.resource.param.module.SysModuleEditParam;
import vip.wqs.sys.modular.resource.param.module.SysModuleIdParam;
import vip.wqs.sys.modular.resource.param.module.SysModulePageParam;
import vip.wqs.sys.modular.resource.service.SysMenuService;
import vip.wqs.sys.modular.resource.service.SysModuleService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 模块Service接口实现类
 *
 * @author jetox
 * @date 2022/6/27 14:25
 **/
@Service
public class SysModuleServiceImpl extends ServiceImpl<SysModuleMapper, SysModule> implements SysModuleService {

    @Resource
    private SysMenuService sysMenuService;

    @Resource
    private SysRelationService sysRelationService;

    @Override
    public Page<SysModule> page(SysModulePageParam sysModulePageParam) {
        QueryWrapper<SysModule> queryWrapper = new QueryWrapper<SysModule>().checkSqlInjection();
        queryWrapper.lambda().eq(SysModule::getCategory, SysResourceCategoryEnum.MODULE.getValue());
        if(ObjectUtil.isNotEmpty(sysModulePageParam.getSearchKey())) {
            queryWrapper.lambda().like(SysModule::getTitle, sysModulePageParam.getSearchKey());
        }
        if(ObjectUtil.isAllNotEmpty(sysModulePageParam.getSortField(), sysModulePageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(sysModulePageParam.getSortOrder());
            queryWrapper.orderBy(true, sysModulePageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(sysModulePageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(SysModule::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysModuleAddParam sysModuleAddParam) {
        SysModule sysModule = BeanUtil.toBean(sysModuleAddParam, SysModule.class);
        boolean repeatTitle = this.count(new LambdaQueryWrapper<SysModule>().eq(SysModule::getCategory,
                SysResourceCategoryEnum.MODULE.getValue()).eq(SysModule::getTitle, sysModule.getTitle())) > 0;
        if(repeatTitle) {
            throw new CommonException("存在重复的模块，名称为：{}", sysModule.getTitle());
        }
        if(ObjectUtil.isEmpty(sysModule.getCode())) {
            sysModule.setCode(RandomUtil.randomString(10));
        }
        sysModule.setCategory(SysResourceCategoryEnum.MODULE.getValue());
        this.save(sysModule);
        // 发布增加事件
        CommonDataChangeEventCenter.doAddWithData(SysDataTypeEnum.RESOURCE.getValue(), JSONUtil.createArray().put(sysModule));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysModuleEditParam sysModuleEditParam) {
        SysModule sysModule = this.queryEntity(sysModuleEditParam.getId());
        BeanUtil.copyProperties(sysModuleEditParam, sysModule);
        boolean repeatTitle = this.count(new LambdaQueryWrapper<SysModule>().eq(SysModule::getCategory,
                SysResourceCategoryEnum.MODULE.getValue()).eq(SysModule::getTitle, sysModule.getTitle())
                .ne(SysModule::getId, sysModule.getId())) > 0;
        if(repeatTitle) {
            throw new CommonException("存在重复的模块，名称为：{}", sysModule.getTitle());
        }
        this.updateById(sysModule);

        // 发布更新事件
        CommonDataChangeEventCenter.doUpdateWithData(SysDataTypeEnum.RESOURCE.getValue(), JSONUtil.createArray().put(sysModule));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<SysModuleIdParam> sysModuleIdParamList) {
        List<String> sysModuleIdList = CollStreamUtil.toList(sysModuleIdParamList, SysModuleIdParam::getId);
        if(ObjectUtil.isNotEmpty(sysModuleIdList)) {
            boolean containsSystemModule = this.listByIds(sysModuleIdList).stream().map(SysModule::getCode)
                    .collect(Collectors.toSet()).contains(SysBuildInEnum.BUILD_IN_MODULE_CODE.getValue());
            if(containsSystemModule) {
                throw new CommonException("不可删除系统内置模块");
            }

            // 获取模块下的菜单、按钮
            List<SysMenu> allMenuList = sysMenuService.list(new LambdaQueryWrapper<SysMenu>()
                    .in(SysMenu::getCategory, CollectionUtil.newArrayList(SysResourceCategoryEnum.MENU.getValue(),
                            SysResourceCategoryEnum.BUTTON.getValue())));
            if(ObjectUtil.isNotEmpty(allMenuList)) {
                List<String> toDeleteMenuIdList = CollectionUtil.newArrayList(sysModuleIdList);
                allMenuList.stream().filter(sysMenu -> sysModuleIdList.contains(sysMenu.getModule()))
                        .collect(Collectors.toList()).forEach(sysMenu -> toDeleteMenuIdList
                        .addAll(sysMenuService.getChildListById(allMenuList, sysMenu.getId(), true).stream()
                                .map(SysMenu::getId).collect(Collectors.toList())));
                if(ObjectUtil.isNotEmpty(toDeleteMenuIdList)) {
                    // 清除对应的角色与资源信息
                    sysRelationService.remove(new LambdaUpdateWrapper<SysRelation>().in(SysRelation::getTargetId, toDeleteMenuIdList)
                            .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue()));
                    // 执行删除
                    this.removeByIds(toDeleteMenuIdList);

                    // 发布删除事件
                    CommonDataChangeEventCenter.doDeleteWithDataIdList(SysDataTypeEnum.RESOURCE.getValue(), toDeleteMenuIdList);
                }
            }
        }
    }

    @Override
    public List<JSONObject> moduleSelector() {
        LambdaQueryWrapper<SysModule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(SysModule::getId, SysModule::getTitle);
        lambdaQueryWrapper.eq(SysModule::getCategory, SysResourceCategoryEnum.MODULE.getValue());
        return this.list(lambdaQueryWrapper).stream()
                .map(item -> JSONUtil.createObj().set("id", item.getId()).set("name", item.getTitle()))
                .collect(Collectors.toList());
    }

    @Override
    public SysModule detail(SysModuleIdParam sysModuleIdParam) {
        return this.queryEntity(sysModuleIdParam.getId());
    }

    @Override
    public SysModule queryEntity(String id) {
        SysModule sysModule = this.getById(id);
        if(ObjectUtil.isEmpty(sysModule)) {
            throw new CommonException("模块不存在，id值为：{}", id);
        }
        return sysModule;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteModuleByCode(String moduleCode, List<String> resourceIdList) {
        SysModule module = this.getOne(new LambdaQueryWrapper<SysModule>().eq(SysModule::getCode, moduleCode));
        // 创建一个新的列表来存储所有需要删除的资源ID
        List<String> allDeleteResourceIds = new ArrayList<>(resourceIdList);

        if (ObjectUtil.isNotEmpty(module)) {
            String moduleId = module.getId();
            // 执行删除模块
            this.removeById(moduleId);
            // 将模块ID添加到删除列表中
            allDeleteResourceIds.add(moduleId);
        }

        // 删除资源
        sysMenuService.removeByIds(resourceIdList);
        // 清除对应的角色与资源信息
        sysRelationService.remove(new LambdaUpdateWrapper<SysRelation>().in(SysRelation::getTargetId, resourceIdList)
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue()));
        // 清除对应的用户与资源信息
        sysRelationService.remove(new LambdaUpdateWrapper<SysRelation>().in(SysRelation::getTargetId, resourceIdList)
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_RESOURCE.getValue()));

        // 发布删除事件，使用合并后的ID列表
        CommonDataChangeEventCenter.doDeleteWithDataIdList(SysDataTypeEnum.RESOURCE.getValue(), allDeleteResourceIds);
    }
}
