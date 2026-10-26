
package vip.wqs.pay.core.consts;

/**
 * 支付插件通用常量
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
public class PayCommonConst {

    /** 支付宝配置KEY：应用编号 */
    public static final String SNOWY_PAY_ALI_APP_ID_KEY = "SNOWY_PAY_ALI_APP_ID";

    /** 支付宝配置KEY：应用私钥 */
    public static final String SNOWY_PAY_ALI_PRIVATE_KEY_KEY = "SNOWY_PAY_ALI_PRIVATE_KEY";

    /** 支付宝配置KEY：商家用户ID */
    public static final String SNOWY_PAY_ALI_MCH_USER_ID_KEY = "SNOWY_PAY_ALI_MCH_USER_ID";

    /** 支付宝配置KEY：支付宝公钥证书路径 */
    public static final String SNOWY_PAY_ALI_CERT_PATH_KEY = "SNOWY_PAY_ALI_CERT_PATH";

    /** 支付宝配置KEY：支付宝根证书路径 */
    public static final String SNOWY_PAY_ALI_ROOT_CERT_PATH_KEY = "SNOWY_PAY_ALI_ROOT_CERT_PATH";

    /** 支付宝配置KEY：应用公钥证书路径 */
    public static final String SNOWY_PAY_ALI_APP_CERT_PATH_KEY = "SNOWY_PAY_ALI_APP_CERT_PATH";

    /** 支付宝配置KEY：支付宝支付网关 */
    public static final String SNOWY_PAY_ALI_SERVER_URL_KEY = "SNOWY_PAY_ALI_SERVER_URL";

    /** 支付宝配置KEY：支付回调地址 */
    public static final String SNOWY_PAY_ALI_NOTIFY_URL_KEY = "SNOWY_PAY_ALI_NOTIFY_URL";

    /** 微信支付配置KEY：应用编号 */
    public static final String SNOWY_PAY_WX_APP_ID_KEY = "SNOWY_PAY_WX_APP_ID";

    /** 微信支付配置KEY：AppSecret */
    public static final String SNOWY_PAY_WX_APP_SECRET_KEY = "SNOWY_PAY_WX_APP_SECRET";

    /** 微信支付配置KEY：商户号 */
    public static final String SNOWY_PAY_WX_MCH_ID_KEY = "SNOWY_PAY_WX_MCH_ID";

    /** 微信支付配置KEY：商户V2密钥 */
    public static final String SNOWY_PAY_WX_MCH_KEY_KEY = "SNOWY_PAY_WX_MCH_KEY";

    /** 微信支付配置KEY：p12证书路径 */
    public static final String SNOWY_PAY_WX_KEY_PATH_KEY = "SNOWY_PAY_WX_KEY_PATH";

    /** 微信支付配置KEY：pub_key.pem证书路径 */
    public static final String SNOWY_PAY_WX_PUB_KEY_PATH_KEY = "SNOWY_PAY_WX_PUB_KEY_PATH";

    /** 微信支付配置KEY：公钥ID */
    public static final String SNOWY_PAY_WX_PUB_KEY_ID_KEY = "SNOWY_PAY_WX_PUB_KEY_ID";

    /** 微信支付配置KEY：apiclient_key.pem路径 */
    public static final String SNOWY_PAY_WX_PRIVATE_KEY_PATH_KEY = "SNOWY_PAY_WX_PRIVATE_KEY_PATH";

    /** 微信支付配置KEY：apiclient_cert.pem路径 */
    public static final String SNOWY_PAY_WX_PRIVATE_CERT_PATH_KEY = "SNOWY_PAY_WX_PRIVATE_CERT_PATH";

    /** 微信支付配置KEY：API V3证书序列号 */
    public static final String SNOWY_PAY_WX_CERT_SERIAL_NO_KEY = "SNOWY_PAY_WX_CERT_SERIAL_NO";

    /** 微信支付配置KEY：API V3秘钥值 */
    public static final String SNOWY_PAY_WX_API_V3_KEY_KEY = "SNOWY_PAY_WX_API_V3_KEY";

    /** 微信支付配置KEY：支付回调地址 */
    public static final String SNOWY_PAY_WX_NOTIFY_URL_KEY = "SNOWY_PAY_WX_NOTIFY_URL";

    /** 微信支付配置KEY：退款回调地址 */
    public static final String SNOWY_PAY_WX_REFUND_NOTIFY_URL_KEY = "SNOWY_PAY_WX_REFUND_NOTIFY_URL";

    /** 支付宝默认支付网关 */
    public static final String ALI_DEFAULT_SERVER_URL = "https://openapi.alipay.com/gateway.do";

    /** 支付宝回调成功响应 */
    public static final String ALI_NOTIFY_SUCCESS_RESULT = "success";

    /** 支付宝回调失败响应 */
    public static final String ALI_NOTIFY_FAIL_RESULT = "failure";

    /** 微信回调成功响应 */
    public static final String WX_NOTIFY_SUCCESS_RESULT = "{\"code\":\"SUCCESS\"}";

    /** 微信回调失败响应 */
    public static final String WX_NOTIFY_FAIL_RESULT = "{\"code\":\"FAIL\"}";

    /** 微信支付事件类型：支付成功通知 */
    public static final String WX_EVENT_TYPE_TRANSACTION_SUCCESS = "TRANSACTION.SUCCESS";
}
