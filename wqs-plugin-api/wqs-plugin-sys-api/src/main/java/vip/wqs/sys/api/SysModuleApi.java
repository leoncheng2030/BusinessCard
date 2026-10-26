
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 模块API
 *
 * @author yubaoshan
 * @date 2024/9/6 01:24
 **/
public interface SysModuleApi {

    /**
     * 获取所有模块
     *
     * @author yubaoshan
     * @date 2024/9/6 01:24
     **/
    List<JSONObject> moduleSelector();

    /**
     * 增加模块
     *
     * @author yubaoshan
     * @date 2025/5/5 04:30
     */
    void addModule(String id, String title, String code, String icon, String color);

    /**
     * 删除模块以及所有的资源
     *
     * @author yubaoshan
     * @date 2025/5/5 04:30
     */
    void deleteModuleByCode(String moduleCode, List<String> resourceIdList);
}
