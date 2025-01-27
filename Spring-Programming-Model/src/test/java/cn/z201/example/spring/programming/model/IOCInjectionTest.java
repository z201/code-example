package cn.z201.example.spring.programming.model;

import cn.z201.example.spring.programming.model.injection.domain.InjectionBean;
import cn.z201.example.spring.programming.model.injection.repository.InjectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public class IOCInjectionTest {

    @Test
    public void testInjection() {
        // 编辑 ioc-delay-lookup-context.xml 并创建对应bean。
        // 启动 spring 上下文
        BeanFactory beanFactory = new ClassPathXmlApplicationContext(
                "classpath:META-INF/ioc-delay-injection-context.xml");
        lookupType(beanFactory);
        lookupCollectionType(beanFactory);
    }

    /**
     * 单一对象注入
     * @param beanFactory
     */
    private void lookupType(BeanFactory beanFactory) {
        // 根据id 获取bean
        InjectionBean injectionBean = beanFactory.getBean(InjectionBean.class);
        // 重写 DelayLookup.toString()方法成功输出 spring 。
        System.out.println("单一对象注入  " + injectionBean);
    }

    /**
     * 集合对象注入
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, InjectionRepository> delayLookupMap = listableBeanFactory
                    .getBeansOfType(InjectionRepository.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value
            // 作为对象
            delayLookupMap.forEach((key, value) -> {
                System.out.println("集合对象注入  " + key + " " + value);
            });
        }
    }

}
