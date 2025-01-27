package cn.z201.spring.container;

import cn.z201.spring.injection.domain.SuperInjectionBean;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author z201.coding@gmail.com
 * 基于@Component创建
 **/
@Component
public class IocContainer {

    /**
     * 基于@Bean创建
     * @return
     */
    @Bean
    public SuperInjectionBean superInjectionBean(){
        SuperInjectionBean superInjectionBean = new SuperInjectionBean();
        superInjectionBean.setName("spring");
        superInjectionBean.setAddress("杭州");
        return superInjectionBean;
    }

}
