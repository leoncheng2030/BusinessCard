
package vip.wqs.common.aspect;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.springframework.stereotype.Component;
import vip.wqs.common.util.CommonTraceIdUtil;

/**
 * 自定义traceId转换器
 *
 * @author dongxiayu
 * @date 2025/1/9 15:36
 */
@Component
public class CustomTraceIdConverter extends ClassicConverter {

    // 使用常量避免重复创建字符串
    private static final String LEFT_BRACKET = "[";
    private static final String RIGHT_BRACKET = "]";

    // 缓存StringBuilder以减少对象创建
    private static final ThreadLocal<StringBuilder> bufferHolder =
            ThreadLocal.withInitial(() -> new StringBuilder(64));

    @Override
    public String convert(ILoggingEvent event) {
        StringBuilder buffer = bufferHolder.get();
        buffer.setLength(0);
        String traceId = CommonTraceIdUtil.getTraceId();

        buffer.append(LEFT_BRACKET);
        if (traceId != null && !traceId.isEmpty()) {
            buffer.append(traceId);
        } else {
            buffer.append(event.getThreadName());
        }
        buffer.append(RIGHT_BRACKET);
        return buffer.toString();
    }
}