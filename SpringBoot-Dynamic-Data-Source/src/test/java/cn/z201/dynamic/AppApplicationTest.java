package cn.z201.dynamic;

import cn.z201.dynamic.dynamic.DynamicDataSourceContextHolder;
import cn.z201.dynamic.dynamic.DynamicRoutingDataSource;
import cn.z201.dynamic.dynamic.DynamicRoutingDataSourceTool;
import cn.z201.dynamic.mybaits.SnowflakeTool;
import cn.z201.dynamic.persistence.dao.TenantInfoDao;
import cn.z201.dynamic.persistence.entity.TenantInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.init.ScriptUtils;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    protected SqlSessionFactory sqlSessionFactory;

    @Autowired
    private DataSource dataSource;

    @Resource
    TenantInfoDao tenantInfoDao;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    public void before() {
        log.info("before");
        List<TenantInfo> tenantInfoList = tenantInfoDao.selectList(null);
        if (CollectionUtils.isEmpty(tenantInfoList)) {
            TenantInfo tenantInfo = new TenantInfo();
            int i = 3;
            for (int j = 0; j < i; j++) {
                String sql = "CREATE DATABASE IF NOT EXISTS `docker_dynamic_data_${db}` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;".replace("${db}", String.valueOf(j));
                log.info("sql {} \n", sql);
                tenantInfo.setTenantId(String.valueOf(SnowflakeTool.getInstance().nextId()));
                tenantInfo.setTenantName(Base64.getEncoder().encodeToString(tenantInfo.getTenantId().getBytes()));
                tenantInfo.setDatasourceUsername("root");
                tenantInfo.setDatasourcePassword("123456");
                tenantInfo.setDatasourceDriver("com.mysql.cj.jdbc.Driver");
                tenantInfo.setDatasourceUrl(DynamicRoutingDataSourceTool.buildDataBase("docker_dynamic_data_" + j));
                tenantInfoDao.insert(tenantInfo);
            }
        }
    }


    @AfterEach
    public void after() {
        log.info("after");
        List<TenantInfo> tenantInfoList = tenantInfoDao.selectList(null);
//        if (!CollectionUtils.isEmpty(tenantInfoList)) {
//            ClassPathResource classPathResource = new ClassPathResource("clean.sql");
//            try {
//                ScriptUtils.executeSqlScript(dataSource.getConnection(), classPathResource);
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }
//        }
    }

    @Test
    public void setUp() {
        log.info("bean Count {}", applicationContext.getBeanDefinitionCount());
        lookupCollectionType(applicationContext);
        DynamicRoutingDataSource dynamicRoutingDataSource = applicationContext.getBean(DynamicRoutingDataSource.class);
        Set<Object> dataSourceKeys = DynamicDataSourceContextHolder.getInstance().all();
        concurrentDataBase();
        log.info("dataSourceKey All {}", dataSourceKeys);
        for (Object dataSourceKey : dataSourceKeys) {
            dynamicRoutingDataSource.toggleDataSource(dataSourceKey.toString());
            // jdbcTemplate 在运行时需要重新指定 DataSource
            this.jdbcTemplate = new JdbcTemplate(dynamicRoutingDataSource.determineTargetDataSource());
            concurrentDataBase();
        }
    }

    private void concurrentDataBase() {
        List<String> dataBasesList = jdbcTemplate.queryForList("SELECT DATABASE();", String.class);
        log.info("concurrentDataBase {}", dataBasesList);
    }

    /**
     * 单一类型集合查找
     *
     * @param beanFactory
     */
    private void lookupCollectionType(BeanFactory beanFactory) {
        if (beanFactory instanceof ListableBeanFactory) {
            ListableBeanFactory listableBeanFactory = (ListableBeanFactory) beanFactory;
            Map<String, DataSource> beansMap = listableBeanFactory.getBeansOfType(DataSource.class);
            beansMap.forEach((key, value) -> {
                System.out.println("单一类型集合查找  " + key + " " + value);
            });
        }
    }
}