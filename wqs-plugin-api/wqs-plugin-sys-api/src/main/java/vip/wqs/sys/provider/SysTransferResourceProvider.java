
package vip.wqs.sys.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import java.util.Collections;
import java.util.List;

/**
 * 权限转移资源提供者接口，各插件模块实现此接口注册可转移的资源类型
 *
 * @author yubaoshan
 * @date 2026/05/13
 */
public interface SysTransferResourceProvider {

    /**
     * 获取资源类型标识
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    String getResourceType();

    /**
     * 获取资源类型名称
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    String getResourceTypeName();

    /**
     * 获取排序值，值越小越靠前
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    default int getOrder() {
        return 100;
    }

    /**
     * 获取指定用户拥有的该类型资源总数
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    long getCount(String userId);

    /**
     * 获取指定用户拥有的该类型资源明细列表
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    List<JSONObject> getDetailList(String userId);

    /**
     * 执行权限转移
     *
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID（删除模式下可为null）
     * @param transferMode 转移模式：TRANSFER、COPY、DELETE
     * @param transferAll  是否全部转移
     * @param selectedIds  选中的资源ID列表（transferAll为true时可为空）
     * @author yubaoshan
     * @date 2026/05/13
     */
    void executeTransfer(String sourceUserId, String targetUserId, String transferMode,
                         boolean transferAll, List<String> selectedIds);

    /**
     * 获取资源明细的列定义，前端表格根据此定义动态渲染列
     *
     * @author yubaoshan
     * @date 2026/05/13
     */
    default List<JSONObject> getDetailColumns() {
        return Collections.singletonList(JSONUtil.createObj().set("title", "名称").set("dataIndex", "name"));
    }
}
