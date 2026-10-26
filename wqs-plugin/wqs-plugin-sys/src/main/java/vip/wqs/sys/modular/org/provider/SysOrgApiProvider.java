
package vip.wqs.sys.modular.org.provider;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysOrgApi;
import vip.wqs.sys.modular.org.entity.SysOrg;
import vip.wqs.sys.modular.org.param.SysOrgSelectorOrgListParam;
import vip.wqs.sys.modular.org.param.SysOrgSelectorTreeParam;
import vip.wqs.sys.modular.org.service.SysOrgService;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 组织API接口提供者
 *
 * @author jetox
 * @date 2022/7/22 14:56
 **/
@Service
public class SysOrgApiProvider implements SysOrgApi {

    @Resource
    private SysOrgService sysOrgService;

    @Override
    public String getNameById(String orgId) {
        return sysOrgService.queryEntity(orgId).getName();
    }

    @Override
    public String getSupervisorIdByOrgId(String orgId) {
        SysOrg sysOrg = sysOrgService.getById(orgId);
        if(ObjectUtil.isNotEmpty(sysOrg)) {
            return sysOrg.getDirectorId();
        }
        return null;
    }

    @Override
    public List<JSONObject> orgTreeSelector(String parentId, String searchKey) {
        SysOrgSelectorTreeParam sysOrgSelectorTreeParam = new SysOrgSelectorTreeParam();
        sysOrgSelectorTreeParam.setParentId(parentId);
        sysOrgSelectorTreeParam.setSearchKey(searchKey);
        return sysOrgService.orgTreeSelector(sysOrgSelectorTreeParam);
    }

    @SuppressWarnings("ALL")
    @Override
    public Page<JSONObject> orgListSelector(String parentId, String searchKey) {
        SysOrgSelectorOrgListParam sysOrgSelectorOrgListParam = new SysOrgSelectorOrgListParam();
        sysOrgSelectorOrgListParam.setParentId(parentId);
        sysOrgSelectorOrgListParam.setSearchKey(searchKey);
        return BeanUtil.toBean(sysOrgService.orgListSelector(sysOrgSelectorOrgListParam), Page.class);
    }

    @Override
    public List<String> getParentIdListByOrgId(String orgId) {
        return sysOrgService.getParentIdListByOrgId(orgId);
    }

    @Override
    public List<JSONObject> getOrgListByIdListWithoutException(List<String> orgIdList) {
        return sysOrgService.listByIds(orgIdList).stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public void clearOrgCache() {
        sysOrgService.clearOrgCache();
    }

    @Override
    public List<String> getChildOrgIdListById(String orgId) {
        return sysOrgService.getChildListById(sysOrgService.getAllOrgList(), orgId, true)
                .stream().map(SysOrg::getId).collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> getAncestorNodes(List<String> orgIdList) {
        return sysOrgService.getAncestorNodes(orgIdList);
    }
}
