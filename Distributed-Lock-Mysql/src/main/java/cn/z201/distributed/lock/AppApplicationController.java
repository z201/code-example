package cn.z201.distributed.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author z201.coding@gmail.com
 * @date 2022/1/7
 **/
@RestController
public class AppApplicationController {

    @Autowired
    DistributedLockTool distributedLockTool;

    @RequestMapping(value = "{key}/{value}")
    public Object lock(@PathVariable(value = "key") String key, @PathVariable(value = "value") String value) {
        if (StringUtils.isEmpty(key)) {
            key = UUID.randomUUID().toString();
        }
        if ( StringUtils.isEmpty(value)) {
            value = UUID.randomUUID().toString();
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", distributedLockTool.lock(key, value));
        return data;
    }

    @RequestMapping(value = "del/{key}")
    public Object unlock(@PathVariable(value = "key") String key) {
        if (StringUtils.isEmpty(key)) {
           throw new IllegalArgumentException("参数不正确");
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", distributedLockTool.unLock(key));
        return data;
    }
}
