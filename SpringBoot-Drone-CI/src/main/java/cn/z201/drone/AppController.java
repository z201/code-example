package cn.z201.drone;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppController {

    @RequestMapping(value = "")
    public Object index() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", this.getClass().getSimpleName());
        return data;
    }

}
