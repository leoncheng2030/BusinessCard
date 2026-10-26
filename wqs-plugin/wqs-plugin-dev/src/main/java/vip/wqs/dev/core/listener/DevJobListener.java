
package vip.wqs.dev.core.listener;

import cn.hutool.cron.CronUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import vip.wqs.common.exception.CommonException;
import vip.wqs.common.timer.CommonTimerTaskRunner;
import vip.wqs.dev.modular.job.entity.DevJob;
import vip.wqs.dev.modular.job.enums.DevJobStatusEnum;
import vip.wqs.dev.modular.job.service.DevJobService;

/**
 * 定时任务监听器，系统启动时将定时任务启动
 *
 * @author jetox
 * @date 2022/8/5 16:07
 **/
@Slf4j
@Configuration
public class DevJobListener implements ApplicationListener<ApplicationStartedEvent>, Ordered {

    @Override
    public void onApplicationEvent(@NonNull ApplicationStartedEvent applicationStartedEvent) {
        SpringUtil.getBean(DevJobService.class).list(new LambdaQueryWrapper<DevJob>()
                .eq(DevJob::getJobStatus, DevJobStatusEnum.RUNNING.getValue()).orderByAsc(DevJob::getSortCode))
                .forEach(devJob -> CronUtil.schedule(devJob.getId(), devJob.getCronExpression(), () -> {
                    try {
                        // 运行定时任务
                        ((CommonTimerTaskRunner) SpringUtil.getBean(Class.forName(devJob.getActionClass()))).action(devJob.getExtJson());
                    } catch (ClassNotFoundException e) {
                        throw new CommonException("定时任务找不到对应的类，名称为：{}", devJob.getActionClass());
                    }
                }));
        // 设置秒级别的启用
        CronUtil.setMatchSecond(true);
        // 启动定时器执行器
        CronUtil.start();
    }

    @Override
    public int getOrder() {
        return LOWEST_PRECEDENCE;
    }
}
