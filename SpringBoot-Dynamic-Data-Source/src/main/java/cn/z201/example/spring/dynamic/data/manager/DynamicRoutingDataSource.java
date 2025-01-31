package cn.z201.example.spring.dynamic.data.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
public class DynamicRoutingDataSource extends AbstractRoutingDataSource {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRoutingDataSource.class);

    @Override
    public DataSource determineTargetDataSource() {
        return super.determineTargetDataSource();
    }

    /**
     * /** 必须执行此操作，才会重新初始化AbstractRoutingDataSource 中的 resolvedDataSources，也只有这样，动态切换才会起效
     * @param defaultTargetDataSource 默认的数据源
     * @param targetDataSources 多数据源每个key对应一个数据源
     */
    public void setDynamicRoutingDataSource(DataSource defaultTargetDataSource, Map<Object, Object> targetDataSources) {
        super.setDefaultTargetDataSource(defaultTargetDataSource);
        super.setTargetDataSources(targetDataSources);
        super.afterPropertiesSet();
        DynamicDataSourceContextHolder.getInstance().addDataSourceKeys(targetDataSources.keySet());
        logger.info("setDynamicRoutingDataSource ... ");
    }

    /**
     * 切换数据源
     * @param key
     * @return
     */
    public boolean toggleDataSource(String key) {
        if (DynamicDataSourceContextHolder.getInstance().containDataSourceKey(key)) {
            String concurrentDataBase = DynamicDataSourceContextHolder.getInstance().getDataSourceKey();
            DynamicDataSourceContextHolder.getInstance().setDataSourceKey(key);
            determineTargetDataSource();
            logger.info("toggleDataSource {} -> {}", concurrentDataBase, key);
            return true;
        }
        return false;
    }

    @Override
    protected Object determineCurrentLookupKey() {
        return DynamicDataSourceContextHolder.getInstance().getDataSourceKey();
    }

}
