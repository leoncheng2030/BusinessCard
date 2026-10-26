
package vip.wqs.pay.core.config;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.CertAlipayRequest;
import com.alipay.api.DefaultAlipayClient;
import com.github.binarywang.wxpay.config.WxPayConfig;
import com.github.binarywang.wxpay.service.WxPayService;
import com.github.binarywang.wxpay.service.impl.WxPayServiceImpl;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.wqs.common.exception.CommonException;
import vip.wqs.dev.api.DevConfigApi;
import vip.wqs.pay.core.consts.PayCommonConst;

/**
 * 支付凭证配置
 * <p>支付凭证通过参数配置中心（dev_config 表）管理，本类启动时通过 DevConfigApi 读取并初始化
 * 支付宝/微信支付客户端。相关配置必须在管理后台配置完成，否则对应支付通道无法使用。</p>
 *
 * @author wqs
 * @date 2026/9/3 14:30
 */
@Slf4j
@Component
public class PayConfigure {

    @Resource
    private DevConfigApi devConfigApi;

    /** 支付宝客户端（公钥证书模式） */
    private AlipayClient alipayClient;

    /** 微信支付服务 */
    private WxPayService wxPayService;

    /**
     * 启动时初始化支付凭证
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    @PostConstruct
    public void init() {
        initAlipayClient();
        initWxPayService();
    }

    /**
     * 初始化支付宝客户端（公钥证书模式）
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private void initAlipayClient() {
        String appId = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_APP_ID_KEY);
        String privateKey = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_PRIVATE_KEY_KEY);
        String certPath = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_CERT_PATH_KEY);
        String rootCertPath = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_ROOT_CERT_PATH_KEY);
        String appCertPath = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_APP_CERT_PATH_KEY);
        // 未配置支付宝核心凭证时跳过初始化，调用对应支付通道时再报错提示
        if(StrUtil.hasBlank(appId, privateKey, certPath, rootCertPath, appCertPath)) {
            log.warn("支付宝支付凭证未配置完整，支付宝支付通道暂不可用");
            return;
        }
        try {
            CertAlipayRequest certAlipayRequest = new CertAlipayRequest();
            certAlipayRequest.setServerUrl(getAlipayServerUrl());
            certAlipayRequest.setAppId(appId);
            certAlipayRequest.setPrivateKey(privateKey);
            certAlipayRequest.setCertPath(certPath);
            certAlipayRequest.setAlipayPublicCertPath(appCertPath);
            certAlipayRequest.setRootCertPath(rootCertPath);
            alipayClient = new DefaultAlipayClient(certAlipayRequest);
        } catch (AlipayApiException e) {
            log.error("支付宝客户端初始化失败：{}", e.getMessage(), e);
        }
    }

    /**
     * 初始化微信支付服务（API V3）
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    private void initWxPayService() {
        String appId = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_APP_ID_KEY);
        String mchId = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_MCH_ID_KEY);
        String mchKey = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_MCH_KEY_KEY);
        String privateKeyPath = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_PRIVATE_KEY_PATH_KEY);
        String privateCertPath = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_PRIVATE_CERT_PATH_KEY);
        String certSerialNo = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_CERT_SERIAL_NO_KEY);
        String apiV3Key = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_API_V3_KEY_KEY);
        String notifyUrl = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_NOTIFY_URL_KEY);
        // 未配置微信核心凭证时跳过初始化，调用对应支付通道时再报错提示
        if(StrUtil.hasBlank(appId, mchId, privateKeyPath, privateCertPath, certSerialNo, apiV3Key)) {
            log.warn("微信支付凭证未配置完整，微信支付通道暂不可用");
            return;
        }
        WxPayConfig wxPayConfig = new WxPayConfig();
        wxPayConfig.setAppId(appId);
        wxPayConfig.setMchId(mchId);
        wxPayConfig.setMchKey(mchKey);
        wxPayConfig.setKeyPath(devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_KEY_PATH_KEY));
        wxPayConfig.setPrivateKeyPath(privateKeyPath);
        wxPayConfig.setPrivateCertPath(privateCertPath);
        wxPayConfig.setCertSerialNo(certSerialNo);
        wxPayConfig.setApiV3Key(apiV3Key);
        wxPayConfig.setNotifyUrl(notifyUrl);
        WxPayService payService = new WxPayServiceImpl();
        payService.setConfig(wxPayConfig);
        wxPayService = payService;
    }

    /**
     * 获取支付宝客户端，未初始化时抛异常提示
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public AlipayClient getAlipayClient() {
        if(ObjectUtil.isEmpty(alipayClient)) {
            throw new CommonException("支付宝支付凭证未配置完整，请在管理后台参数配置中完善支付宝支付相关配置");
        }
        return alipayClient;
    }

    /**
     * 获取微信支付服务，未初始化时抛异常提示
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public WxPayService getWxPayService() {
        if(ObjectUtil.isEmpty(wxPayService)) {
            throw new CommonException("微信支付凭证未配置完整，请在管理后台参数配置中完善微信支付相关配置");
        }
        return wxPayService;
    }

    /**
     * 获取支付宝支付网关，未配置时使用默认生产网关
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getAlipayServerUrl() {
        String serverUrl = devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_SERVER_URL_KEY);
        return StrUtil.isBlank(serverUrl) ? PayCommonConst.ALI_DEFAULT_SERVER_URL : serverUrl;
    }

    /**
     * 获取支付宝支付回调地址
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getAlipayNotifyUrl() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_NOTIFY_URL_KEY);
    }

    /**
     * 获取支付宝公钥证书路径（回调验签用）
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getAlipayCertPath() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_ALI_CERT_PATH_KEY);
    }

    /**
     * 获取微信支付回调地址
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getWxNotifyUrl() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_NOTIFY_URL_KEY);
    }

    /**
     * 获取微信退款回调地址
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getWxRefundNotifyUrl() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_REFUND_NOTIFY_URL_KEY);
    }

    /**
     * 获取微信应用编号
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getWxAppId() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_APP_ID_KEY);
    }

    /**
     * 获取微信商户私钥路径（apiclient_key.pem）
     *
     * @author wqs
     * @date 2026/9/3 14:30
     */
    public String getWxPrivateKeyPath() {
        return devConfigApi.getValueByKey(PayCommonConst.SNOWY_PAY_WX_PRIVATE_KEY_PATH_KEY);
    }
}
