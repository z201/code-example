package cn.z201.mybatis;

import cn.z201.mybatis.dao.BatchDao;
import cn.z201.mybatis.dao.TableDataDao;
import cn.z201.mybatis.entity.TableData;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wildfly.common.lock.Locks;

import javax.annotation.Resource;
import java.time.Clock;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AppApplication.class, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@AutoConfigureMockMvc
// 指定单元测试方法顺序
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AppApplicationTest {


    @Resource
    protected TableDataDao tableDataDao;

    @Autowired
    protected  BatchDao batchDao;

    @Autowired
    protected  SqlSessionFactory sqlSessionFactory;


    @BeforeEach
    public void before() {
        log.info("before");
        tableDataDao.truncate();
    }

    @AfterEach
    public void after() {
        log.info("after");
//        tableDataDao.truncate();
    }

    private List<TableData> mockData(int size) {
        List<TableData> list = new ArrayList<>();
        Long time = Clock.systemDefaultZone().millis();
        for (int i = 0; i < size; i++) {
            list.add(TableData.builder().createTime(time + i).build());
        }
        return list;
    }

    @Test
    @Disabled
    public void insert() {
        List<TableData> tableDataList = mockData(10);
        tableDataList.stream().forEach(i -> tableDataDao.insert(i));
    }

    @Test
    @Disabled
    public void batchInsert() {
        List<TableData> tableDataList = mockData(10);
        tableDataDao.batchInsert(tableDataList);
    }

    @Test
    @Disabled
    public void batchSave() {
        List<TableData> tableDataList = mockData(10);
        boolean result = batchDao.batchSave(tableDataList, TableDataDao.class, (mapper, data) -> {
            mapper.batchInsert(tableDataList);
        });
        log.info("result {} ", result);
    }

}