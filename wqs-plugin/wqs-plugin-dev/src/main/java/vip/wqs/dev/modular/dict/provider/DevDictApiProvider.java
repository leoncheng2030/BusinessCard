
package vip.wqs.dev.modular.dict.provider;

import cn.hutool.json.JSONObject;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.dev.api.DevDictApi;
import vip.wqs.dev.modular.dict.service.DevDictService;

import java.util.List;

/**
 * 字典API接口实现类
 *
 * @author jetox
 * @date 2022/9/2 16:05
 */
@Service
public class DevDictApiProvider implements DevDictApi {

    @Resource
    private DevDictService devDictService;

    /**
     * 根据类型跟子类型获得翻译后的label
     *
     * @author yubaoshan
     * @date 2025/6/6 13:04
     */
    public String getDictLabel(String typeCode, String value) {
        return devDictService.getDictLabel(typeCode, value);
    }

    /**
     * 根据父类型获取字典列表
     *
     * @author yubaoshan
     * @date 2025/6/6 13:04
     */
    @Override
    public List<JSONObject> getDictListByParentDictValue(String typeCode) {
        return devDictService.getDictListByParentDictValue(typeCode);
    }
}
