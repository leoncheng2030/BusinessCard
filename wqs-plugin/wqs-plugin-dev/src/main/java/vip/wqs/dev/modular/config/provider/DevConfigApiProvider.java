
package vip.wqs.dev.modular.config.provider;

import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.dev.api.DevConfigApi;
import vip.wqs.dev.modular.config.service.DevConfigService;

/**
 * 配置API接口实现类
 *
 * @author jetox
 * @date 2022/6/17 14:43
 **/
@Service
public class DevConfigApiProvider implements DevConfigApi {

    @Resource
    private DevConfigService devConfigService;

    @Override
    public String getValueByKey(String key) {
        return devConfigService.getValueByKey(key);
    }
}
