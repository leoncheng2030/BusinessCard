
package vip.wqs.dev.modular.dev;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.NetUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.system.SystemUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import vip.wqs.dev.api.DevApi;
import vip.wqs.dev.modular.dict.entity.DevDict;
import vip.wqs.dev.modular.dict.enums.DevDictCategoryEnum;
import vip.wqs.dev.modular.dict.service.DevDictService;
import vip.wqs.dev.modular.email.service.DevEmailService;
import vip.wqs.dev.modular.file.service.DevFileService;
import vip.wqs.dev.modular.job.service.DevJobService;
import vip.wqs.dev.modular.message.service.DevMessageService;
import vip.wqs.dev.modular.sms.service.DevSmsService;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;

/**
 * 开发工具模块综合API接口实现类
 *
 * @author jetox
 * @date 2022/9/26 14:30
 **/
@Service
public class DevApiProvider implements DevApi {

    @Resource
    private DevDictService devDictService;

    @Resource
    private DevJobService devJobService;

    @Resource
    private DevFileService devFileService;

    @Resource
    private DevSmsService devSmsService;

    @Resource
    private DevEmailService devEmailService;

    @Resource
    private DevMessageService devMessageService;

    @Override
    public JSONObject getDevOpCount() {
        Long sysDictCount = devDictService.count(new LambdaQueryWrapper<DevDict>().eq(DevDict::getCategory,
                DevDictCategoryEnum.FRM.getValue()));
        Long jobCount = 0L;
//        Long jobCount = devJobService.count(new LambdaQueryWrapper<DevJob>().eq(DevJob::getJobStatus,
//                DevJobStatusEnum.RUNNING.getValue()));
        JSONObject json = new JSONObject();
        json.set("sysDictCount", sysDictCount);
        json.set("jobCount", jobCount);
        return json;
    }

    @Override
    public JSONObject getToolDataCount() {
        JSONObject json = new JSONObject();
        json.set("fileCount", devFileService.count());
        json.set("smsCount", devSmsService.count());
        json.set("emailCount", devEmailService.count());
        json.set("messageCount", devMessageService.count());
        return json;
    }

    @Override
    public JSONObject getSysRunInfo() {
        RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
        JSONObject json = new JSONObject();
        json.set("javaVersion", SystemUtil.get("java.version", false));
        json.set("osName", SystemUtil.getOsInfo().getName());
        json.set("serverIp", NetUtil.getLocalhostStr());
        json.set("runTime", DateUtil.formatBetween(DateUtil.date(runtimeMXBean.getStartTime()), DateUtil.date()));
        return json;
    }
}
