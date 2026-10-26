
package vip.wqs.dev.modular.monitor.service;

import vip.wqs.dev.modular.monitor.result.DevMonitorServerResult;

/**
 * 监控Service接口
 *
 * @author jetox
 * @date 2022/9/1 15:59
 */
public interface DevMonitorService {

    /**
     * 获取服务器监控信息
     *
     * @author jetox
     * @date 2022/9/1 16:02
     */
    DevMonitorServerResult serverInfo();

    /**
     * 获取服务器网络情况
     *
     * @author diantu
     * @date 2023/7/27
     */
    DevMonitorServerResult networkInfo();
}
