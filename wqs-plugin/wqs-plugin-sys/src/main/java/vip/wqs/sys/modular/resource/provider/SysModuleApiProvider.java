
package vip.wqs.sys.modular.resource.provider;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.sys.api.SysModuleApi;
import vip.wqs.sys.modular.resource.param.module.SysModuleAddParam;
import vip.wqs.sys.modular.resource.service.SysModuleService;

import java.util.List;

/**
 * 模块API接口实现类
 *
 * @author yubaoshan
 * @date 2024/9/6 01:24
 **/
@Service
public class SysModuleApiProvider implements SysModuleApi {

    @Resource
    private SysModuleService sysModuleService;

    @Override
    public List<JSONObject> moduleSelector() {
        return sysModuleService.moduleSelector();
    }

    @Override
    public void addModule(String id, String title, String code, String icon, String color) {
        SysModuleAddParam param = new SysModuleAddParam();
        param.setId(id);
        param.setTitle(title);
        param.setCode(code);
        param.setIcon(icon);
        param.setColor(color);
        sysModuleService.add(param);
    }

    @Override
    public void deleteModuleByCode(String moduleCode, List<String> resourceIdList) {
        sysModuleService.deleteModuleByCode(moduleCode, resourceIdList);
    }
}
