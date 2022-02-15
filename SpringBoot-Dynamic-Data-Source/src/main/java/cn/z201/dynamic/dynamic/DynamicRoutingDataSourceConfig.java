package cn.z201.dynamic.dynamic;

import cn.z201.dynamic.persistence.dao.TenantInfoDao;
import cn.z201.dynamic.persistence.entity.TenantInfo;
import com.baomidou.mybatisplus.core.MybatisConfiguration;
import com.baomidou.mybatisplus.extension.spring.MybatisSqlSessionFactoryBean;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.logging.slf4j.Slf4jImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author z201.coding@gmail.com
 **/
@Configuration
public class DynamicRoutingDataSourceConfig {

    private static final Logger logger = LoggerFactory.getLogger(DynamicRoutingDataSourceConfig.class);

    @Autowired
    ApplicationContext applicationContext;

    @Resource
    TenantInfoDao tenantInfoDao;

    @Autowired
    HikariDataSource hikariDataSource;

    @Bean(DynamicDataSourceConstant.MASTER)
    @ConfigurationProperties(prefix = "spring.datasource.hikari")
    public DataSource master() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @ConditionalOnBean(name = {DynamicDataSourceConstant.MASTER})
    public DynamicRoutingDataSource dynamicRoutingDataSource(@Qualifier(DynamicDataSourceConstant.MASTER) DataSource dataSource) {
        List<String> dataBasesList = new JdbcTemplate(dataSource).queryForList("SELECT DATABASE()", String.class);
        if (CollectionUtils.isEmpty(dataBasesList)) {
            ClassPathResource classPathResource = new ClassPathResource("create.sql");
            try {
                ScriptUtils.executeSqlScript(dataSource.getConnection(), classPathResource);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        DynamicRoutingDataSource dynamicRoutingDataSource = new DynamicRoutingDataSource();
        dynamicRoutingDataSource.setDynamicRoutingDataSource(dataSource, new HashMap<>());

        return dynamicRoutingDataSource;
    }

    @PostConstruct
    @ConditionalOnBean(DynamicRoutingDataSource.class)
    public void initDynamicDataSource() throws Exception {
        logger.info("initDynamicDataSource ...");
        DynamicRoutingDataSource dynamicRoutingDataSource = applicationContext.getBean(DynamicRoutingDataSource.class);
        HikariDataSource master = (HikariDataSource) applicationContext.getBean(DynamicDataSourceConstant.MASTER);
        Map<Object, Object> dataSourceMap = new HashMap<>();
        List<TenantInfo> tenantInfoList = tenantInfoDao.selectList(null);
        tenantInfoList.stream().forEach(i ->
                {
                    /**
                     *       maximumPoolSize: 20 # 连接池最大连接数，默认是10
                     *       minimumIdle: 5  # 最小空闲连接数量
                     *       idleTimeout: 600000 # 空闲连接存活最大时间，默认600000（10分钟）
                     *       connectionTimeout: 30000 # 数据库连接超时时间,默认30秒，即30000
                     *       maxLifetime: 1800000 # 此属性控制池中连接的最长生命周期，值0表示无限生命周期，默认1800000即30分钟
                     */
                    logger.info(" init  dataSource {}", i.getTenantName());
                    HikariConfig hikariConfig = new HikariConfig();
                    hikariConfig.setDriverClassName(i.getDatasourceDriver());
                    hikariConfig.setJdbcUrl(i.getDatasourceUrl());
                    hikariConfig.setUsername(i.getDatasourceUsername());
                    hikariConfig.setPassword(i.getDatasourcePassword());
                    hikariConfig.setMaximumPoolSize(20);
                    hikariConfig.setMinimumIdle(2);
                    hikariConfig.setIdleTimeout(600000);
                    hikariConfig.setConnectionTimeout(30000);
                    hikariConfig.setMaxLifetime(1800000);
                    DataSource dataSource = new HikariDataSource(hikariConfig);
                    dataSourceMap.put(i.getTenantId(), dataSource);
                }
        );
        // 设置数据源
        dynamicRoutingDataSource.setDynamicRoutingDataSource(master, dataSourceMap);
    }

    @Bean
    public MybatisSqlSessionFactoryBean sqlSessionFactoryBean(@Qualifier("dynamicRoutingDataSource") DynamicRoutingDataSource dynamicRoutingDataSource
    ) throws Exception {
        MybatisSqlSessionFactoryBean sessionFactory = new MybatisSqlSessionFactoryBean();
        sessionFactory.setDataSource(dynamicRoutingDataSource);
        // 可以从配置文件中获取，这里演示暂时写死。
        sessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*.xml"));
        sessionFactory.setTypeAliasesPackage("cn.z201.dynamic.persistence.entity");
        MybatisConfiguration config = new MybatisConfiguration();
        config.setMapUnderscoreToCamelCase(true);  //开启下划线转驼峰
        config.setLogImpl(Slf4jImpl.class); // 日志输出实现
        sessionFactory.setConfiguration(config);
        return sessionFactory;
    }

    /**
     * 配置事务管理, 使用事务时在方法头部添加@Transactional注解即可
     *
     * @param dynamicRoutingDataSource
     * @return
     */
    @Bean
    public PlatformTransactionManager transactionManager(@Qualifier("dynamicRoutingDataSource") DynamicRoutingDataSource dynamicRoutingDataSource) {
        return new DataSourceTransactionManager(dynamicRoutingDataSource);
    }

    /**
     * jdbc 扩展
     *
     * @param dynamicRoutingDataSource
     * @return
     */
    @Bean
    public DynamicJdbcTemplateManager dynamicJdbcTemplateManager(@Qualifier("dynamicRoutingDataSource") DynamicRoutingDataSource dynamicRoutingDataSource) {
        return new DynamicJdbcTemplateManager(dynamicRoutingDataSource);
    }


}