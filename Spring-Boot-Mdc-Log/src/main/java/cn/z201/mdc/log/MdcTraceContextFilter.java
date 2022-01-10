package cn.z201.mdc.log;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author z201.coding@gamil.com
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 8)
@ConditionalOnClass(WebMvcConfigurer.class)
@Slf4j
@Component
public class MdcTraceContextFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String readIp = request.getHeader(MdcApiConstant.X_REAL_IP);
        String appTraceId = request.getHeader(MdcApiConstant.HTTP_HEADER_TRACE_ID);
        String authorization = request.getHeader(MdcApiConstant.HTTP_TOKEN_HEADER);
        String tenant = request.getHeader(MdcApiConstant.APP_TENANT);
        /**
         * 没有设置就设置下,设置了就直接返回。注意这里必须提前在拦截器中设置好，不然会失效。
         */
        if (StrUtil.isEmpty(appTraceId)) {
            appTraceId = MDC.get(MdcApiConstant.HTTP_HEADER_TRACE_ID);
            if (Strings.isEmpty(appTraceId)) {
                appTraceId = MdcTool.getInstance().currentTraceId();
            }
            request.setAttribute(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        }
        if (StrUtil.isEmpty(authorization)) {
            authorization = Strings.EMPTY;
        }
        if (StrUtil.isEmpty(tenant)) {
            tenant = Strings.EMPTY;
        }
        if (StrUtil.isEmpty(readIp)) {
            readIp = Strings.EMPTY;
        }
        MDC.put(MdcApiConstant.X_REAL_IP, readIp);
        MDC.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        MDC.put(MdcApiConstant.APP_TENANT, tenant);
        MDC.put(MdcApiConstant.HTTP_TOKEN_HEADER, authorization);
        filterChain.doFilter(request, response);
    }
}

