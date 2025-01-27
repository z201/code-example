package cn.z201.spring;

import cn.z201.spring.container.IocContainer;
import cn.z201.spring.container.IocImportContainer;
import cn.z201.spring.injection.domain.SuperInjectionBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Map;

/**
 * @author z201.coding@gmail.com
 * 演示
 * DefaultListableBeanFactory 基于xml配置信息创建bean
 * AnnotationConfigApplicationContext 基于注解类创建bean
 * 1.基于@Bean
 * 2.基于@Component
 * 3.基于@Import
 * Spring 会避免重复注册。
 **/
public class IocContainerTest {

    @Test
    public void setupXmlApplicationContext(){
        // 创建BeanFactory 容器
        DefaultListableBeanFactory defaultListableBeanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(defaultListableBeanFactory);
        String location = "classpath:META-INF/ioc-delay-injection-context.xml";
        // 加载 xml格式的配置文件，并获取bean的数量
        int beanDefinitionCount =  reader.loadBeanDefinitions(location);
        System.out.println("bean 定义加载的数量 " + beanDefinitionCount);
        lookupCollectionType(defaultListableBeanFactory);
    }

    @Test
    public void setupAnnotationConfigApplicationContext(){
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        // 1. 通过@Bean -> IocContainer
        // 2. 通过@Component
        config.register(IocContainer.class);
        // 3. 通过@Import
        config.register(IocImportContainer.class);
        // 4.通过命名Bean的方式，如果名称重复Spring会避免重复创建。
        registerBeanDefinition(config,"superInjectionBean", SuperInjectionBean.class);
        // 启动应用上下文
        config.refresh();
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    public void registerBeanDefinition(BeanDefinitionRegistry registry,String beanName,Class<?> beanClass){
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(beanClass);
        beanDefinitionBuilder.addPropertyValue("name", "spring");
        beanDefinitionBuilder.addPropertyValue("address", "杭州");
        registry.registerBeanDefinition(beanName,beanDefinitionBuilder.getBeanDefinition());
    }

    /**
     * 集合对象注入
     *
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, SuperInjectionBean> delayLookupMap = listableBeanFactory.getBeansOfType(SuperInjectionBean.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value 作为对象
            delayLookupMap.forEach((key, value) -> {
                System.out.println("集合对象注入  " + key + " " + value);
            });
        }
    }

}
