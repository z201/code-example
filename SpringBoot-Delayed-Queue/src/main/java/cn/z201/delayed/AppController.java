package cn.z201.delayed;

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
    public Object add(@PathVariable(required = false) Long id) {
        if (null == id) {
            id = RandomUtil.randomLong(10,1000);
        }
        OrderBo orderBo = OrderBo.builder()
                .id(id)
                .createTime(DateTool.currentTimeMillis())
                .orderDeadlineTime(DateTool.currentTimeMillis() + 1 * 60 * 1000).build();
        delayOrder.addToDelayQueue(orderBo);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", orderBo);
        return data;
    }

    @RequestMapping(value = "list")
    public Object list() {
        List<OrderBo> list = delayOrder.all();
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data",list);
        return data;
    }


    @RequestMapping(value = "del/{id}")
    public Object del(@PathVariable(required = false) Long id) {
        delayOrder.removeToOrderDelayQueueById(id);
        List<OrderBo> list = delayOrder.all();
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", list);
        return data;
    }

}
