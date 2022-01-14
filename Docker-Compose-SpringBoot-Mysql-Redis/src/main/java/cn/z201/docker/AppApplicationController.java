package cn.z201.docker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author z201.coding@gmail.com
 **/
@RestController
public class AppApplicationController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private RedisTemplate redisTemplate;

    @RequestMapping(value = "")
    public Object index() {
        List<String> dataBasesList = jdbcTemplate.queryForList("SHOW  DATABASES", String.class);
        Properties info =  redisTemplate.getConnectionFactory().getConnection().info();
        Map<String, Object> data = new HashMap<>();
        data.put("code", "200");
        data.put("db", dataBasesList.toString());
        data.put("cache", info);
        System.out.println(info.get("redis_version"));
        return data;
    }
}
