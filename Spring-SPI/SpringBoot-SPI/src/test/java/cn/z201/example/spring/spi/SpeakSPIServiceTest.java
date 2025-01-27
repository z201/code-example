package cn.z201.example.spring.spi;

import cn.z201.example.jdk.spi.LanguageType;
import cn.z201.example.jdk.spi.LanguageTypeTool;
import cn.z201.example.jdk.spi.SpeakSPIService;
import cn.z201.example.spring.spi.SpeakSPIServiceConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.util.Iterator;
import java.util.Map;
import java.util.ServiceLoader;

class SpeakSPIServiceTest {

    @Test
    public void setUp() {
        SpeakSPIService speakSPIService = LanguageTypeTool.language("chinese");
        if (null != speakSPIService) {
            System.out.println(speakSPIService.echo());
        }
        speakSPIService = LanguageTypeTool.language("english");
        if (null != speakSPIService) {
            System.out.println(speakSPIService.echo());
        }
    }

    @Test
    public void setSpringUp() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.register(SpeakSPIServiceConfig.class);
        // 启动上下文
        config.refresh();
        ServiceLoader<SpeakSPIService> serviceLoader = config.getBean("initBeanFactoryServiceLoaderFactoryBean",
                ServiceLoader.class);
        displayServiceLoader(serviceLoader);
        // 关闭上下文
        config.close();
    }

    private void displayServiceLoader(ServiceLoader<SpeakSPIService> serviceLoader) {
        Iterator<SpeakSPIService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            SpeakSPIService speakSPIService = iterator.next();
            System.out.println("speakSPIService " + speakSPIService.echo());
        }
    }

    @Test
    public void setSpringRegisterBeanUp() {
        // 创建BeanFactory 容器
        AnnotationConfigApplicationContext config = new AnnotationConfigApplicationContext();
        config.register(SpeakSPIServiceConfig.class);
        // 启动上下文
        config.refresh();
        registerBean(config);
        lookupCollectionType(config);
        // 关闭上下文
        config.close();
    }

    /**
     * 循环遍历 {@link ServiceLoader} 获取 根据LanguageType 注解上的信息，作为bean的id。进行注册。
     * @param applicationContext
     */
    private void registerBean(AnnotationConfigApplicationContext applicationContext) {
        ServiceLoader<SpeakSPIService> serviceLoader = applicationContext
                .getBean("initBeanFactoryServiceLoaderFactoryBean", ServiceLoader.class);
        Iterator<SpeakSPIService> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            SpeakSPIService speakSPIService = iterator.next();
            Class clazz = speakSPIService.getClass();
            LanguageType languageType = (LanguageType) clazz.getDeclaredAnnotation(LanguageType.class);
            if (null != languageType) {
                applicationContext.registerBean(languageType.language(), clazz);
            }
        }
    }

    /**
     * 单一类型集合查找
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, SpeakSPIService> beansMap = listableBeanFactory.getBeansOfType(SpeakSPIService.class);
            // getBeansOfType 匹配所有类型的 bean，无论是单例、原型还是 FactoryBean , bean name 作为key value
            // 作为对象
            beansMap.forEach((key, value) -> {
                System.out.println("单一类型集合查找  " + key + " " + value.echo());
            });
        }
    }

}