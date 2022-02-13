package cn.z201.spring;

import cn.z201.spring.initialization.DefaultInitFactory;
import cn.z201.spring.initialization.InitBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;

/**
 * @author z201.coding@gmail.com
 * <p>
 * BeanFactory#getBean 会抛出bean不存在异常
 * ObjectFactory#getObject 会抛出bean不存在异常
 * ObjectProvider#getIfAvailable 不抛出bean不存在异常
 * ListableBeanFactory#getBeansOfType 不抛出bean不存在异常
 * ObjectProvider#stream 不抛出bean不存在异常
 **/
public class ObjectProviderTest {

    @Test
    public void setUp() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.register(ObjectProviderTest.class);
        // 启动上下文
        config.refresh();
        lookupByObjectProvider(config);
        lookupCollectionType(config);
        // BeanFactory#getBean 演示操作，找不到定义需求相关Bean报错
        lookupBeanFactoryGetBean(config);
        // ObjectFactory#getObject 演示操作，找不到定义需求相关Bean报错
        lookupByObjectProviderGetBean(config);
        // ObjectProvider#getIfAvailable 安全  找不到定义需求相关Bean不会报错
        lookupByObjectProviderGetIfAvailable(config);
        // ListableBeanFactory#getBeansOfType 安全 找不到定义需求相关Bean不会报错
        lookupByListableBeanFactoryGetBeansOfType(config);
        // ObjectProvider#stream 安全  找不到定义需求相关Bean不会报错
        lookupByObjectProviderStream(config);
        config.close();
    }

    private void lookupBeanFactoryGetBean(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        execException("lookupBeanFactoryGetBean", () -> annotationConfigApplicationContext.getBean(InitBean.class));
    }

    private void lookupByObjectProviderGetBean(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        ObjectProvider<InitBean> objectProvider = annotationConfigApplicationContext.getBeanProvider(InitBean.class);
        execException("lookupByObjectProviderGetBean", () -> objectProvider.getObject());
    }

    private void lookupByObjectProviderGetIfAvailable(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        ObjectProvider<InitBean> objectProvider = annotationConfigApplicationContext.getBeanProvider(InitBean.class);
        execException("lookupByObjectProviderGetIfAvailable", () -> objectProvider.getIfAvailable(InitBean::createInitBean));
    }

    private void lookupByListableBeanFactoryGetBeansOfType(ListableBeanFactory listableBeanFactory) {
        execException("lookupByListableBeanFactoryGetBeansOfType", () -> listableBeanFactory.getBeansOfType(InitBean.class));
    }

    private void lookupByObjectProviderStream(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        ObjectProvider<InitBean> objectProvider = annotationConfigApplicationContext.getBeanProvider(InitBean.class);
        execException("lookupByObjectProviderStream", () -> objectProvider.stream().forEach(System.out::println));
    }

    /**
     * Bean 的名称默认是 method名称
     *
     * @return
     */
    @Bean
    public String bean() {
        return "hello bean";
    }

    /**
     * 基于 ObjectProvider 获取bean
     * ObjectProvider 是间接的依赖查找，ObjectProvider 相当于一个代理，实际依赖查找发生在ObjectProvider#get 等方法时。
     *
     * @param annotationConfigApplicationContext
     */
    public void lookupByObjectProvider(AnnotationConfigApplicationContext annotationConfigApplicationContext) {
        ObjectProvider<String> objectProvider = annotationConfigApplicationContext.getBeanProvider(String.class);
        System.out.println(objectProvider.getObject());
    }

    /**
     * 单一类型集合查找
     * ListableBeanFactory可以通过某个类型去查找一个集合的列表，集合列表可能有两种情况，一种是查询Bean的名称、
     * 还有一种是查询Bean的实例。推荐使用Bean的名称来判断Bean是否存在，这种方式可以避免提早初始化Bean，产生一些不确定的因素。
     * 对于集合类型依赖查找，通过BeanFactory#getBeanNamesForType 和BeanFactory#getBeansForType，
     * 两个依赖查找方法，前者不会强制bean的初始化，而是通过BeanDefinition和FactoryBean的getClassType进行判断；后者会强制Bean的初始化。
     *
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            System.out.println("getBeanDefinitionCount " + listableBeanFactory.getBeanDefinitionCount());
            for (String beanDefinitionName : listableBeanFactory.getBeanDefinitionNames()) {
                System.out.println(beanDefinitionName);
            }
        }
    }

    private void execException(String from, Runnable runnable) {
        System.out.println("from : " + from);
        try {
            runnable.run();
        } catch (BeansException throwable) {
            System.err.println("trace : " + throwable.getMessage());
        }
    }

}
