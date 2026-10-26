
package vip.wqs.dev.modular.config.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.config.entity.DevBrandConfig;
import vip.wqs.dev.modular.config.param.DevConfigBatchParam;

import java.util.List;

/**
 * 平台品牌配置Service接口
 *
 * @author wqs
 * @date 2026/9/4 10:00
 **/
public interface DevBrandConfigService extends IService<DevBrandConfig> {

    /**
     * 获取平台品牌配置列表（按排序码升序）
     **/
    List<DevBrandConfig> brandList();

    /**
     * 平台品牌批量保存（按配置键幂等：存在则更新值，不存在则新增）
     **/
    void editBatch(List<DevConfigBatchParam> devConfigBatchParamList);
}
