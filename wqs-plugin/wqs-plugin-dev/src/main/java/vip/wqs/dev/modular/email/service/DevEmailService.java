
package vip.wqs.dev.modular.email.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.email.entity.DevEmail;
import vip.wqs.dev.modular.email.param.*;

import java.util.List;

/**
 * 邮件Service接口
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
public interface DevEmailService extends IService<DevEmail> {

    /**
     * 动态发送TXT邮件
     *
     * @param engine 发送引擎
     * @param receiveAccounts 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author jetox
     * @date 2022/2/7 22:29
     */
    void sendDynamicTxt(String engine, String receiveAccounts, String subject, String content);

    /**
     * 动态发送HTML邮件
     *
     * @param engine 发送引擎
     * @param receiveAccounts 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author jetox
     * @date 2022/2/7 22:29
     */
    void sendDynamicHtml(String engine, String receiveAccounts, String subject, String content);

    /**
     * 动态发送TXT邮件
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendDynamicTxt(DevEmailSendDynamicTxtParam devEmailSendDynamicTxtParam);

    /**
     * 动态发送HTML邮件
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendDynamicHtml(DevEmailSendDynamicHtmlParam devEmailSendDynamicHtmlParam);

    /**
     * 发送邮件——本地TXT
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendLocal(DevEmailSendLocalTxtParam devEmailSendLocalTxtParam);

    /**
     * 发送邮件——本地HTML
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendLocal(DevEmailSendLocalHtmlParam devEmailSendLocalHtmlParam);

    /**
     * 发送邮件——阿里云TXT
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunTxtParam devEmailSendAliyunTxtParam);

    /**
     * 发送邮件——阿里云HTML
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunHtmlParam devEmailSendAliyunHtmlParam);

    /**
     * 发送邮件——阿里云TMP
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendAliyun(DevEmailSendAliyunTmpParam devEmailSendAliyunTmpParam);

    /**
     * 发送邮件——腾讯云TXT
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentTxtParam devEmailSendTencentTxtParam);

    /**
     * 发送邮件——腾讯云HTML
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentHtmlParam devEmailSendTencentHtmlParam);

    /**
     * 发送邮件——腾讯云TMP
     *
     * @author jetox
     * @date 2022/6/21 18:37
     **/
    void sendTencent(DevEmailSendTencentTmpParam devEmailSendTencentTmpParam);

    /**
     * 获取邮件分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<DevEmail> page(DevEmailPageParam devEmailPageParam);

    /**
     * 删除邮件
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevEmailIdParam> devEmailIdParamList);

    /**
     * 获取邮件详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevEmail detail(DevEmailIdParam devEmailIdParam);

    /**
     * 获取邮件详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevEmail queryEntity(String id);
}
