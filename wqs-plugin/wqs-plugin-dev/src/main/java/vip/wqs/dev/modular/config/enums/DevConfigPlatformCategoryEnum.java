
package vip.wqs.dev.modular.config.enums;

import cn.hutool.core.util.StrUtil;
import lombok.Getter;

/**
 * 平台级配置分类枚举（邮件/短信发送通道、文件存储引擎参数及模板文案等平台统一配置）
 *
 * <p>这些分类由平台统一提供并集中运营，保证发送/存储通道及模板全局一致。</p>
 *
 * @author wqs
 * @date 2026/9/4 10:00
 */
@Getter
public enum DevConfigPlatformCategoryEnum {

    /** 文件-本地存储引擎 */
    FILE_LOCAL("FILE_LOCAL"),

    /** 文件-阿里云OSS引擎 */
    FILE_ALIYUN("FILE_ALIYUN"),

    /** 文件-腾讯云COS引擎 */
    FILE_TENCENT("FILE_TENCENT"),

    /** 文件-MinIO引擎 */
    FILE_MINIO("FILE_MINIO"),

    /** 文件-RustFS引擎 */
    FILE_RUSTFS("FILE_RUSTFS"),

    /** 文件-FTP引擎 */
    FILE_FTP("FILE_FTP"),

    /** 邮件-本地通道（无凭据，仅发件配置） */
    EMAIL_LOCAL("EMAIL_LOCAL"),

    /** 邮件-阿里云通道 */
    EMAIL_ALIYUN("EMAIL_ALIYUN"),

    /** 邮件-腾讯云通道 */
    EMAIL_TENCENT("EMAIL_TENCENT"),

    /** 短信-阿里云通道 */
    SMS_ALIYUN("SMS_ALIYUN"),

    /** 短信-腾讯云通道 */
    SMS_TENCENT("SMS_TENCENT"),

    /** 邮件模板-B端 */
    EMAIL_TEMPLATE_FOR_B("EMAIL_TEMPLATE_FOR_B"),

    /** 邮件模板-C端 */
    EMAIL_TEMPLATE_FOR_C("EMAIL_TEMPLATE_FOR_C"),

    /** 短信模板-B端 */
    SMS_TEMPLATE_FOR_B("SMS_TEMPLATE_FOR_B"),

    /** 短信模板-C端 */
    SMS_TEMPLATE_FOR_C("SMS_TEMPLATE_FOR_C");

    private final String value;

    DevConfigPlatformCategoryEnum(String value) {
        this.value = value;
    }

    /**
     * 分类是否为平台级（发送/存储通道及模板统一由平台配置）
     *
     * @param category 配置分类
     * @return true-平台级分类
     **/
    public static boolean isPlatformCategory(String category) {
        if(StrUtil.isEmpty(category)) {
            return false;
        }
        for(DevConfigPlatformCategoryEnum platformCategory : values()) {
            if(platformCategory.value.equals(category)) {
                return true;
            }
        }
        return false;
    }
}
