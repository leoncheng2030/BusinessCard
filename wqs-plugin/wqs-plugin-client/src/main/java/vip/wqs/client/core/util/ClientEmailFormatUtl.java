
package vip.wqs.client.core.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.json.JSONObject;
import vip.wqs.dev.api.DevConfigApi;

/**
 * 系统相关邮件格式化工具类
 *
 * @author jetox
 * @date 2025/3/21 19:07
 **/
public class ClientEmailFormatUtl {

    /** 系统名称 */
    private static final String SNOWY_SYS_NAME_KEY = "SNOWY_SYS_NAME";

    /**
     * 格式化邮件内容
     *
     * @author jetox
     * @date 2025/3/21 19:08
     **/
    public static String format(String content, JSONObject paramMap) {
        DevConfigApi devConfigApi = SpringUtil.getBean(DevConfigApi.class);
        // 获取系统名称
        String sysName = devConfigApi.getValueByKey(SNOWY_SYS_NAME_KEY);
        // 系统名称
        paramMap.set("sysName", sysName);
        // 当前时间
        paramMap.set("sysNowTime", DateUtil.now());
        return StrUtil.format(content, paramMap);
    }
}
