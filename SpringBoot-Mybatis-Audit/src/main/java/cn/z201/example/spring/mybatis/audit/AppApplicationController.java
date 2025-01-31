
package cn.z201.example.spring.mybatis.audit;

import cn.z201.example.spring.mybatis.audit.config.aspect.annotation.MonitorAnnotation;
import cn.z201.example.spring.mybatis.audit.domain.param.ApiParam;
import cn.z201.example.spring.mybatis.audit.service.AuditServiceI;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    private final AuditServiceI auditService;

    @Autowired
    public AppApplicationController(AuditServiceI auditService) {
        this.auditService = auditService;
    }

    /**
     * 无参数
     * @return
     */
    @MonitorAnnotation(audit = true, type = "查看", title = "首页")
    @RequestMapping(value = "")
    public Object index() {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        return data;
    }

    /**
     * url占位符
     * @param key
     * @return
     */
    @RequestMapping(value = "/add{key}")
    public Object add(@PathVariable(required = false) String key) {
        if (Objects.isNull(key)) {
            key = Strings.EMPTY;
        }
        auditService.test(key);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", key);
        return data;
    }

    /**
     * url参数
     * @param apiParam
     * @return
     */
    @MonitorAnnotation(audit = true, type = "查看", title = "api", descriptionExpression = "解析参数 #{[0].id}")
    @RequestMapping(value = "/path")
    public Object path(ApiParam apiParam) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", apiParam);
        return data;
    }

    /**
     * json参数
     * @param apiParam
     * @return
     */
    @MonitorAnnotation(audit = true, type = "查看", title = "api", descriptionExpression = "解析json #{[0].id}")
    @RequestMapping(value = "/api")
    public Object api(@RequestBody ApiParam apiParam) {
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("data", apiParam);
        return data;
    }

}
