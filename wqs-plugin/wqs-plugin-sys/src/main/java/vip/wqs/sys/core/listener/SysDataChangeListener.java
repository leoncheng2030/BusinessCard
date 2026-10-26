
package vip.wqs.sys.core.listener;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.auth.core.util.StpLoginUserUtil;
import vip.wqs.common.listener.CommonDataChangeListener;
import vip.wqs.sys.core.enums.SysDataTypeEnum;
import vip.wqs.sys.modular.org.service.SysOrgService;
import vip.wqs.sys.modular.org.service.SysUserDataScopeService;

import java.util.Collections;
import java.util.List;

/**
 * 系统模块数据变化侦听器
 *
 * @author jetox
 * @date 2023/3/3 10:44
 **/
@Component
public class SysDataChangeListener implements CommonDataChangeListener {

    @Resource
    private SysOrgService sysOrgService;

    @Resource
    private SysUserDataScopeService sysUserDataScopeService;

    @Override
    public void doAddWithDataId(String dataType, String dataId) {
        if(dataType.equals(SysDataTypeEnum.ORG.getValue())) {
            List<String> dataIdList = Collections.singletonList(dataId);
            // 清除机构缓存
            sysOrgService.clearOrgCache();
            // 将新机构加入到当前登录用户的数据范围缓存
            SaBaseLoginUser saBaseLoginUser = StpLoginUserUtil.getLoginUser();
            saBaseLoginUser.getDataScopeList().forEach(dataScope -> dataScope.getDataScope().addAll(dataIdList));
            saBaseLoginUser.setDataScopeList(saBaseLoginUser.getDataScopeList());
            // 重新缓存当前登录用户信息
            StpUtil.getTokenSession().set("loginUser", saBaseLoginUser);
            // 同步更新预计算表：为当前用户的所有API追加新机构
            sysUserDataScopeService.appendOrgIdsForUser(saBaseLoginUser.getId(), dataIdList);
        }
    }

    @Override
    public void doAddWithDataIdList(String dataType, List<String> dataIdList) {
        if(dataType.equals(SysDataTypeEnum.ORG.getValue())) {
            // 清除机构缓存
            sysOrgService.clearOrgCache();
            // 将新机构加入到当前登录用户的数据范围缓存
            SaBaseLoginUser saBaseLoginUser = StpLoginUserUtil.getLoginUser();
            saBaseLoginUser.getDataScopeList().forEach(dataScope -> dataScope.getDataScope().addAll(dataIdList));
            saBaseLoginUser.setDataScopeList(saBaseLoginUser.getDataScopeList());
            // 重新缓存当前登录用户信息
            StpUtil.getTokenSession().set("loginUser", saBaseLoginUser);
            // 同步更新预计算表：为当前用户的所有API追加新机构
            sysUserDataScopeService.appendOrgIdsForUser(saBaseLoginUser.getId(), dataIdList);
        }
    }

    @Override
    public void doAddWithData(String dataType, JSONObject jsonObject) {
        // 此处可做额外处理
    }

    @Override
    public void doAddWithDataList(String dataType, JSONArray jsonArray) {
        // 此处可做额外处理
    }

    @Override
    public void doUpdateWithDataId(String dataType, String dataId) {
        if(dataType.equals(SysDataTypeEnum.ORG.getValue())) {
            sysOrgService.clearOrgCache();
        }
    }

    @Override
    public void doUpdateWithDataIdList(String dataType, List<String> dataIdList) {
        if(dataType.equals(SysDataTypeEnum.ORG.getValue())) {
            sysOrgService.clearOrgCache();
        }
    }

    @Override
    public void doUpdateWithData(String dataType, JSONObject jsonObject) {
        // 此处可做额外处理
    }

    @Override
    public void doUpdateWithDataList(String dataType, JSONArray jsonArray) {
        // 此处可做额外处理
    }

    @Override
    public void doDeleteWithDataId(String dataType, String dataId) {
        // 此处可做额外处理
    }

    @Override
    public void doDeleteWithDataIdList(String dataType, List<String> dataIdList) {
        if(dataType.equals(SysDataTypeEnum.ORG.getValue())) {
            sysOrgService.clearOrgCache();
            // 精准删除预计算表中对应的机构记录
            sysUserDataScopeService.deleteByOrgIds(dataIdList);
        }
    }
}
