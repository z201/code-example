package cn.z201.example.spring.programming.model.lookup.domain;

import cn.z201.example.spring.programming.model.lookup.annotation.Point;

/**
 * @author z201.coding@gmail.com
 **/
@Point
public class AnnotationDelayLookup extends DelayLookup {

    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "AnnotationDelayLookup{" + "address='" + address + '\'' + "} " + super.toString();
    }

}
