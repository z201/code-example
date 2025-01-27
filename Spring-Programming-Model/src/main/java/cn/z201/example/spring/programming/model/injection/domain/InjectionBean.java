package cn.z201.example.spring.programming.model.injection.domain;

/**
 * @author z201.coding@gmail.com
 **/
public class InjectionBean {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "InjectionBean{" + "name='" + name + '\'' + '}';
    }

}
