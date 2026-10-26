
package vip.wqs.dev.modular.sms.service;

import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.sms.entity.DevSms;
import vip.wqs.dev.modular.sms.param.*;

import java.util.List;

/**
 * 短信Service接口
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
public interface DevSmsService extends IService<DevSms> {

    /**
     * 动态发送短信
     *
     * @param engine 发送引擎
     * @param phoneNumbers 手机号
     * @param templateCodeOrId 模板id或编码
     * @param templateParam 发送参数
     * @author jetox
     * @date 2022/2/7 22:29
     */
    void sendDynamic(String engine, String phoneNumbers, String templateCodeOrId, JSONObject templateParam);

    /**
     * 动态发送短信
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendDynamic(DevSmsSendDynamicParam devSmsSendDynamicParam);

    /**
     * 发送短信——阿里云
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevSmsSendAliyunParam devSmsSendAliyunParam);

    /**
     * 发送短信——腾讯云
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevSmsSendTencentParam devSmsSendTencentParam);

    /**
     * 获取短信分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<DevSms> page(DevSmsPageParam devSmsPageParam);

    /**
     * 删除短信
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevSmsIdParam> devSmsIdParamList);

    /**
     * 获取短信详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevSms detail(DevSmsIdParam devSmsIdParam);

    /**
     * 获取短信详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevSms queryEntity(String id);
}
