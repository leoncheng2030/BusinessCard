
package vip.wqs.dev.modular.log.provider;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.auth.core.util.StpLoginUserUtil;
import vip.wqs.common.page.CommonPageRequest;
import vip.wqs.dev.api.DevLogApi;
import vip.wqs.dev.modular.log.entity.DevLog;
import vip.wqs.dev.modular.log.enums.DevLogCategoryEnum;
import vip.wqs.dev.modular.log.service.DevLogService;
import vip.wqs.dev.modular.log.util.DevLogUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 日志API接口实现类
 *
 * @author jetox
 * @date 2022/9/2 16:07
 */
@Service
public class DevLogApiProvider implements DevLogApi {

    @Resource
    private DevLogService devLogService;

    @Override
    public void executeLoginLog(String userName) {
        DevLogUtil.executeLoginLog(userName);
    }

    @Override
    public void executeLogoutLog(String userName) {
        DevLogUtil.executeLogoutLog(userName);
    }

    @Override
    public List<JSONObject> currentUserVisLogList() {
        return devLogService.page(CommonPageRequest.defaultPage(), new LambdaQueryWrapper<DevLog>()
                .select(DevLog::getId, DevLog::getName, DevLog::getOpUser, DevLog::getOpTime, DevLog::getOpAddress, DevLog::getOpIp, DevLog::getCategory)
                .eq(DevLog::getOpUser, StpLoginUserUtil.getLoginUser().getName())
                .in(DevLog::getCategory, DevLogCategoryEnum.LOGIN.getValue(), DevLogCategoryEnum.LOGOUT.getValue())
                .orderByDesc(DevLog::getCreateTime))
                .getRecords().stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }

    @Override
    public List<JSONObject> currentUserOpLogList() {
        return devLogService.page(CommonPageRequest.defaultPage(), new LambdaQueryWrapper<DevLog>()
                .select(DevLog::getId, DevLog::getName, DevLog::getOpUser, DevLog::getOpTime, DevLog::getOpAddress, DevLog::getOpIp, DevLog::getExeStatus)
                .eq(DevLog::getOpUser, StpLoginUserUtil.getLoginUser().getName())
                .in(DevLog::getCategory, DevLogCategoryEnum.OPERATE.getValue(), DevLogCategoryEnum.EXCEPTION.getValue())
                .orderByDesc(DevLog::getCreateTime))
                .getRecords().stream().map(JSONUtil::parseObj).collect(Collectors.toList());
    }
}
