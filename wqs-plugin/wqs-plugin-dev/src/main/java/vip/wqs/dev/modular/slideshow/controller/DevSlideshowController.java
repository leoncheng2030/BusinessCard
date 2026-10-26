
package vip.wqs.dev.modular.slideshow.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.wqs.common.annotation.CommonLog;
import vip.wqs.common.pojo.CommonResult;
import vip.wqs.dev.modular.slideshow.entity.DevSlideshow;
import vip.wqs.dev.modular.slideshow.param.DevSlideshowAddParam;
import vip.wqs.dev.modular.slideshow.param.DevSlideshowEditParam;
import vip.wqs.dev.modular.slideshow.param.DevSlideshowIdParam;
import vip.wqs.dev.modular.slideshow.param.DevSlideshowPageParam;
import vip.wqs.dev.modular.slideshow.service.DevSlideshowService;

import java.util.List;

/**
 * 轮播图控制器
 *
 * @author yubaoshan
 * @date  2024/07/13 00:31
 */
@Tag(name = "轮播图控制器")
@RestController
@Validated
public class DevSlideshowController {

    @Resource
    private DevSlideshowService devSlideshowService;

    /**
     * 获取轮播图分页
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "获取轮播图分页")
    @GetMapping("/dev/slideshow/page")
    public CommonResult<Page<DevSlideshow>> page(DevSlideshowPageParam devSlideshowPageParam) {
        return CommonResult.data(devSlideshowService.page(devSlideshowPageParam));
    }

    /**
     * 获取指定位置的轮播图列表（后台首页轮播卡片等按位置展示，仅返回启用项）
     *
     * @author wqs
     * @date 2026/9/4 15:00
     */
    @Operation(summary = "获取指定位置的轮播图列表")
    @GetMapping("/dev/slideshow/getListByPlace")
    public CommonResult<List<JSONObject>> getListByPlace(@RequestParam("place") String place) {
        return CommonResult.data(devSlideshowService.getListByPlace(place));
    }

    /**
     * 添加轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "添加轮播图")
    @CommonLog("添加轮播图")
    @SaCheckPermission("/dev/slideshow/add")
    @PostMapping("/dev/slideshow/add")
    public CommonResult<String> add(@RequestBody @Valid DevSlideshowAddParam devSlideshowAddParam) {
        devSlideshowService.add(devSlideshowAddParam);
        return CommonResult.ok();
    }

    /**
     * 编辑轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "编辑轮播图")
    @CommonLog("编辑轮播图")
    @SaCheckPermission("/dev/slideshow/edit")
    @PostMapping("/dev/slideshow/edit")
    public CommonResult<String> edit(@RequestBody @Valid DevSlideshowEditParam devSlideshowEditParam) {
        devSlideshowService.edit(devSlideshowEditParam);
        return CommonResult.ok();
    }

    /**
     * 删除轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "删除轮播图")
    @CommonLog("删除轮播图")
    @SaCheckPermission("/dev/slideshow/delete")
    @PostMapping("/dev/slideshow/delete")
    public CommonResult<String> delete(@RequestBody @Valid @NotEmpty(message = "集合不能为空")
                                                   List<DevSlideshowIdParam> devSlideshowIdParamList) {
        devSlideshowService.delete(devSlideshowIdParamList);
        return CommonResult.ok();
    }

    /**
     * 禁用轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "禁用轮播图")
    @CommonLog("禁用轮播图")
    @SaCheckPermission("/dev/slideshow/disableStatus")
    @PostMapping("/dev/slideshow/disableStatus")
    public CommonResult<String> disableStatus(@RequestBody @Valid DevSlideshowIdParam devSlideshowIdParam) {
        devSlideshowService.disableStatus(devSlideshowIdParam);
        return CommonResult.ok();
    }

    /**
     * 启用轮播图
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     */
    @Operation(summary = "启用轮播图")
    @CommonLog("启用轮播图")
    @SaCheckPermission("/dev/slideshow/enableStatus")
    @PostMapping("/dev/slideshow/enableStatus")
    public CommonResult<String> enableStatus(@RequestBody @Valid DevSlideshowIdParam devSlideshowIdParam) {
        devSlideshowService.enableStatus(devSlideshowIdParam);
        return CommonResult.ok();
    }
}
