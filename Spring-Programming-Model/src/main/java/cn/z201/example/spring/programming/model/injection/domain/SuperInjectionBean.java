package cn.z201.example.spring.programming.model.injection.domain;

/**
 * @author z201.coding@gmail.com
 **/
public class SuperInjectionBean extends InjectionBean {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "SuperInjectionBean{" + "address='" + address + '\'' + "} " + super.toString();
    }

}
