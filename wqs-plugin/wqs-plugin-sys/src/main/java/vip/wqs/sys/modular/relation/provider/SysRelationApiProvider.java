
package vip.wqs.sys.modular.relation.provider;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysRelationApi;
import vip.wqs.sys.modular.relation.entity.SysRelation;
import vip.wqs.sys.modular.relation.enums.SysRelationCategoryEnum;
import vip.wqs.sys.modular.relation.service.SysRelationService;
import vip.wqs.sys.modular.user.entity.SysUser;
import vip.wqs.sys.modular.user.enums.SysUserStatusEnum;
import vip.wqs.sys.modular.user.service.SysUserService;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 关系API接口实现类
 *
 * @author jetox
 * @date 2022/6/6 15:33
 **/
@Service
public class SysRelationApiProvider implements SysRelationApi {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysUserService sysUserService;

    @Override
    public List<String> getUserIdListByRoleIdList(String initiator, List<String> roleIdList) {
        List<String> userIdList = sysRelationService.getRelationObjectIdListByTargetIdListAndCategory(roleIdList,
                SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        if(ObjectUtil.isNotEmpty(userIdList)){
            return sysUserService.list(new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIdList)
                            .eq(SysUser::getUserStatus, SysUserStatusEnum.ENABLE.getValue()))
                    .stream().map(SysUser::getId).collect(Collectors.toList());
        } else {
            return CollectionUtil.newArrayList();
        }
    }

    @Override
    public List<String> getUserIdListByGroupIdList(String initiator, List<String> groupIdList) {
        List<String> userIdList = sysRelationService.getRelationObjectIdListByTargetIdListAndCategory(groupIdList,
                SysRelationCategoryEnum.SYS_USER_HAS_GROUP.getValue());
        if(ObjectUtil.isNotEmpty(userIdList)){
            return sysUserService.list(new LambdaQueryWrapper<SysUser>().in(SysUser::getId, userIdList)
                            .eq(SysUser::getUserStatus, SysUserStatusEnum.ENABLE.getValue()))
                    .stream().map(SysUser::getId).collect(Collectors.toList());
        } else {
            return CollectionUtil.newArrayList();
        }
    }

    @Override
    public List<String> getGroupIdListByUserIdList(List<String> userIdList) {
        return sysRelationService.getRelationTargetIdListByObjectIdListAndCategory(userIdList,
                SysRelationCategoryEnum.SYS_USER_HAS_GROUP.getValue());
    }

    @Override
    public void removeRoleHasMobileMenuRelation(List<String> targetIdList) {
        sysRelationService.remove(new LambdaQueryWrapper<SysRelation>().in(SysRelation::getTargetId, targetIdList)
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_MOBILE_MENU.getValue()));
    }

    @Override
    public void removeRoleHasMobileButtonRelation(List<String> targetIdList, List<String> buttonIdList) {
        sysRelationService.list(new LambdaQueryWrapper<SysRelation>().in(SysRelation::getTargetId, targetIdList)
                .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_ROLE_HAS_MOBILE_MENU.getValue())
                .isNotNull(SysRelation::getExtJson)).forEach(mobileRelation -> {
            JSONObject extJsonObject = JSONUtil.parseObj(mobileRelation.getExtJson());
            List<String> buttonInfoList = extJsonObject.getBeanList("buttonInfo", String.class);
            if (ObjectUtil.isNotEmpty(buttonInfoList)) {
                Set<String> intersectionDistinct = CollectionUtil.intersectionDistinct(buttonIdList, buttonInfoList);
                if(ObjectUtil.isNotEmpty(intersectionDistinct)) {
                    Collection<String> disjunction = CollectionUtil.disjunction(buttonInfoList, intersectionDistinct);
                    extJsonObject.set("buttonInfo", disjunction);
                }
            }
            // 清除对应的角色与移动端菜单信息中的【授权的移动端按钮信息】
            sysRelationService.update(new LambdaUpdateWrapper<SysRelation>().eq(SysRelation::getId, mobileRelation.getId())
                    .set(SysRelation::getExtJson, JSONUtil.toJsonStr(extJsonObject)));
        });
    }

    @Override
    public void saveRelationBatchWithClear(String objectId, List<String> targetIdList, String category, List<String> extJsonList) {
        sysRelationService.saveRelationBatchWithClear(objectId, targetIdList, category, extJsonList);
    }

    @Override
    public List<String> getRelationTargetIdListByObjectIdAndCategory(String objectId, String category) {
        return sysRelationService.getRelationTargetIdListByObjectIdAndCategory(objectId, category);
    }

    @Override
    public List<String> getRelationObjectIdListByTargetIdAndCategory(String targetId, String category) {
        return sysRelationService.getRelationObjectIdListByTargetIdAndCategory(targetId, category);
    }

    @Override
    public List<JSONObject> getRelationListByObjectIdAndCategory(String objectId, String category) {
        return sysRelationService.list(new LambdaQueryWrapper<SysRelation>().eq(SysRelation::getObjectId, objectId)
                .eq(SysRelation::getCategory, category)).stream().map(sysRelation -> {
            JSONObject jsonObject = new JSONObject();
            jsonObject.set("targetId", sysRelation.getTargetId());
            jsonObject.set("extJson", sysRelation.getExtJson());
            return jsonObject;
        }).collect(Collectors.toList());
    }
}
