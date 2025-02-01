-- 创建数据库
CREATE DATABASE IF NOT EXISTS `docker_mybatis_batch` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS  docker_mybatis_batch.`table_data`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `create_time`   bigint(20) unsigned NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='测试数据表';