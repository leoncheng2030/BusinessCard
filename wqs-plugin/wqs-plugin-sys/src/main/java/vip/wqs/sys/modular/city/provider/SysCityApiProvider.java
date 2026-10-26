
package vip.wqs.sys.modular.city.provider;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysCityApi;
import vip.wqs.sys.modular.city.service.SysCityService;

import java.util.Collection;
import java.util.Map;

/**
 * 系统城市API接口提供者
 *
 * @author wqs
 * @date 2026/9/3
 **/
@Service
public class SysCityApiProvider implements SysCityApi {

    @Resource
    private SysCityService sysCityService;

    @Override
    public Map<Integer, String> getCityNameMapByIdList(Collection<Integer> cityIdList) {
        return sysCityService.getCityNameMapByIdList(cityIdList);
    }
}
