
package vip.wqs.sys.api;

import java.util.Collection;
import java.util.Map;

/**
 * 城市API
 *
 * @author wqs
 * @date 2026/9/3
 **/
public interface SysCityApi {

    /**
     * 根据cityId集合批量获取城市名称Map（cityId → name，不存在的cityId不返回）
     *
     * @author wqs
     * @date 2026/9/3
     **/
    Map<Integer, String> getCityNameMapByIdList(Collection<Integer> cityIdList);
}
