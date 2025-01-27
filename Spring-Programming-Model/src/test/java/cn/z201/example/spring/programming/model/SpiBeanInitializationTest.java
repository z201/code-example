package cn.z201.example.spring.programming.model;

import cn.z201.example.spring.programming.model.initialization.DefaultInitFactory;
import cn.z201.example.spring.programming.model.initialization.InitBean;
import cn.z201.example.spring.programming.model.initialization.InitFactory;
import cn.z201.example.spring.programming.model.initialization.ServiceLoaderFactoryBeanConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * @author z201.coding@gmail.com
 **/
public class SpiBeanInitializationTest {

    /**
     * 基于 ServiceLoader spi机制 ServiceLoader<InitFactory> serviceLoader =
     * ServiceLoader.load(InitFactory.class,
     * Thread.currentThread().getContextClassLoader());
     */
    @Test
    public void setUpServiceLoader() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        // 启动上下文
        config.refresh();
        ServiceLoader<InitFactory> serviceLoader = ServiceLoader.load(InitFactory.class,
                Thread.currentThread().getContextClassLoader());
        displayServiceLoader(serviceLoader);
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    /**
     * 基于 ServiceLoaderFactoryBean spring 对 jdk spi机制封装 ServiceLoader<InitFactory>
     * serviceLoader = config.getBean("initBeanFactoryServiceLoaderFactoryBean",
     * ServiceLoader.class);
     */
    @Test
    public void setUpServiceLoaderFactoryBean() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.register(ServiceLoaderFactoryBeanConfig.class);
        // 启动上下文
        config.refresh();
        ServiceLoader<InitFactory> serviceLoader = config.getBean("initBeanFactoryServiceLoaderFactoryBean",
                ServiceLoader.class);
        displayServiceLoader(serviceLoader);
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    private void displayServiceLoader(ServiceLoader<InitFactory> serviceLoader) {
        Iterator<InitFactory> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            InitFactory initFactory = iterator.next();
            System.out.println("initBeanFactory " + initFactory);
        }
    }

    /***
     * 基于AutowireCapableBeanFactory#createBean 创建
     */
    @Test
    public void setUpAutowireCapableBeanFactoryByCreateBean() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.refresh();
        AutowireCapableBeanFactory autowireCapableBeanFactory = config.getAutowireCapableBeanFactory();
        DefaultInitFactory defaultInitFactory = autowireCapableBeanFactory.createBean(DefaultInitFactory.class);
        System.out.println("defaultInitFactory " + defaultInitFactory);
        // 关闭上下文
        config.close();
    }

    @Test
    public void setUpBeanDefinitionBuilderByCreateBean() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        registerBeanDefinition(config, "initBean", InitBean.class);
        config.refresh();
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    private void registerBeanDefinition(BeanDefinitionRegistry registry, String beanName, Class<?> beanClass) {
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        beanDefinitionBuilder.addPropertyValue("name", "spring");
        beanDefinitionBuilder.addPropertyValue("address", "杭州");
        registry.registerBeanDefinition(beanName, beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 单一类型集合查找
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, InitBean> beansMap = listableBeanFactory.getBeansOfType(InitBean.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value
            // 作为对象
            beansMap.forEach((key, value) -> {
                System.out.println("单一类型集合查找  " + key + " " + value);
            });
        }
    }

}
