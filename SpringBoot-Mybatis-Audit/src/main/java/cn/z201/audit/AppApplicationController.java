
package cn.z201.audit;

import cn.z201.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.audit.domain.param.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    @MonitorAnnotation(audit = true, type = "查看", title = "首页")
    @RequestMapping(value = "")
    public Object index() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        return data;
    }

    @MonitorAnnotation(audit = true, type = "查看", title = "首页", descriptionExpression = "#{[0]}")
    @RequestMapping(value = "/add{key}")
    public Object add(@PathVariable(required = false) String key) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", key);
        return data;
    }


    @MonitorAnnotation(audit = true, type = "查看", title = "api", descriptionExpression = "解析json #{[0].id}")
    @RequestMapping(value = "/path")
    public Object path(ApiParam apiParam) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", apiParam);
        return data;
    }

    @MonitorAnnotation(audit = true, type = "查看", title = "api", descriptionExpression = "解析json #{[0].id}")
    @RequestMapping(value = "/api")
    public Object api(@RequestBody ApiParam apiParam) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", apiParam);
        return data;
    }

}
