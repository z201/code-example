package cn.z201.example.spring.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author z201.coding@gmail.com
 **/
@Component
@Aspect
@Slf4j
public class LogsAspect {

    @Pointcut("@annotation(cn.z201.example.spring.aop.AnnotationAop)")
    private void cutMethod() {

    }

    /**
     * 异常通知：目标方法抛出异常时执行
     */
    @AfterThrowing("cutMethod()")
    public void afterThrowing(JoinPoint joinPoint) {
        log.info("after throwing");
    }

    /**
     * 环绕通知：灵活自由的在目标方法中切入代码
     */
    @Before("cutMethod()")
    public void before(JoinPoint joinPoint) throws Throwable {
        // 获取目标方法的名称
        String methodName = joinPoint.getSignature().getName();
        // 获取方法传入参数
        Object[] params = joinPoint.getArgs();
        if (null != params && params.length != 0) {
            log.info(" method name {} args {}", methodName, params[0]);
        }
        // 执行源方法
        AnnotationAop annotationAop = getDeclaredAnnotation(joinPoint);
        if (null != annotationAop) {
            log.info(" annotationAop value {} ", annotationAop.value());
        }
    }

    /**
     * 获取方法中声明的注解
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public AnnotationAop getDeclaredAnnotation(JoinPoint joinPoint) throws NoSuchMethodException {
        // 获取方法名
        String methodName = joinPoint.getSignature().getName();
        // 反射获取目标类
        Class<?> targetClass = joinPoint.getTarget().getClass();
        // 拿到方法对应的参数类型
        Class<?>[] parameterTypes = ((MethodSignature) joinPoint.getSignature()).getParameterTypes();
        // 根据类、方法、参数类型（重载）获取到方法的具体信息
        Method objMethod = targetClass.getMethod(methodName, parameterTypes);
        // 拿到方法定义的注解信息
        AnnotationAop annotation = objMethod.getDeclaredAnnotation(AnnotationAop.class);
        // 返回
        return annotation;
    }

}
