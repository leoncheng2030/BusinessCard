
package vip.wqs.sys.modular.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.auth.core.pojo.SaBaseLoginUser;
import vip.wqs.sys.modular.org.entity.SysUserDataScope;

import java.util.List;

/**
 * 用户数据范围预计算Service接口
 *
 * @author yubaoshan
 * @date 2026/2/12
 **/
public interface SysUserDataScopeService extends IService<SysUserDataScope> {

    /**
     * 刷新指定用户的数据范围预计算（登录时调用）
     * 按API维度存储，每个API的数据范围独立
     *
     * @param userId 用户ID
     * @param dataScopeList 用户的数据范围集合（per-API）
     */
    void refreshByUserId(String userId, List<SaBaseLoginUser.DataScope> dataScopeList);

    /**
     * 删除指定用户的数据范围预计算
     *
     * @param userId 用户ID
     */
    void deleteByUserId(String userId);

    /**
     * 删除指定机构ID的预计算记录（机构删除时调用）
     * 精准删除，不影响其他机构的数据
     *
     * @param orgIds 被删除的机构ID列表
     */
    void deleteByOrgIds(List<String> orgIds);

    /**
     * 为指定用户的所有API追加机构ID（机构新增时调用）
     *
     * @param userId 用户ID
     * @param orgIds 新增的机构ID列表
     */
    void appendOrgIdsForUser(String userId, List<String> orgIds);
}
