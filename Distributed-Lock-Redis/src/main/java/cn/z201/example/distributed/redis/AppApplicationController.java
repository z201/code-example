package cn.z201.example.distributed.redis;

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
public class AppApplicationController {

    @Autowired
    DistributedLockRedisTool distributedLockRedisTool;

    @RequestMapping(value = "{id}")
    public Object index(@PathVariable(required = false) String id) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        if (null == id) {
            data.put("data", "请求参数不合法");
        } else {
            data.put("data", distributedLockRedisTool.lock(id, id, 10000L));
        }
        return data;
    }
}
