
package vip.wqs.client.core.timer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.wqs.client.modular.user.service.ClientUserService;
import vip.wqs.common.timer.CommonTimerTaskRunner;

/**
 * 通知用户密码即将到期定时类
 *
 * @author jetox
 * @date 2022/8/5 15:52
 **/
@Slf4j
@Component
public class ClientUserPasswordExpiredNoticeTimerTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private ClientUserService clientUserService;

    @Override
    public void action(String extJson) {
        // 通知用户密码即将到期
        clientUserService.noticeUserPasswordAboutToExpired();
        log.info(">>> 密码即将到期通知定时任务执行完成");
    }
}
