package cn.z201.example.spring.mybatis.audit.utils;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @author z201.coding@gmail.com
 **/
public class AnnotationTools {

    /**
     * 获取方法信息
     * @param joinPoint
     * @return
     * @throws NoSuchMethodException
     */
    public static Method getDeclaredAnnotation(JoinPoint joinPoint) {
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
        }
        catch (NoSuchMethodException e) {
            return null;
        }
        // 拿到方法定义的注解信息
        return objMethod;
    }

}
