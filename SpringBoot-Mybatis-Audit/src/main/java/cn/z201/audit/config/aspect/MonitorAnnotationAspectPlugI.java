package cn.z201.audit.config.aspect;


import org.aspectj.lang.ProceedingJoinPoint;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-10
 * 拦截器扩展
 **/
public interface MonitorAnnotationAspectPlugI {

    Object around(ProceedingJoinPoint joinPoint) throws Throwable;
}
