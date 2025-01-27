package cn.z201.example.spring.mybatis.audit.config.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Aspect
@Order(2)
@Slf4j
@Component
public class MonitorAnnotationAspect {

    private static final String EXECUTION_FIX = "execution (* cn.z201..*..*.*(..)) "
            + "&& @annotation(cn.z201.audit.config.aspect.annotation.MonitorAnnotation)";

    @Autowired(required = false)
    MonitorAnnotationAspectPlugI monitorAnnotationAspectPlugI;

    @Around(EXECUTION_FIX)
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        return monitorAnnotationAspectPlugI.around(joinPoint);
    }

}
