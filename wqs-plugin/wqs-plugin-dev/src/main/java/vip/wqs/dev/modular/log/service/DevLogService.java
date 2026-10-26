
package vip.wqs.dev.modular.log.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.log.entity.DevLog;
import vip.wqs.dev.modular.log.param.DevLogDeleteParam;
import vip.wqs.dev.modular.log.param.DevLogIdParam;
import vip.wqs.dev.modular.log.param.DevLogPageParam;
import vip.wqs.dev.modular.log.result.DevLogOpBarChartDataResult;
import vip.wqs.dev.modular.log.result.DevLogOpPieChartDataResult;
import vip.wqs.dev.modular.log.result.DevLogVisLineChartDataResult;
import vip.wqs.dev.modular.log.result.DevLogVisPieChartDataResult;

import java.util.List;

/**
 * 日志Service接口
 *
 * @author jetox
 * @date 2022/9/2 15:04
 */
public interface DevLogService extends IService<DevLog> {

    /**
     * 获取日志分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<DevLog> page(DevLogPageParam devLogPageParam);

    /**
     * 获取单条日志详情
     */
    DevLog detail(DevLogIdParam devLogIdParam);

    /**
     * 清空日志
     *
     * @author jetox
     * @date 2022/9/6 13:17
     */
    void delete(DevLogDeleteParam devLogDeleteParam);

    /**
     * 获取访问日志折线图数据
     *
     * @author jetox
     * @date 2022/9/4 21:18
     */
    List<DevLogVisLineChartDataResult> visLogLineChartData();

    /**
     * 获取访问日志饼状图数据
     *
     * @author jetox
     * @date 2022/9/4 21:18
     */
    List<DevLogVisPieChartDataResult> visLogPieChartData();

    /**
     * 获取操作日志柱状图数据
     *
     * @author jetox
     * @date 2022/9/4 21:18
     */
    List<DevLogOpBarChartDataResult> opLogBarChartData();

    /**
     * 获取操作日志饼状图数据
     *
     * @author jetox
     * @date 2022/9/4 21:18
     */
    List<DevLogOpPieChartDataResult> opLogPieChartData();
}
