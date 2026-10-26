
package vip.wqs.mobile.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 移动端菜单API
 *
 * @author jetox
 * @date 2023/1/31 10:09
 **/
public interface MobileModuleApi {

    /**
     * 获取移动端模块选择器
     *
     * @author jetox
     * @date 2023/7/15 22:01
     **/
    List<JSONObject> mobileModuleSelector();
}
