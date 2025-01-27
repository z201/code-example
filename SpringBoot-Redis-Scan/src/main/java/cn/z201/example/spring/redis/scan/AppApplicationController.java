package cn.z201.example.spring.redis.scan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
@Slf4j
public class AppApplicationController {

    @RequestMapping(value = "{id}")
    public Object index(@PathVariable(required = false) String id) {
        log.info("id", id);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", id);
        return data;
    }

}
