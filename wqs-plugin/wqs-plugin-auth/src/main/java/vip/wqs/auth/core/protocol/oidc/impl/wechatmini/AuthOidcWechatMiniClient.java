
package vip.wqs.auth.core.protocol.oidc.impl.wechatmini;

import com.xkcoding.http.HttpUtil;
import com.xkcoding.http.support.hutool.HutoolImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhyd.oauth.config.AuthConfig;
import me.zhyd.oauth.request.AuthRequest;
import me.zhyd.oauth.request.AuthWechatMiniProgramRequest;
import vip.wqs.auth.core.protocol.oidc.AuthOidcBaseJson;


/**
 * OIDC认证源微信小程序客户端
 *
 * @author jetox
 * @date 2024/11/8 15:41
 **/
@Slf4j
public record AuthOidcWechatMiniClient(AuthOidcBaseJson authOidcBaseJson) {

    public AuthRequest getAuthRequest() {
        return getAuthRequest(true);
    }

    public AuthRequest getAuthRequest(boolean ignoreCheckState) {
        String clientId = authOidcBaseJson.getClientId();
        String clientSecret = authOidcBaseJson.getClientSecret();
        HttpUtil.setHttp(new HutoolImpl());
        return new AuthWechatMiniProgramRequest(AuthConfig.builder()
                .clientId(clientId)
                .clientSecret(clientSecret)
                .ignoreCheckRedirectUri(true)
                .ignoreCheckState(ignoreCheckState)
                .build());
    }
}
