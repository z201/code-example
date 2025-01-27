package cn.z201.example.spring.aop.log.config.aspect;

import cn.z201.example.spring.aop.log.config.aspect.annotation.MonitorAnnotation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author z201.coding@gmail.com
 * @date 2020-09-10 拦截器扩展
 **/
public interface MonitorAnnotationAspectPlugI {

    /**
     * 鉴权处理
     * @param monitorAnnotation
     * @param request
     * @param httpServletResponse
     */
    void authentication(MonitorAnnotation monitorAnnotation, HttpServletRequest request,
            HttpServletResponse httpServletResponse);

    /**
     * 前置处理
     * @param monitorAnnotation
     * @param request
     * @param httpServletResponse
     */
    void before(MonitorAnnotation monitorAnnotation, HttpServletRequest request,
            HttpServletResponse httpServletResponse);

    /**
     * 后置处理
     * @param monitorAnnotation
     * @param request
     * @param httpServletResponse
     */
    void after(MonitorAnnotation monitorAnnotation, HttpServletRequest request,
            HttpServletResponse httpServletResponse);

}
