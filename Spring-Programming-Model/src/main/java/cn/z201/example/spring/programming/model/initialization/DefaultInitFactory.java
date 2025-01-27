package cn.z201.spring.initialization;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * @author z201.coding@gmail.com
 **/
public class DefaultInitFactory implements InitFactory, InitializingBean, DisposableBean {

    public void initBean() {
        System.out.println("初始化 自定义方法 initBean    ...");
    }

    @PostConstruct
    public void init() {
        System.out.println("初始化 @PostConstruct    ...");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("初始化 InitializingBean#afterPropertiesSet ...");
    }

    public void destroyBean() {
        System.out.println("销毁 自定义方法 destroyBean    ...");
    }

    @PreDestroy
    public void doDestroyBean() {
        System.out.println("销毁 @PreDestroy    ...");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("销毁 DisposableBean#destroy    ...");
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("gc  finalize ...");
    }

}
