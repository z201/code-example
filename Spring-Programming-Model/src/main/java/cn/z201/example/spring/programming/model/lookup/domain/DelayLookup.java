package cn.z201.example.spring.programming.model.lookup.domain;

/**
 * @author z201.coding@gmail.com spring ioc 延时查找
 **/
public class DelayLookup {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "DelayLookup{" + "name='" + name + '\'' + '}';
    }

}
