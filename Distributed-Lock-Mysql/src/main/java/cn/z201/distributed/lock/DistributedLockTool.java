package cn.z201.distributed.lock;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author z201.coding@gmail.com
 * @date 2022/1/9
 **/
@Service
public class DistributedLockTool {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public Boolean lock(String key, String value) {
        String sql = "SELECT * FROM distributed_lock WHERE lock_id = ? LOCK IN SHARE MODE";
        Map<String, Object> lock = new HashMap<>();
        try {
            lock = jdbcTemplate.queryForMap(sql, key);
        } catch (EmptyResultDataAccessException e) {

        }
        if (CollectionUtils.isEmpty(lock)) {
            // 简单的sql执行
            String insertSql = "INSERT IGNORE INTO `distributed_lock`(`lock_id`, `lock_value`)" +
                    " VALUES (?, ?)";
            return jdbcTemplate.update(insertSql, key, value) > 0;
        } else {
            if (!Objects.equals(lock.get("lock_value"), value)) {
                return false;
            }
        }
        return true;
    }

    public Boolean unLock(String key) {
        String sql = "DELETE FROM `distributed_lock` WHERE lock_id = ?";
        return jdbcTemplate.update(sql, key) > 0;
    }

}
