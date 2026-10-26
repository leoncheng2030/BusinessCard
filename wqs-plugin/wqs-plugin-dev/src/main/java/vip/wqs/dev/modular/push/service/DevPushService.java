
package vip.wqs.dev.modular.push.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import vip.wqs.dev.modular.push.entity.DevPush;
import vip.wqs.dev.modular.push.param.*;

import java.util.List;

/**
 * 消息推送Service接口
 *
 * @author jetox
 * @date 2022/2/23 18:27
 **/
public interface DevPushService extends IService<DevPush> {

    /**
     * 动态推送消息
     *
     * @param engine 推送引擎
     * @param content 文本内容
     * @param noticeAll 是否@所有人
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushDynamicText(String engine, String content, boolean noticeAll);

    /**
     * 动态推送消息
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushDynamicText(DevPushDynamicTextParam devPushDynamicTextParam);

    /**
     * 推送消息——飞书TXT
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushFeiShuText(DevPushFeiShuTextParam devPushFeiShuTextParam);

    /**
     * 推送消息——钉钉TXT
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushDingTalkText(DevPushDingTalkTextParam devPushDingTalkTextParam);

    /**
     * 推送消息——钉钉MARKDOWN
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushDingTalkMarkdown(DevPushDingTalkMarkdownParam devPushDingTalkMarkdownParam);

    /**
     * 推送消息——钉钉LINK
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushDingTalkLink(DevPushDingTalkLinkParam devPushDingTalkLinkParam);

    /**
     * 推送消息——企业微信TXT
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushWorkWechatText(DevPushWorkWechatTextParam devPushWorkWechatTextParam);

    /**
     * 推送消息——企业微信MARKDOWN
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushWorkWechatMarkdown(DevPushWorkWechatMarkdownParam devPushWorkWechatMarkdownParam);

    /**
     * 推送消息——企业微信NEWS
     *
     * @author jetox
     * @date 2022/4/24 20:47
     */
    void pushWorkWechatNews(DevPushWorkWechatNewsParam devPushWorkWechatNewsParam);

    /**
     * 获取推送消息分页
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    Page<DevPush> page(DevPushPageParam devPushPageParam);

    /**
     * 删除推送消息
     *
     * @author jetox
     * @date 2022/8/4 10:36
     **/
    void delete(List<DevPushIdParam> devPushIdParamList);

    /**
     * 获取推送消息详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevPush detail(DevPushIdParam devPushIdParam);

    /**
     * 获取推送消息详情
     *
     * @author jetox
     * @date 2022/4/24 20:08
     */
    DevPush queryEntity(String id);
}
