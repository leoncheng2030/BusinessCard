
package vip.wqs.sys.modular.city.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.wqs.sys.modular.city.entity.SysCity;
import vip.wqs.sys.modular.city.mapper.SysCityMapper;
import vip.wqs.sys.modular.city.service.SysCityService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统城市Service接口实现类
 *
 * @author wqs
 * @date 2026/9/3
 **/
@Service
public class SysCityServiceImpl extends ServiceImpl<SysCityMapper, SysCity> implements SysCityService {

    @Override
    public List<JSONObject> cityTree() {
        // 全量载入展示中数据（isShow=1，逻辑删除由MyBatis-Plus自动过滤），列表顺序即数据录入顺序（省级在前）
        List<SysCity> allCityList = this.list(new LambdaQueryWrapper<SysCity>().eq(SysCity::getIsShow, 1));
        // 父子索引：parentId -> 直接子节点列表（LinkedHashMap保序，兄弟节点维持录入顺序）
        Map<Integer, List<SysCity>> childrenMap = allCityList.stream().collect(Collectors.groupingBy(
                SysCity::getParentId, LinkedHashMap::new, Collectors.toList()));
        List<JSONObject> result = new ArrayList<>();
        List<SysCity> provinceList = childrenMap.getOrDefault(0, new ArrayList<>());
        for (SysCity province : provinceList) {
            JSONObject provinceJson = this.buildTreeNode(province);
            List<JSONObject> cityJsonList = new ArrayList<>();
            for (SysCity city : childrenMap.getOrDefault(province.getCityId(), new ArrayList<>())) {
                JSONObject cityJson = this.buildTreeNode(city);
                List<JSONObject> districtJsonList = childrenMap.getOrDefault(city.getCityId(), new ArrayList<>())
                        .stream().map(this::buildTreeNode).collect(Collectors.toList());
                cityJson.set("children", districtJsonList);
                cityJsonList.add(cityJson);
            }
            provinceJson.set("children", cityJsonList);
            result.add(provinceJson);
        }
        return result;
    }

    @Override
    public Map<Integer, String> getCityNameMapByIdList(Collection<Integer> cityIdList) {
        if (CollectionUtil.isEmpty(cityIdList)) {
            return new HashMap<>();
        }
        return this.list(new LambdaQueryWrapper<SysCity>().in(SysCity::getCityId, cityIdList)).stream()
                .collect(Collectors.toMap(SysCity::getCityId, SysCity::getName, (cityId, name) -> name));
    }

    /**
     * 构建级联树轻量节点（只带value/label，children由外层按层级拼装）
     */
    private JSONObject buildTreeNode(SysCity sysCity) {
        return JSONUtil.createObj().set("value", sysCity.getCityId()).set("label", sysCity.getName());
    }
}
