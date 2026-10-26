
package vip.wqs.auth.core.protocol.base;

import me.zhyd.oauth.enums.AuthResponseStatus;
import me.zhyd.oauth.exception.AuthException;
import me.zhyd.oauth.model.AuthResponse;
import me.zhyd.oauth.model.AuthUser;

/**
 * 认证源基础请求接口
 *
 * @author jetox
 * @date 2025/2/8 14:44
 **/
public interface AuthBaseRequest {

    default String authorize(String clientType) {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }

    default AuthResponse<AuthUser> login() {
        throw new AuthException(AuthResponseStatus.NOT_IMPLEMENTED);
    }
}
