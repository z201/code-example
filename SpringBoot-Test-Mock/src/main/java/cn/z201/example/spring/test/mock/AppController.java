package cn.z201.example.spring.test.mock;

import cn.hutool.core.lang.Validator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppController {

    @Autowired
    private AppServiceI appService;

    @RequestMapping(value = "{clazz}")
    public Object version(@PathVariable(required = false) String clazz) {
        if (Validator.isNull(clazz)) {
            clazz = "mysql";
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", appService.version(clazz));
        return data;
    }

}
