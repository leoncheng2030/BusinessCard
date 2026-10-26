
package vip.wqs.dev.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 字典API
 *
 * @author jetox
 * @date 2022/9/2 15:58
 */
public interface DevDictApi {

    /**
     * 根据类型跟子类型获得翻译后的label
     *
     * @author yubaoshan
     * @date 2025/6/6 13:04
     */
    String getDictLabel(String typeCode, String value);

    /**
     * 根据父类型获取字典列表
     *
     * @author jetox
     * @date 2025/6/6 13:04
     */
    List<JSONObject> getDictListByParentDictValue(String typeCode);
}
