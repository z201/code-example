package cn.z201.spring.initialization;

/**
 * @author z201.coding@gmail.com
 **/
public interface InitFactory {

    default InitBean initFactory(){
        return InitBean.createInitBean();
    }
}
