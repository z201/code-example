package cn.z201.dynamic;

import cn.z201.example.dynamic.AppApplication;
import cn.z201.example.dynamic.manager.DynamicDataSourceContextHolder;
import cn.z201.example.dynamic.manager.DynamicJdbcTemplateManager;
import cn.z201.example.dynamic.manager.DynamicRoutingDataSource;
import cn.z201.example.dynamic.manager.DynamicRoutingDataSourceTool;
import cn.z201.example.dynamic.mybatis.SnowflakeTool;
import cn.z201.example.dynamic.dao.TenantInfoDao;
import cn.z201.example.dynamic.entity.TenantInfo;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.*;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {

    @Autowired
    private ApplicationContext applicationContext;

    @Resource
    private TenantInfoDao tenantInfoDao;

    @Autowired
    private DynamicJdbcTemplateManager jdbcTemplate;

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
    }

    @Test
    public void setUp() {
        log.info("bean Count {}", applicationContext.getBeanDefinitionCount());
        lookupCollectionType(applicationContext);
        DynamicRoutingDataSource dynamicRoutingDataSource = applicationContext.getBean(DynamicRoutingDataSource.class);
        Set<Object> dataSourceKeys = DynamicDataSourceContextHolder.getInstance().all();
        log.info("dataSourceKey All {}", dataSourceKeys);
        for (Object dataSourceKey : dataSourceKeys) {
            // 切换数据库
            dynamicRoutingDataSource.toggleDataSource(dataSourceKey.toString());
            // jdbcTemplate 在运行时需要重新指定 DataSource
            concurrentDataBase();
        }
    }

    private void concurrentDataBase() {
        List<String> dataBasesList = jdbcTemplate.dynamicJdbcTemplate().queryForList("SELECT DATABASE();", String.class);
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