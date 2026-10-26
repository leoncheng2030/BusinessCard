
package vip.wqs.sys.modular.user.provider;

import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import vip.wqs.sys.provider.SysTransferResourceProvider;
import vip.wqs.sys.modular.relation.entity.SysRelation;
import vip.wqs.sys.modular.relation.enums.SysRelationCategoryEnum;
import vip.wqs.sys.modular.relation.service.SysRelationService;
import vip.wqs.sys.modular.role.entity.SysRole;
import vip.wqs.sys.modular.role.service.SysRoleService;
import vip.wqs.common.enums.CommonTransferModeEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色资源转移提供者
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Component
public class SysUserRoleTransferProvider implements SysTransferResourceProvider {

    @Resource
    private SysRelationService sysRelationService;

    @Resource
    private SysRoleService sysRoleService;

    @Override
    public String getResourceType() {
        return "ROLE";
    }

    @Override
    public String getResourceTypeName() {
        return "角色";
    }

    @Override
    public int getOrder() {
        return 20;
    }

    @Override
    public List<JSONObject> getDetailColumns() {
        return List.of(JSONUtil.createObj().set("title", "角色名称").set("dataIndex", "name"));
    }

    @Override
    public long getCount(String userId) {
        List<String> roleIdList = sysRelationService.getRelationTargetIdListByObjectIdAndCategory(
                userId, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        return roleIdList.size();
    }

    @Override
    public List<JSONObject> getDetailList(String userId) {
        List<String> roleIdList = sysRelationService.getRelationTargetIdListByObjectIdAndCategory(
                userId, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        if (ObjectUtil.isEmpty(roleIdList)) {
            return new ArrayList<>();
        }
        List<SysRole> roles = sysRoleService.listByIds(roleIdList);
        return roles.stream().map(role -> {
            JSONObject obj = new JSONObject();
            obj.set("id", role.getId());
            obj.set("name", role.getName());
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public void executeTransfer(String sourceUserId, String targetUserId, String transferMode,
                                boolean transferAll, List<String> selectedIds) {
        List<SysRelation> sourceRoleRelations = sysRelationService.getRelationListByObjectIdAndCategory(
                sourceUserId, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        if (ObjectUtil.isEmpty(sourceRoleRelations)) {
            return;
        }
        if (!transferAll && ObjectUtil.isNotEmpty(selectedIds)) {
            sourceRoleRelations = sourceRoleRelations.stream()
                    .filter(r -> selectedIds.contains(r.getTargetId()))
                    .collect(Collectors.toList());
        }
        if (ObjectUtil.isEmpty(sourceRoleRelations)) {
            return;
        }
        List<String> roleIdList = CollStreamUtil.toList(sourceRoleRelations, SysRelation::getTargetId);

        if (CommonTransferModeEnum.TRANSFER.getValue().equals(transferMode)) {
            sysRelationService.remove(new LambdaQueryWrapper<SysRelation>()
                    .eq(SysRelation::getObjectId, sourceUserId)
                    .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue())
                    .in(SysRelation::getTargetId, roleIdList));
            appendRolesToUser(targetUserId, roleIdList);
        } else if (CommonTransferModeEnum.COPY.getValue().equals(transferMode)) {
            appendRolesToUser(targetUserId, roleIdList);
        } else if (CommonTransferModeEnum.DELETE.getValue().equals(transferMode)) {
            sysRelationService.remove(new LambdaQueryWrapper<SysRelation>()
                    .eq(SysRelation::getObjectId, sourceUserId)
                    .eq(SysRelation::getCategory, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue())
                    .in(SysRelation::getTargetId, roleIdList));
        }
    }

    private void appendRolesToUser(String userId, List<String> roleIdList) {
        List<String> existingRoleIds = sysRelationService.getRelationTargetIdListByObjectIdAndCategory(
                userId, SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
        List<String> newRoleIds = roleIdList.stream()
                .filter(id -> !existingRoleIds.contains(id))
                .collect(Collectors.toList());
        if (ObjectUtil.isNotEmpty(newRoleIds)) {
            List<SysRelation> newRelations = newRoleIds.stream().map(roleId -> {
                SysRelation sysRelation = new SysRelation();
                sysRelation.setObjectId(userId);
                sysRelation.setTargetId(roleId);
                sysRelation.setCategory(SysRelationCategoryEnum.SYS_USER_HAS_ROLE.getValue());
                return sysRelation;
            }).collect(Collectors.toList());
            sysRelationService.saveBatch(newRelations);
        }
    }
}
