
package vip.wqs.sys.modular.user.provider;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import vip.wqs.sys.provider.SysTransferResourceProvider;
import vip.wqs.common.enums.CommonTransferModeEnum;
import vip.wqs.sys.modular.user.entity.SysUser;
import vip.wqs.sys.modular.user.service.SysUserService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 下属资源转移提供者
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Component
public class SysUserSubordinateTransferProvider implements SysTransferResourceProvider {

    @Resource
    private SysUserService sysUserService;

    @Override
    public String getResourceType() {
        return "SUBORDINATE";
    }

    @Override
    public String getResourceTypeName() {
        return "下属";
    }

    @Override
    public int getOrder() {
        return 10;
    }

    @Override
    public List<JSONObject> getDetailColumns() {
        return Arrays.asList(
                JSONUtil.createObj().set("title", "姓名").set("dataIndex", "name"),
                JSONUtil.createObj().set("title", "账号").set("dataIndex", "account")
        );
    }

    @Override
    public long getCount(String userId) {
        return sysUserService.count(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDirectorId, userId));
    }

    @Override
    public List<JSONObject> getDetailList(String userId) {
        List<SysUser> subordinates = sysUserService.list(new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDirectorId, userId)
                .select(SysUser::getId, SysUser::getName, SysUser::getAccount, SysUser::getAvatar));
        return subordinates.stream().map(user -> {
            JSONObject obj = new JSONObject();
            obj.set("id", user.getId());
            obj.set("name", user.getName());
            obj.set("account", user.getAccount());
            obj.set("avatar", user.getAvatar());
            return obj;
        }).collect(Collectors.toList());
    }

    @Override
    public void executeTransfer(String sourceUserId, String targetUserId, String transferMode,
                                boolean transferAll, List<String> selectedIds) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<SysUser>()
                .eq(SysUser::getDirectorId, sourceUserId);
        if (!transferAll && ObjectUtil.isNotEmpty(selectedIds)) {
            queryWrapper.in(SysUser::getId, selectedIds);
        }
        List<SysUser> subordinates = sysUserService.list(queryWrapper);
        if (ObjectUtil.isEmpty(subordinates)) {
            return;
        }
        if (CommonTransferModeEnum.DELETE.getValue().equals(transferMode)) {
            subordinates.forEach(user -> user.setDirectorId(null));
        } else {
            subordinates.forEach(user -> user.setDirectorId(targetUserId));
        }
        sysUserService.updateBatchById(subordinates);
    }
}
