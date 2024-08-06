package cn.z201.spring.config.aspect;

import cn.hutool.extra.servlet.ServletUtil;
import cn.z201.spring.config.aspect.annotation.MonitorAnnotation;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Aspect
@Order(2)
@Component
public class MonitorAnnotationAspect {

    private static Logger LOGGER = LoggerFactory.getLogger(MonitorAnnotationAspect.class);

    private static final String EXECUTION_FIX = "execution (* cn.z201.spring..*..*.*(..)) " +
            "&& @annotation(cn.z201.spring.config.aspect.annotation.MonitorAnnotation)";

    private static final String LOCALHOST = "0:0:0:0:0:0:0:1";

    @Autowired(required = false)
    private MonitorAnnotationAspectPlugI monitorAnnotationAspectPlugI;

    private Object[] getArgs(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof HttpServletRequest
                    || args[i] instanceof HttpServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        return arguments;
    }

    @Around(EXECUTION_FIX)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        boolean httpRequest = true;
        HttpServletResponse response = null;
        HttpServletRequest request = null;
        Object result = null;
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        if (Objects.isNull(httpServletRequest)) {
            httpRequest = false;
        }
        MonitorAnnotation monitorAnnotation = getDeclaredAnnotation(joinPoint);
        Object[] arguments = getArgs(joinPoint);
        if (httpRequest && null != monitorAnnotation) {
            request = httpServletRequest.getRequest();
            response = httpServletRequest.getResponse();
            // 请求的IP地址
            String ip = ServletUtil.getClientIP(request);
            if (Objects.equals(LOCALHOST,ip)) {
                ip = "127.0.0.1";
            }

            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" ClassPath#Method#Http#Title  :  {}#{}#{}#{}#{}", joinPoint.getSignature().getDeclaringTypeName(),
                        joinPoint.getSignature().getName(),
                        request.getRequestURL().toString(),
                        request.getMethod(),
                        monitorAnnotation.title());
            }
            if (monitorAnnotation.showRequestData()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(" HTTP Request Args   : {}", gson.toJson(arguments));
                }
            }
            if (null != monitorAnnotationAspectPlugI) {
                monitorAnnotationAspectPlugI.authentication(monitorAnnotation, request, response);
                monitorAnnotationAspectPlugI.before(monitorAnnotation, request, response);
                result = joinPoint.proceed();
                monitorAnnotationAspectPlugI.after(monitorAnnotation, request, response);
            } else {
                result = joinPoint.proceed();
            }
            if (monitorAnnotation.showResponseData()) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(" HTTP Response Args   : {}", gson.toJson(result));
                }
            }
            stopWatch.stop();
            long consuming = stopWatch.getTotalTimeMillis();
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" Time-Consuming : {} ms", consuming);
            }
            long time = monitorAnnotation.timeout();
            if (time > 0 && time <= consuming) {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("long enough to execute");
                }
            }
        } else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(" HTTP URL Method  {}#{}",request.getRequestURL().toString(), request.getMethod());
                LOGGER.debug(" Args   : {}", gson.toJson(arguments));
            }
        }
        return result;
    }

    /**
     * 获取方法中声明的注解
     *
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    private MonitorAnnotation getDeclaredAnnotation(JoinPoint joinPoint) {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = null;
        try {
            objMethod = targetClass.getMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            return null;
        }
        // 拿到方法定义的注解信息
        return objMethod.getDeclaredAnnotation(MonitorAnnotation.class);
    }

}
