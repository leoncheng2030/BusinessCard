
package vip.wqs.sys.modular.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.exception.CommonException;
import vip.wqs.sys.api.SysTransferApi;
import vip.wqs.sys.provider.SysTransferResourceProvider;
import vip.wqs.common.enums.CommonTransferModeEnum;
import vip.wqs.sys.modular.org.param.SysOrgSelectorTreeParam;
import vip.wqs.sys.modular.org.service.SysOrgService;
import vip.wqs.sys.modular.user.entity.SysUser;
import vip.wqs.sys.modular.user.param.SysUserSelectorUserParam;
import vip.wqs.sys.modular.user.param.SysUserTransferExecuteParam;
import vip.wqs.sys.modular.user.param.SysUserTransferItemParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceDetailParam;
import vip.wqs.sys.modular.user.param.SysUserTransferResourceListParam;
import vip.wqs.sys.modular.user.result.SysUserTransferResourceResult;
import vip.wqs.sys.modular.user.service.SysUserService;
import vip.wqs.sys.modular.user.service.SysUserTransferService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 权限转移Service实现类
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
@Service
public class SysUserTransferServiceImpl implements SysUserTransferService, SysTransferApi {

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private List<SysTransferResourceProvider> transferResourceProviders;

    @Override
    public List<SysUserTransferResourceResult> resourceList(SysUserTransferResourceListParam sysUserTransferResourceListParam) {
        String sourceUserId = sysUserTransferResourceListParam.getSourceUserId();
        SysUser sysUser = sysUserService.getById(sourceUserId);
        if (ObjectUtil.isEmpty(sysUser)) {
            throw new CommonException("用户不存在，id值为：{}", sourceUserId);
        }
        List<SysUserTransferResourceResult> resultList = new ArrayList<>();
        List<SysTransferResourceProvider> sortedProviders = transferResourceProviders.stream()
                .sorted(Comparator.comparingInt(SysTransferResourceProvider::getOrder))
                .toList();
        for (SysTransferResourceProvider provider : sortedProviders) {
            SysUserTransferResourceResult result = new SysUserTransferResourceResult();
            result.setResourceType(provider.getResourceType());
            result.setResourceTypeName(provider.getResourceTypeName());
            try {
                result.setTotalCount(provider.getCount(sourceUserId));
            } catch (Exception e) {
                result.setTotalCount(0L);
            }
            if (result.getTotalCount() <= 0) {
                continue;
            }
            result.setDetailColumns(provider.getDetailColumns());
            resultList.add(result);
        }
        return resultList;
    }

    @Override
    public List<JSONObject> resourceDetail(SysUserTransferResourceDetailParam sysUserTransferResourceDetailParam) {
        String sourceUserId = sysUserTransferResourceDetailParam.getSourceUserId();
        String resourceType = sysUserTransferResourceDetailParam.getResourceType();
        SysTransferResourceProvider provider = getProviderByType(resourceType);
        return provider.getDetailList(sourceUserId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void execute(SysUserTransferExecuteParam sysUserTransferExecuteParam) {
        String sourceUserId = sysUserTransferExecuteParam.getSourceUserId();
        String targetUserId = sysUserTransferExecuteParam.getTargetUserId();
        String transferMode = sysUserTransferExecuteParam.getTransferMode();
        CommonTransferModeEnum.validate(transferMode);

        if (!CommonTransferModeEnum.DELETE.getValue().equals(transferMode)) {
            if (ObjectUtil.isEmpty(targetUserId)) {
                throw new CommonException("转移和复制模式下目标用户不能为空");
            }
            if (sourceUserId.equals(targetUserId)) {
                throw new CommonException("被调整人和目标人不能是同一个人");
            }
            SysUser targetUser = sysUserService.getById(targetUserId);
            if (ObjectUtil.isEmpty(targetUser)) {
                throw new CommonException("目标用户不存在");
            }
        }

        SysUser sourceUser = sysUserService.getById(sourceUserId);
        if (ObjectUtil.isEmpty(sourceUser)) {
            throw new CommonException("被调整用户不存在");
        }

        for (SysUserTransferItemParam item : sysUserTransferExecuteParam.getTransferItems()) {
            SysTransferResourceProvider provider = getProviderByType(item.getResourceType());
            provider.executeTransfer(sourceUserId, targetUserId, transferMode,
                    Boolean.TRUE.equals(item.getTransferAll()),
                    item.getSelectedIds() != null ? item.getSelectedIds() : new ArrayList<>());
        }
    }

    private SysTransferResourceProvider getProviderByType(String resourceType) {
        return transferResourceProviders.stream()
                .filter(p -> p.getResourceType().equals(resourceType))
                .findFirst()
                .orElseThrow(() -> new CommonException("不支持的资源类型：{}", resourceType));
    }

    @Override
    public List<JSONObject> getTransferResourceList(String sourceUserId) {
        SysUserTransferResourceListParam param = new SysUserTransferResourceListParam();
        param.setSourceUserId(sourceUserId);
        return resourceList(param).stream().map(r -> JSONUtil.parseObj(JSONUtil.toJsonStr(r))).toList();
    }

    @Override
    public List<JSONObject> getTransferResourceDetail(String sourceUserId, String resourceType) {
        SysUserTransferResourceDetailParam param = new SysUserTransferResourceDetailParam();
        param.setSourceUserId(sourceUserId);
        param.setResourceType(resourceType);
        return resourceDetail(param);
    }

    @Override
    public void executeTransfer(JSONObject executeParam) {
        SysUserTransferExecuteParam param = BeanUtil.toBean(executeParam, SysUserTransferExecuteParam.class);
        execute(param);
    }

    @Override
    public List<JSONObject> getTransferOrgTreeSelector(JSONObject param) {
        SysOrgSelectorTreeParam sysOrgSelectorTreeParam = BeanUtil.toBean(param, SysOrgSelectorTreeParam.class);
        return sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam);
    }

    @Override
    public Page<JSONObject> getTransferUserSelector(JSONObject param) {
        SysUserSelectorUserParam sysUserSelectorUserParam = BeanUtil.toBean(param, SysUserSelectorUserParam.class);
        Page<SysUser> page = sysUserService.userSelector(sysUserSelectorUserParam);
        Page<JSONObject> result = new Page<>(page.getCurrent(), page.getSize(), page.getTotal());
        result.setRecords(page.getRecords().stream().map(u -> JSONUtil.parseObj(JSONUtil.toJsonStr(u))).toList());
        return result;
    }
}
