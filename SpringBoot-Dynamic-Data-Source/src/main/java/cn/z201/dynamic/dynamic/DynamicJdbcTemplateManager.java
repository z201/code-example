package cn.z201.dynamic.dynamic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

/**
 * @author z201.coding@gmail.com
 **/
//@Component
public class DynamicJdbcTemplateManager {

    private DynamicRoutingDataSource dynamicRoutingDataSource;

    @Autowired
    public DynamicJdbcTemplateManager(DynamicRoutingDataSource dynamicRoutingDataSource) {
        this.dynamicRoutingDataSource = dynamicRoutingDataSource;
    }

    /**
     * 获取默认的数据源
     * @return
     */
    public JdbcTemplate jdbcTemplate() {
        return new JdbcTemplate(dynamicRoutingDataSource.getResolvedDefaultDataSource());
    }

    /**
     * 动态jdbcTemplate
     * @return
     */
    public JdbcTemplate dynamicJdbcTemplate() {
        return new JdbcTemplate(dynamicRoutingDataSource.determineTargetDataSource());
    }

}
