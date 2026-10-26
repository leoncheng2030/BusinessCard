
package vip.wqs.sys.modular.org.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.org.entity.SysOrgExt;

/**
 * 组织扩展Service接口
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
public interface SysOrgExtService extends IService<SysOrgExt> {

    /**
     * 插入扩展信息
     *
     * @author jetox
     * @date 2022/4/27 21:38
     */
    void createExtInfo(String orgId, String sourceFromType);
}
