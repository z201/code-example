package cn.z201.spring;

import cn.z201.spring.initialization.DefaultInitFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class InitBeanInitializationTest {

    @Test
    public void setUp(){
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.register(InitBeanInitializationTest.class);
        // 启动上下文
        config.refresh();
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    /**
     * 单一类型集合查找
     *
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, DefaultInitFactory> beansMap = listableBeanFactory.getBeansOfType(DefaultInitFactory.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value 作为对象
            beansMap.forEach((key, value) -> {
                System.out.println("单一类型集合查找  " + key + " " + value);
            });
        }
    }

    @Bean(initMethod = "initFactory")
    public DefaultInitFactory initialization(){
        return new DefaultInitFactory();
    }

}
