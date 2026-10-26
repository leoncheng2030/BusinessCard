
package vip.wqs.sys.modular.org.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.sys.modular.org.entity.SysOrgExt;
import vip.wqs.sys.modular.org.mapper.SysOrgExtMapper;
import vip.wqs.sys.modular.org.service.SysOrgExtService;

/**
 * 组织扩展Service接口实现类
 *
 * @author yubaoshan
 * @date  2024/12/21 01:25
 **/
@Service
public class SysOrgExtServiceImpl extends ServiceImpl<SysOrgExtMapper, SysOrgExt> implements SysOrgExtService {

    @Override
    public void createExtInfo(String orgId, String sourceFromType) {
        SysOrgExt sysOrgExt = new SysOrgExt();
        sysOrgExt.setOrgId(orgId);
        sysOrgExt.setSourceFromType(sourceFromType);
        this.save(sysOrgExt);
    }
}
