package cn.z201.dynamic;

import cn.z201.dynamic.dynamic.DynamicJdbcTemplateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public class AppApplicationController {

    @Autowired
    private DynamicJdbcTemplateManager dynamicJdbcTemplateManager;

    @RequestMapping(value = "")
    public Object index() {
        List<String> dataBasesList = dynamicJdbcTemplateManager.jdbcTemplate().queryForList("SHOW  DATABASES", String.class);
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("db", dataBasesList.toString());
        return data;
    }
}
