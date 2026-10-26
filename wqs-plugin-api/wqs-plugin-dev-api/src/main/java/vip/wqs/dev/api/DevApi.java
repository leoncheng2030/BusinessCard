
package vip.wqs.dev.api;

import cn.hutool.json.JSONObject;

/**
 * 开发工具模块综合API
 *
 * @author jetox
 * @date 2022/9/26 14:24
 **/
public interface DevApi {

    /**
     * 获得dev模块运维数据
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     **/
    JSONObject getDevOpCount();

    /**
     * 获得dev工具数量（短信、邮件、文件、消息）
     *
     * @author yubaoshan
     * @date 2024/7/18 17:35
     **/
    JSONObject getToolDataCount();

    /**
     * 获取系统运行信息（Java版本、OS、运行时长、服务器IP）
     *
     * @author yubaoshan
     * @date 2026/4/21
     **/
    JSONObject getSysRunInfo();
}
