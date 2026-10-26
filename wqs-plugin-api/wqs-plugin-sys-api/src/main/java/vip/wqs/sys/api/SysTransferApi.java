
package vip.wqs.sys.api;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 权限转移Api
 *
 * @author yubaoshan
 * @date 2026/05/14
 */
public interface SysTransferApi {

    /**
     * 获取用户可转移资源列表
     */
    List<JSONObject> getTransferResourceList(String sourceUserId);

    /**
     * 获取资源明细列表
     */
    List<JSONObject> getTransferResourceDetail(String sourceUserId, String resourceType);

    /**
     * 执行权限转移
     */
    void executeTransfer(JSONObject executeParam);

    /**
     * 获取机构树选择器
     */
    List<JSONObject> getTransferOrgTreeSelector(JSONObject param);

    /**
     * 获取用户选择器
     */
    Page<JSONObject> getTransferUserSelector(JSONObject param);
}
