package cn.z201.spring.initialization;

import org.springframework.beans.factory.FactoryBean;

/**
 * @author z201.coding@gmail.com
 **/
public class InitBeanFactory implements FactoryBean {
    @Override
    public Object getObject() throws Exception {
        return InitBean.createInitBean();
    }

    @Override
    public Class<?> getObjectType() {
        return InitBean.class;
    }
}
