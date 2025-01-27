package cn.z201.example.mybatis.audit;

import cn.z201.audit.config.aspect.MonitorAnnotationAspectPlugI;
import cn.z201.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.audit.repository.AuditRepository;
import cn.z201.audit.utils.AnnotationTools;
import cn.z201.audit.utils.JsonFormatTool;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Slf4j
public class MonitorAnnotationAspectPlugImpl implements MonitorAnnotationAspectPlugI {

    long startTime;

    @Autowired
    private AuditRepository auditRepository;

    @Override
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        Method method = AnnotationTools.getDeclaredAnnotation(joinPoint);
        MonitorAnnotation monitorAnnotation = method.getDeclaredAnnotation(MonitorAnnotation.class);
        logHttp();
        logArgs(joinPoint);
        Object result = joinPoint.proceed();
        logHttpAfter();
        auditRepository.add(monitorAnnotation,args);
        return result;
    }

    private void logHttp(){
        startTime = System.currentTimeMillis();
        ServletRequestAttributes httpServletRequest = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (null != httpServletRequest) {
            HttpServletRequest request = httpServletRequest.getRequest();
            // 打印请求相关参数
            log.info(" HTTP URL Method :  {}#{}", request.getRequestURL().toString(), request.getMethod());
        }
    }

    private void logHttpAfter(){
        long consuming = System.currentTimeMillis() - startTime;
        log.info(" Time-Consuming : {} ms", consuming);
    }

    private void logArgs(ProceedingJoinPoint joinPoint){
        log.info(" Class Method    :  {}#{}", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName());
        Object[] args = joinPoint.getArgs();
        Object[] arguments = new Object[args.length];
        for (int i = 0; i < args.length; i++) {
            if (args[i] instanceof ServletRequest
                    || args[i] instanceof ServletResponse
                    || args[i] instanceof MultipartFile) {
                continue;
            }
            arguments[i] = args[i];
        }
        log.info(" Args   : {}", JsonFormatTool.toString(arguments));
    }




}
