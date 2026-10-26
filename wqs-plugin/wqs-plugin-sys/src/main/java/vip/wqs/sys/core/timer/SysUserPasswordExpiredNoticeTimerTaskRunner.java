
package vip.wqs.sys.core.timer;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.wqs.common.timer.CommonTimerTaskRunner;
import vip.wqs.sys.modular.user.service.SysUserService;

/**
 * 通知用户密码即将到期定时类
 *
 * @author jetox
 * @date 2022/8/5 15:52
 **/
@Slf4j
@Component
public class SysUserPasswordExpiredNoticeTimerTaskRunner implements CommonTimerTaskRunner {

    @Resource
    private SysUserService sysUserService;

    @Override
    public void action(String extJson) {
        // 通知用户密码即将到期
        sysUserService.noticeUserPasswordAboutToExpired();
        log.info(">>> 密码即将到期通知定时任务执行完成");
    }
}
