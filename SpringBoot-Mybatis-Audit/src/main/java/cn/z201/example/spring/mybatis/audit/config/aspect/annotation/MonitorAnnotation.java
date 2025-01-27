package cn.z201.example.spring.mybatis.audit.config.aspect.annotation;

import java.lang.annotation.*;

/**
 * @author z201.coding@gmail.com
 * @date 2020-08-26
 **/
@Target(value = { ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MonitorAnnotation {

    String value() default "";

    /**
     * 审计
     */
    boolean audit() default false;

    /**
     * 事件类型 insert update delete select 等等
     */
    String type() default "";

    /**
     * 事件标题 xxx查看了什么记录 xx修改了什么记录
     */
    String title() default "";

    /**
     * 事件描述
     */
    String descriptionExpression() default "";

}
