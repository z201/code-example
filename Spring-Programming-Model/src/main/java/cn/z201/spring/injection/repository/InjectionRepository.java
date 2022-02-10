package cn.z201.spring.injection.repository;

import cn.z201.spring.injection.domain.InjectionBean;

import java.util.Collection;

/**
 * @author z201.coding@gmail.com
 **/
public class InjectionRepository {

    private InjectionBean bean;

    private Collection<InjectionBean> beans;

    public InjectionBean getBean() {
        return bean;
    }

    public void setBean(InjectionBean bean) {
        this.bean = bean;
    }

    public Collection<InjectionBean> getBeans() {
        return beans;
    }

    public void setBeans(Collection<InjectionBean> beans) {
        this.beans = beans;
    }

    @Override
    public String toString() {
        return "InjectionRepository{" +
                "beans=" + beans +
                '}';
    }
}
