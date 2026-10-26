
package vip.wqs.sys.modular.group.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.listener.CommonDataChangeEventCenter;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.sys.core.enums.SysDataTypeEnum;
import vip.wqs.sys.modular.group.entity.SysGroup;
import vip.wqs.sys.modular.group.mapper.SysGroupMapper;
import vip.wqs.sys.modular.group.param.*;
import vip.wqs.sys.modular.group.service.SysGroupService;
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.org.param.SysOrgSelectorTreeParam;
import vip.wqs.sys.modular.org.service.SysOrgService;
import vip.wqs.sys.modular.relation.entity.SysRelation;
import vip.wqs.sys.modular.relation.enums.SysRelationCategoryEnum;
import vip.wqs.sys.modular.relation.service.SysRelationService;
import vip.wqs.sys.modular.user.entity.SysUser;
import vip.wqs.sys.modular.user.enums.SysUserStatusEnum;
import vip.wqs.sys.modular.user.service.SysUserService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户组Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class SysGroupServiceImpl extends ServiceImpl<SysGroupMapper, SysGroup> implements SysGroupService {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public Page<SysGroup> page(SysGroupPageParam sysGroupPageParam) {
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection();
        if(ObjectUtil.isNotEmpty(sysGroupPageParam.getSearchKey())) {
            queryWrapper.lambda().like(SysGroup::getName, sysGroupPageParam.getSearchKey());
        }
        if(ObjectUtil.isNotEmpty(sysGroupPageParam.getName())) {
            queryWrapper.lambda().like(SysGroup::getName, sysGroupPageParam.getName());
        }
        if(ObjectUtil.isAllNotEmpty(sysGroupPageParam.getSortField(), sysGroupPageParam.getSortOrder())) {
            CommonSortOrderEnum.validate(sysGroupPageParam.getSortOrder());
            queryWrapper.orderBy(true, sysGroupPageParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(sysGroupPageParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(SysGroup::getSortCode);
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void add(SysGroupAddParam sysGroupAddParam) {
        SysGroup sysGroup = BeanUtil.toBean(sysGroupAddParam, SysGroup.class);
        this.save(sysGroup);
        // 发布增加事件
        CommonDataChangeEventCenter.doAddWithData(SysDataTypeEnum.GROUP.getValue(), JSONUtil.createArray().put(sysGroup));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(SysGroupEditParam sysGroupEditParam) {
        SysGroup sysGroup = this.queryEntity(sysGroupEditParam.getId());
        BeanUtil.copyProperties(sysGroupEditParam, sysGroup);
        this.updateById(sysGroup);
        // 发布更新事件
        CommonDataChangeEventCenter.doUpdateWithData(SysDataTypeEnum.GROUP.getValue(), JSONUtil.createArray().put(sysGroup));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<SysGroupIdParam> sysGroupIdParamList) {
        List<String> groupIdList = CollStreamUtil.toList(sysGroupIdParamList, SysGroupIdParam::getId);
        // 执行删除
        this.removeByIds(groupIdList);
        // 发布删除事件
        CommonDataChangeEventCenter.doDeleteWithDataIdList(SysDataTypeEnum.GROUP.getValue(), groupIdList);
    }

    @Override
    public SysGroup detail(SysGroupIdParam sysGroupIdParam) {
        return this.queryEntity(sysGroupIdParam.getId());
    }

    @Override
    public SysGroup queryEntity(String id) {
        SysGroup sysGroup = this.getById(id);
        if(ObjectUtil.isEmpty(sysGroup)) {
            throw new CommonException("用户组不存在，id值为：{}", id);
        }
        return sysGroup;
    }

    @Override
    public List<String> ownUser(SysGroupIdParam sysGroupIdParam) {
        return sysRelationService.getRelationObjectIdListByTargetIdAndCategory(sysGroupIdParam.getId(),
                SysRelationCategoryEnum.SYS_USER_HAS_GROUP.getValue());
    }

    @Override
    public List<JSONObject> orgTreeSelector(SysOrgSelectorTreeParam sysOrgSelectorTreeParam) {
        return sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam);
    }

    @Override
    public Page<SysUser> userSelector(SysGroupSelectorUserParam sysGroupSelectorUserParam) {
        QueryWrapper<SysUser> queryWrapper = new QueryWrapper<SysUser>().checkSqlInjection();
        // 只查询状态为正常的
        queryWrapper.lambda().eq(SysUser::getUserStatus, SysUserStatusEnum.ENABLE.getValue());
        // 只查询部分字段
        queryWrapper.lambda().select(SysUser::getId, SysUser::getAvatar, SysUser::getOrgId, SysUser::getPositionId, SysUser::getAccount,
                SysUser::getName, SysUser::getSortCode, SysUser::getGender, SysUser::getEntryDate);
        // 如果查询条件为空，则直接查询
        if(ObjectUtil.isAllEmpty(sysGroupSelectorUserParam.getOrgId(), sysGroupSelectorUserParam.getSearchKey())) {
            return sysUserService.getAllUserSelectorList();
        } else {
            if (ObjectUtil.isNotEmpty(sysGroupSelectorUserParam.getOrgId())) {
                // 如果组织id不为空，则查询该组织及其子极其子下的所有人
                List<String> childOrgIdList = CollStreamUtil.toList(sysOrgService.getChildListById(sysOrgService
                        .getAllOrgList(), sysGroupSelectorUserParam.getOrgId(), true), SysOrg::getId);
                if (ObjectUtil.isNotEmpty(childOrgIdList)) {
                    queryWrapper.lambda().in(SysUser::getOrgId, childOrgIdList);
                } else {
                    return new Page<>();
                }
            }
            if (ObjectUtil.isNotEmpty(sysGroupSelectorUserParam.getSearchKey())) {
                queryWrapper.lambda().like(SysUser::getName, sysGroupSelectorUserParam.getSearchKey());
            }
            queryWrapper.lambda().orderByAsc(SysUser::getSortCode);
            return sysUserService.page(CommonPageRequest.defaultPage(), queryWrapper.lambda());
        }
    }

    @Override
    public void grantUser(SysGroupGrantUserParam sysGroupGrantUserParam) {
        String id = sysGroupGrantUserParam.getId();
        List<String> grantInfoList = sysGroupGrantUserParam.getGrantInfoList();
        sysRelationService.remove(new LambdaQueryWrapper<SysRelation>().eq(SysRelation::getTargetId, id)
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_USER_HAS_GROUP.getValue()));
        sysRelationService.saveBatch(grantInfoList.stream().map(userId -> {
            SysRelation sysRelation = new SysRelation();
            sysRelation.setObjectId(userId);
            sysRelation.setTargetId(id);
            sysRelation.setCategory(SysRelationCategoryEnum.SYS_USER_HAS_GROUP.getValue());
            return sysRelation;
        }).collect(Collectors.toList()));
    }

    @Override
    public Page<SysGroup> groupSelector(SysGroupSelectorParam sysGroupSelectorParam) {
        QueryWrapper<SysGroup> queryWrapper = new QueryWrapper<SysGroup>().checkSqlInjection();
        // 只查询部分字段，排掉extJson
        queryWrapper.lambda().select(SysGroup::getId, SysGroup::getName, SysGroup::getSortCode, SysGroup::getRemark);
        queryWrapper.lambda().orderByAsc(SysGroup::getSortCode);
        // 如果查询条件为空，则直接查询
        if (ObjectUtil.isNotEmpty(sysGroupSelectorParam.getSearchKey())) {
            queryWrapper.lambda().like(SysGroup::getName, sysGroupSelectorParam.getSearchKey());
        }
        return this.page(CommonPageRequest.defaultPage(), queryWrapper.lambda());
    }
}
