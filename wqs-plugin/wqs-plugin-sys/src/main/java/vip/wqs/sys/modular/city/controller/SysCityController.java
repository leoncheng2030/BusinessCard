
package vip.wqs.sys.modular.city.controller;

import cn.hutool.json.JSONObject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.sys.modular.city.service.SysCityService;

import java.util.List;

/**
 * 系统城市控制器
 *
 * @author wqs
 * @date 2026/9/3
 **/
@Tag(name = "城市控制器")
@RestController
@Validated
public class SysCityController {

    @Resource
    private SysCityService sysCityService;

    /**
     * 获取省市区树（全量三级：省→市→区县，供前端区域级联选择器使用）
     *
     * @author wqs
     * @date 2026/9/3
     **/
    @Operation(summary = "获取省市区树（全量三级）")
    @GetMapping("/sys/city/tree")
    public CommonResult<List<JSONObject>> tree() {
        return CommonResult.data(sysCityService.cityTree());
    }
}
