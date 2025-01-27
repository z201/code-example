package cn.z201.example.spring.dynamic.data.manager;

import org.springframework.jdbc.core.JdbcTemplate;

/**
 * @author z201.coding@gmail.com
 **/
public class DynamicJdbcTemplateManager {

    private DynamicRoutingDataSource dynamicRoutingDataSource;

    public DynamicJdbcTemplateManager(DynamicRoutingDataSource dynamicRoutingDataSource) {
        this.dynamicRoutingDataSource = dynamicRoutingDataSource;
    }

    public DynamicRoutingDataSource getDynamicRoutingDataSource() {
        return dynamicRoutingDataSource;
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
