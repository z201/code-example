package cn.z201.example.spring.programming.model.initialization;

/**
 * @author z201.coding@gmail.com
 **/
public class InitBean {

    private String name;

    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static InitBean createInitBean() {
        InitBean initBean = new InitBean();
        initBean.setName("spring");
        initBean.setAddress("杭州");
        return initBean;
    }

    @Override
    public String toString() {
        return "InitBean{" + "name='" + name + '\'' + ", address='" + address + '\'' + '}';
    }

}
