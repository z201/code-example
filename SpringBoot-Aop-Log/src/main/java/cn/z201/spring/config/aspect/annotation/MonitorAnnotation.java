package cn.z201.spring.config.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Target(value = {ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorAnnotation {

    /**
     * 模块
     */
    String title() default "";

    /**
     * 是否保存请求的参数
     */
    boolean showRequestData() default true;

    /**
     * 是否保存响应的参数
     */
    boolean showResponseData() default true;

    /**
     * 是否身份检查
     *
     * @return
     */
    boolean auth() default true;

    /**
     * 预警发送谁
     *
     * @return
     */
    String[] alarm() default "";

    /**
     * 超时时间 0 的时候无效
     *
     * @return
     */
    long timeout() default 0L;

}
