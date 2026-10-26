
package vip.wqs.gen.modular.config.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollStreamUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vip.wqs.common.enums.CommonSortOrderEnum;
import vip.wqs.common.exception.CommonException;
import vip.wqs.gen.modular.config.entity.GenConfig;
import vip.wqs.gen.modular.config.mapper.GenConfigMapper;
import vip.wqs.gen.modular.config.param.GenConfigEditParam;
import vip.wqs.gen.modular.config.param.GenConfigIdParam;
import vip.wqs.gen.modular.config.param.GenConfigListParam;
import vip.wqs.gen.modular.config.service.GenConfigService;

import java.util.List;

/**
 * 代码生成详情配置Service接口实现类
 *
 * @author yubaoshan
 * @date 2022/10/25 22:33
 **/
@Service
public class GenConfigServiceImpl extends ServiceImpl<GenConfigMapper, GenConfig> implements GenConfigService {

    @Override
    public List<GenConfig> list(GenConfigListParam genConfigListParam) {
        QueryWrapper<GenConfig> queryWrapper = new QueryWrapper<GenConfig>().checkSqlInjection();

        queryWrapper.lambda().eq(GenConfig::getBasicId, genConfigListParam.getBasicId());
        if(ObjectUtil.isNotEmpty(genConfigListParam.getTableType())) {
            queryWrapper.lambda().eq(GenConfig::getTableType, genConfigListParam.getTableType());
        }
        if(ObjectUtil.isAllNotEmpty(genConfigListParam.getSortField(), genConfigListParam.getSortOrder())) {
            CommonSortOrderEnum.validate(genConfigListParam.getSortOrder());
            queryWrapper.orderBy(true, genConfigListParam.getSortOrder().equals(CommonSortOrderEnum.ASC.getValue()),
                    StrUtil.toUnderlineCase(genConfigListParam.getSortField()));
        } else {
            queryWrapper.lambda().orderByAsc(GenConfig::getSortCode);
        }
        return this.list(queryWrapper);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void edit(GenConfigEditParam genConfigEditParam) {
        GenConfig genConfig = this.queryEntity(genConfigEditParam.getId());
        BeanUtil.copyProperties(genConfigEditParam, genConfig);
        this.updateById(genConfig);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(List<GenConfigIdParam> genConfigIdParamList) {
        List<String> basicIdIdList = CollStreamUtil.toList(genConfigIdParamList, GenConfigIdParam::getId);
        if(ObjectUtil.isNotEmpty(basicIdIdList)) {
            // 执行删除
            this.removeByIds(basicIdIdList);
        }
    }

    @Override
    public GenConfig detail(GenConfigIdParam genConfigIdParam) {
        return this.queryEntity(genConfigIdParam.getId());
    }

    @Override
    public GenConfig queryEntity(String id) {
        GenConfig genConfig = this.getById(id);
        if(ObjectUtil.isEmpty(genConfig)) {
            throw new CommonException("代码生成详情配置不存在，id值为：{}", id);
        }
        return genConfig;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void editBatch(List<GenConfigEditParam> genConfigEditParamList) {
        genConfigEditParamList.forEach(this::edit);
    }
}
