-- 创建数据库
CREATE
DATABASE IF NOT EXISTS  `distributed_lock_mysql` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
DROP TABLE IF EXISTS distributed_lock_mysql.`distributed_lock`;
CREATE TABLE distributed_lock_mysql.`distributed_lock` (
    `lock_id` varchar(50) NOT NULL COMMENT '锁唯一标识',
    `lock_value` varchar(50) NOT NULL COMMENT '锁内容,区分是谁上的锁',
    `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`lock_id`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;