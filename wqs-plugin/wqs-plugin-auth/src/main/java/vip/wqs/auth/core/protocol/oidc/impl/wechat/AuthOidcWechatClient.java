
package vip.wqs.auth.core.protocol.oidc.impl.wechat;

import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.hutool.HutoolImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWeChatOpenRequest;
import vip.wqs.auth.core.protocol.oidc.AuthOidcBaseJson;


/**
 * OIDC认证源微信客户端
 *
 * @author jetox
 * @date 2024/11/8 15:41
 **/
@Slf4j
public record AuthOidcWechatClient(AuthOidcBaseJson authOidcBaseJson) {

    public AuthRequest getAuthRequest() {
        return getAuthRequest(false);
    }

    public AuthRequest getAuthRequest(boolean ignoreCheckState) {
        String clientId = authOidcBaseJson.getClientId();
        String clientSecret = authOidcBaseJson.getClientSecret();
        String callbackUrl = authOidcBaseJson.getCallbackUrl();
        HttpUtil.setHttp(new HutoolImpl());
        return new AuthWeChatOpenRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .redirectUri(callbackUrl)
                .ignoreCheckState(ignoreCheckState)
                .build());
    }
}
