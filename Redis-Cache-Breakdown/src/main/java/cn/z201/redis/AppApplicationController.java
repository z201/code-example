package cn.z201.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * @author z201.coding@gmail.com
 * @date 2022/1/7
 **/
@RestController
public class AppApplicationController {

    @Autowired
    AppApplicationServiceImpl appApplicationService;

    @RequestMapping(value = "{id}")
    public Object index(@PathVariable(required = false) String id) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        if (null == id) {
            data.put("data", "请求参数不合法");
        }else{
            data.put("data", appApplicationService.findCacheById(id,1000L));
        }
        return data;
    }
}
