
package vip.wqs.auth.core.util;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollectionUtil;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.common.util.CommonServletUtil;

import java.util.List;

/**
 * B端登录用户工具类
 *
 * @author jetox
 * @date 2022/7/8 10:40
 **/
public class StpLoginUserUtil {

    /**
     * 获取当前B端登录用户
     *
     * @author jetox
     * @date 2022/7/8 10:41
     **/
    public static SaBaseLoginUser getLoginUser() {
        return (SaBaseLoginUser) StpUtil.getTokenSession().get("loginUser");
    }

    /**
     * 获取当前B端登录用户的当前请求接口的数据范围
     * 返回null表示SCOPE_ALL（全部数据权限，无需过滤）
     * 返回空列表表示无数据权限（需限制）
     * 返回非空列表表示按机构ID过滤
     *
     * @author jetox
     * @date 2022/7/8 10:41
     **/
    public static List<String> getLoginUserDataScope() {
        List<String> resultList = CollectionUtil.newArrayList();
        String servletPath = CommonServletUtil.getRequest().getServletPath();
        for (SaBaseLoginUser.DataScope dataScope : getLoginUser().getDataScopeList()) {
            if (dataScope.getApiUrl().equals(servletPath)) {
                if (dataScope.isScopeAll()) {
                    // SCOPE_ALL：返回null表示全部数据权限，无需过滤
                    return null;
                }
                resultList.addAll(dataScope.getDataScope());
            }
        }
        return resultList;
    }
}
