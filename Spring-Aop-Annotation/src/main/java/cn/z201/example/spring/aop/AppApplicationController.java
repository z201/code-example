package cn.z201.example.spring.aop;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    @RequestMapping(value = "")
    @AnnotationAop(value = "index")
    public Object index() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        return data;
    }

}
