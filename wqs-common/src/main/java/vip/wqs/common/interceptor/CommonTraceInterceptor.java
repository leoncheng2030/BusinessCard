
package vip.wqs.common.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;
import vip.wqs.common.util.CommonTraceIdUtil;

/**
 * 公共链路追踪拦截器
 *
 * @author dongxiayu
 * @date 2025/1/10 11:42
 */
public class CommonTraceInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            CommonTraceIdUtil.getTraceId(request);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        CommonTraceIdUtil.clear();
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
