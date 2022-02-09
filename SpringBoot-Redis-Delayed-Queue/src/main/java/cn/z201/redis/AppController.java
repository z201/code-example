package cn.z201.redis;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppController {

    @Autowired
    private DelayOrderImpl delayOrder;

    @RequestMapping(value = "add/{id}")
    public Object add(@PathVariable(required = true) Long id) {
        if (id > 500) {
            id = 500L;
        }
        for (Integer i = 0; i < id; i++) {
            OrderBo orderBo = OrderBo.builder()
                    .id(RandomUtil.randomLong(10,1000))
                    .createTime(DateTool.currentTimeMillis())
                    .orderDeadlineTime(DateTool.currentTimeMillis() + 1 * 60 * 1000).build();
            delayOrder.addToDelayQueue(orderBo);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", delayOrder.size());
        return data;
    }

    @RequestMapping(value = "list")
    public Object list() {
        List<OrderBo> list = delayOrder.all();
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("size", list.size());
        data.put("data",list);
        return data;
    }


    @RequestMapping(value = "del/{id}")
    public Object del(@PathVariable(required = false) Long id) {
        delayOrder.removeToOrderDelayQueueById(id);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", delayOrder.size());
        return data;
    }

}
