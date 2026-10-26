
package vip.wqs.dev.modular.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.dev.modular.config.entity.DevBrandConfig;
import vip.wqs.dev.modular.config.mapper.DevBrandConfigMapper;
import vip.wqs.dev.modular.config.param.DevConfigBatchParam;
import vip.wqs.dev.modular.config.service.DevBrandConfigService;

import java.util.List;

/**
 * 平台品牌配置Service接口实现类
 *
 * <p>DEV_BRAND_CONFIG 为全平台共享公共表，全平台共享同一份品牌数据。</p>
 *
 * @author wqs
 * @date 2026/9/4 10:00
 **/
@Service
public class DevBrandConfigServiceImpl extends ServiceImpl<DevBrandConfigMapper, DevBrandConfig>
        implements DevBrandConfigService {

    @Override
    public List<DevBrandConfig> brandList() {
        return this.list(new LambdaQueryWrapper<DevBrandConfig>()
                .orderByAsc(DevBrandConfig::getSortCode));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editBatch(List<DevConfigBatchParam> devConfigBatchParamList) {
        devConfigBatchParamList.forEach(devConfigBatchParam -> {
            // 按配置键幂等保存：存在则更新值，不存在则新增（新品牌键未初始化时保存即建行）
            DevBrandConfig brandConfig = this.getOne(new LambdaQueryWrapper<DevBrandConfig>()
                    .eq(DevBrandConfig::getConfigKey, devConfigBatchParam.getConfigKey()));
            if (ObjectUtil.isNotEmpty(brandConfig)) {
                brandConfig.setConfigValue(devConfigBatchParam.getConfigValue());
                this.updateById(brandConfig);
            } else {
                this.save(BeanUtil.toBean(devConfigBatchParam, DevBrandConfig.class));
            }
        });
    }
}
