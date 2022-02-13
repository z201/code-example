package cn.z201.spring;

import cn.z201.spring.beans.SpringBeans;
import cn.z201.spring.initialization.DefaultInitFactory;
import cn.z201.spring.initialization.InitBean;
import cn.z201.spring.initialization.InitFactory;
import cn.z201.spring.injection.domain.SuperInjectionBean;
import org.junit.jupiter.api.Test;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Objects;


public class SpringBeansTest {

    /**
     * 1. 定义 Spring Bean -> this#createBeanDefinition
     * 2. BeanDefinition 元信息 -> this#createBeanDefinitionAlias
     * 3. 命名 Spring Bean -> this
     * - 可以自定义Spring Bean的名称，也可以使用Spring默认生成Bean名称。
     * - 在xml配置中自定义比较常见，在注解中大多使用默认生成。
     * 4. Spring Bean 别名
     * - alias 别名,通过beanName 和 beanNameAlias 获取的实例实际上是一个Bean。
     * 5. 注册Spring Bean -> IocContainerTest
     * -  DefaultListableBeanFactory 基于xml配置信息创建bean
     * -  AnnotationConfigApplicationContext 基于注解类创建bean
     * - 基于@Bean
     * - 基于@Component
     * - 基于@Import
     * - 外部Bean创建
     * 6. 实例化 Spring Bean
     * - Bean 初始化 BeanInitialization -> InitBeanInitializationTest
     * - @PostConstruct 标注 public void method()
     * - 实现InitializationBean接口 afterPropertiesSet()
     * - 指定@Bean init-method 方法名称。
     * - 执行顺序不变 PostConstruct
     * afterPropertiesSet()
     * init-method
     * - AbstractBeanDefinition#setInitMethodName
     * 8. 延迟初始化 Spring Bean
     * - @Lazy(true or false)
     * - xml lazy-init
     * 9. 销毁 Spring Bean ->InitBeanInitializationTest
     * -  @PreDestroy
     * -  DisposableBean#destroy
     * -  指定@Bean destroy-method 方法名称。
     * 10. 垃圾回收 Spring Bean
     * - Object#finalize() 方法
     */
    @Test
    public void setUp() {

    }

    /**
     * BeanDefinition Bean元信息
     */
    @Test
    public void createBeanDefinition() {
        // 1.通过BeanDefinitionBuilder创建
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(SpringBeans.class);
        beanDefinitionBuilder.addPropertyValue("name", "spring");
        beanDefinitionBuilder.addPropertyValue("address", "杭州");
        BeanDefinition beanDefinition = beanDefinitionBuilder.getBeanDefinition();
        System.out.println("获取beanDefinition 实例信息 \n" + beanDefinition);

        // 2.通过AbstractBeanDefinition创建
        AbstractBeanDefinition abstractBeanDefinition = new GenericBeanDefinition();
        abstractBeanDefinition.setBeanClass(SpringBeans.class);
        MutablePropertyValues mutablePropertyValues = new MutablePropertyValues();
        mutablePropertyValues.add("address", "杭州").add("name", "spring");
        abstractBeanDefinition.setPropertyValues(mutablePropertyValues);
        System.out.println("获取abstractBeanDefinition 实例信息 \n" + abstractBeanDefinition);

        // DefaultBeanNameGenerator：默认通用 BeanNameGenerator 实现
        // AnnotationBeanNameGenerator：基于注解扫描的 BeanNameGenerator 实现。在使用@Component 相关派生注解扫描生成。
    }

    /**
     * Bean的别名
     */
    @Test
    public void createBeanDefinitionAlias() {
        BeanFactory beanFactory = new ClassPathXmlApplicationContext("classpath:META-INF/bean-definition-context.xml");
        SuperInjectionBean superInjectionBean = beanFactory.getBean("superInjectionBean", SuperInjectionBean.class);
        SuperInjectionBean superInjectionBeanAlias = beanFactory.getBean("superInjectionBean-alias", SuperInjectionBean.class);
        // 这里输出true 通过beanName 和 beanNameAlias 获取的实例实际上是一个。
        System.out.println(Objects.equals(superInjectionBeanAlias, superInjectionBean));
    }

    /**
     * 注册外部Bean
     */
    @Test
    public void createExternalBean() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        // 创建一个对象
        InitFactory initBean = new DefaultInitFactory();
        // 获取BeanFactory
        ConfigurableListableBeanFactory configurableListableBeanFactory = config.getBeanFactory();
        // 通过beanFactory#registerSingleton 注册外部Bean
        configurableListableBeanFactory.registerSingleton("initBean",initBean);
        config.refresh();
        // 通过依赖查找获取bean信息
        InitFactory externalBean = config.getBean("initBean",InitFactory.class);
        System.out.println("externalBean "+ externalBean);
        config.close();
    }


}