package cn.z201.example.spring.dynamic.data;

import cn.z201.example.spring.dynamic.data.manager.DynamicJdbcTemplateManager;
import cn.z201.example.spring.dynamic.data.manager.DynamicDataSourceConstant;
import cn.z201.example.spring.dynamic.data.manager.DynamicDataSourceContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    @Autowired
    private DynamicJdbcTemplateManager dynamicJdbcTemplateManager;

    /**
     * 所有数据源
     * @return
     */
    @RequestMapping(value = "")
    public Object index() {
        Set<Object> dataBasesList = DynamicDataSourceContextHolder.getInstance().all();
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("all-db", dataBasesList);
        data.put("db", DynamicDataSourceContextHolder.getInstance().getDataSourceKey());
        return data;
    }

    /**
     * 切换数据源
     * @param key
     * @return
     */
    @RequestMapping(value = "{key}")
    public Object switchDB(@PathVariable(required = false) String key) {
        List<String> dataBasesList = new ArrayList<>();
        if (DynamicDataSourceContextHolder.getInstance().containDataSourceKey(key)) {
            dynamicJdbcTemplateManager.getDynamicRoutingDataSource().toggleDataSource(key);
            dataBasesList = dynamicJdbcTemplateManager.dynamicJdbcTemplate().queryForList("SELECT DATABASE()",
                    String.class);
        }
        else {
            key = DynamicDataSourceConstant.MASTER;
            dataBasesList = dynamicJdbcTemplateManager.jdbcTemplate().queryForList("SELECT DATABASE()", String.class);
        }
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("key", key);
        data.put("db", dataBasesList);
        return data;
    }

}
