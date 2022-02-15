package cn.z201.audit.config.mdc;

import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.MDC;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

/**
 * @author z201.coding@gamil.com
 */
@Slf4j
public class MdcTraceContextFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String readIp = request.getHeader(MdcApiConstant.X_REAL_IP);
        String appTraceId = request.getHeader(MdcApiConstant.HTTP_HEADER_TRACE_ID);
        String authorization = request.getHeader(MdcApiConstant.HTTP_TOKEN_HEADER);
        String tenant = request.getHeader(MdcApiConstant.APP_TENANT);
        String uid = request.getHeader(MdcApiConstant.APP_USER_ID);
        /**
         * 没有设置就设置下,设置了就直接返回。注意这里必须提前在拦截器中设置好，不然会失效。
         */
        if (Objects.isNull(appTraceId)) {
            appTraceId = MDC.get(MdcApiConstant.HTTP_HEADER_TRACE_ID);
            if (Strings.isEmpty(appTraceId)) {
                appTraceId = MdcTool.getInstance().getAndCreate();
            }
            request.setAttribute(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        }
        if (Objects.isNull(authorization)) {
            authorization = Strings.EMPTY;
        }
        if (Objects.isNull(tenant)) {
            tenant = Strings.EMPTY;
        }
        if (Objects.isNull(readIp)) {
            readIp = Strings.EMPTY;
        }
        if (Objects.isNull(uid)) {
            uid = Strings.EMPTY;
        }
        MDC.put(MdcApiConstant.X_REAL_IP, readIp);
        MDC.put(MdcApiConstant.HTTP_HEADER_TRACE_ID, appTraceId);
        MDC.put(MdcApiConstant.APP_TENANT, tenant);
        MDC.put(MdcApiConstant.HTTP_TOKEN_HEADER, authorization);
        MDC.put(MdcApiConstant.APP_USER_ID, uid);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
        MdcTool.getInstance().remote();
    }

}

