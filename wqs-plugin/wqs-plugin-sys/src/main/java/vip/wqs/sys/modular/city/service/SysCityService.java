
package vip.wqs.sys.modular.city.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.sys.modular.city.entity.SysCity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * 系统城市Service接口
 *
 * @author wqs
 * @date 2026/9/3
 **/
public interface SysCityService extends IService<SysCity> {

    /**
     * 获取省市区树（全量三级：省→市→区县，节点结构value/label/children供级联选择）
     *
     * @author wqs
     * @date 2026/9/3
     **/
    List<JSONObject> cityTree();

    /**
     * 根据cityId集合批量获取城市名称Map（cityId → name，不存在的cityId不返回）
     *
     * @author wqs
     * @date 2026/9/3
     **/
    Map<Integer, String> getCityNameMapByIdList(Collection<Integer> cityIdList);
}
