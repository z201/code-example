package cn.z201.test.mock;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author z201.coding@gmail.com
 **/
@Repository
public class MysqlVersionRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public String version() {
        List<String> list = jdbcTemplate.queryForList("SELECT VERSION()", String.class);
        return CollectionUtil.isNotEmpty(list) ? list.get(0) : "undefined";
    }

}
