
package vip.wqs.dev.modular.job.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.wqs.common.timer.CommonTimerTaskRunner;

/**
 * 定时器的一个示例
 *
 * @author jetox
 * @date 2022/8/5 15:52
 **/
@Slf4j
@Component
public class DevJobTimerTaskRunner implements CommonTimerTaskRunner {

    private int n = 1;

    @Override
    public void action(String extJson) {
        log.info("我是一个定时任务，正在在被执行第{}次", n);
        n = n + 1;
    }
}
