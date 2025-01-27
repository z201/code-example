package cn.z201.example.distributed.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author z201.coding@gmail.com
 **/
@Service
public class DistributedLockTool {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean lock(String key, String value) {
        try {
            // 简单的sql执行
            String insertSql = "INSERT INTO `distributed_lock`(`lock_id`, `lock_value`)" + " VALUES (?, ?)";
            return jdbcTemplate.update(insertSql, key, value) > 0;
        }
        catch (RuntimeException e) {

        }
        return false;
    }

    @Transactional
    public Boolean unLock(String key, String value) {
        String sql = "SELECT * FROM distributed_lock WHERE lock_id = ? FOR UPDATE";
        Map<String, Object> lock = new HashMap<>();
        try {
            lock = jdbcTemplate.queryForMap(sql, key);
        }
        catch (EmptyResultDataAccessException e) {
            // 防止查询不到数据报错
            return false;
        }
        if (CollectionUtils.isEmpty(lock)) {
            return false;
        }
        else {
            if (Objects.equals(lock.get("lock_value"), value)) {
                sql = "DELETE FROM `distributed_lock` WHERE lock_id = ?";
                return jdbcTemplate.update(sql, key) > 0;
            }
        }
        return false;
    }

}
