package cn.z201.spring;

import cn.z201.spring.annotation.Point;
import cn.z201.spring.domain.AnnotationDelayLookup;
import cn.z201.spring.domain.DelayLookup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;


public class IOCDelayLookupTest {

    @Test
    public void testLookup() {
        // 编辑 ioc-delay-lookup-context.xml 并创建对应bean。
        // 启动 spring 上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/ioc-delay-lookup-context.xml");
        lookup(beanFactory);
        lookupLazy(beanFactory);
        lookupType(beanFactory);
        lookupCollectionType(beanFactory);
        lookupAnnotationType(beanFactory);
    }

    /**
     * 实时查找
     *
     * @param beanFactory
     */
    private void lookup(BeanFactory beanFactory) {
        // 根据id 获取bean
        DelayLookup delayLookup = (DelayLookup) beanFactory.getBean("delayLookup");
        // 重写 DelayLookup.toString()方法成功输出 spring 。
        System.out.println("实时查找 " + delayLookup);
    }

    /**
     * 延时查找
     *
     * @param beanFactory
     */
    private void lookupLazy(BeanFactory beanFactory) {
        // 根据id 获取bean
        ObjectFactory<DelayLookup> delayLookup = (ObjectFactory<DelayLookup>) beanFactory.getBean("objectFactory");
        // 重写 DelayLookup.toString()方法成功输出 spring 。
        System.out.println("延时查找 " + delayLookup.getObject());
    }

    /**
     * 单一类型查找
     *
     * @param beanFactory
     */
    private void lookupType(BeanFactory beanFactory) {
        // 根据id 获取bean
        DelayLookup delayLookup = beanFactory.getBean(DelayLookup.class);
        // 重写 DelayLookup.toString()方法成功输出 spring 。
        System.out.println("单一类型查找  " + delayLookup);
    }


    /**
     * 单一类型集合查找
     *
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, DelayLookup> delayLookupMap = listableBeanFactory.getBeansOfType(DelayLookup.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value 作为对象
            delayLookupMap.forEach((key, value) -> {
                System.out.println("单一类型集合查找  " + key + " " + value);
            });
        }
    }

    /**
     * Java注解查找
     *
     * @param beanFactory
     */
    private void lookupAnnotationType(BeanFactory beanFactory) {
        // Point
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, AnnotationDelayLookup> delayLookupMap = (Map) listableBeanFactory.getBeansWithAnnotation(Point.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value 作为对象
            delayLookupMap.forEach((key, value) -> {
                System.out.println("Java注解 @Point 查找  " + key + " " + value);
            });
        }

    }

}