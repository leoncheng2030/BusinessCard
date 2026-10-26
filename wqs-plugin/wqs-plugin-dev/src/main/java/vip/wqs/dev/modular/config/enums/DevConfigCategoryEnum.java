
package vip.wqs.dev.modular.config.enums;

import lombok.Getter;

/**
 * 配置分类枚举
 *
 * @author jetox
 * @date 2022/7/6 22:21
 */
@Getter
public enum DevConfigCategoryEnum {

    /**
     * 系统基础
     */
    SYS_BASE("SYS_BASE"),

    /**
     * 业务定义
     */
    BIZ_DEFINE("BIZ_DEFINE"),

    /**
     * C端展示-微信小程序（品牌展示/开放平台凭据/引导素材）
     */
    CLIENT_SHOW_MINI("CLIENT_SHOW_MINI"),

    /**
     * C端展示-微信公众号（品牌展示/开放平台凭据/引导素材）
     */
    CLIENT_SHOW_MP("CLIENT_SHOW_MP"),

    /**
     * C端展示-APP（品牌展示/开放平台凭据/下载引导素材）
     */
    CLIENT_SHOW_APP("CLIENT_SHOW_APP");

    private final String value;

    DevConfigCategoryEnum(String value) {
        this.value = value;
    }
}
