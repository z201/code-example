package cn.z201.spring;

import cn.z201.spring.domain.UserBeans;
import org.junit.jupiter.api.Test;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyEditorSupport;
import java.util.Objects;
import java.util.stream.Stream;

public class UserBeansTest {

    /**
     * Java Beans 实现IOC的方式
     * 1.PropertyEditor 将一个字符串内容，通某些中间手段的转换，转换成你想要的类型。
     * 在贫血模型情况下不再使用read / write ，而是交给PropertyEditorClass子类处理。
     * @throws IntrospectionException
     */
    @Test
    public void setup1() throws IntrospectionException {
        BeanInfo beanInfo = Introspector.getBeanInfo(UserBeans.class, Object.class);
        Stream.of(beanInfo.getPropertyDescriptors()).forEach(i -> {
            if (Objects.equals("age",i.getName())) {
                i.setPropertyEditorClass(StringToIntegerPropertyEditor.class);
            }
            System.out.println(i);
        });
    }

    static class StringToIntegerPropertyEditor extends PropertyEditorSupport {
        public void setAsText(String text) throws java.lang.IllegalArgumentException {
            Integer value = Integer.valueOf(text);
            // 方便getValue
            setValue(value);
        }
    }


}