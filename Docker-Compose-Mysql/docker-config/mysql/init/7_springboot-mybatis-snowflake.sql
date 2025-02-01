-- 创建数据库
CREATE DATABASE IF NOT EXISTS `docker_mybatis_snowflake` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


CREATE TABLE IF NOT EXISTS  docker_mybatis_snowflake.`account`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
    `is_enable`     bit(1) NOT NULL                  DEFAULT b'1' COMMENT '数据是否有效 1 有效 0 无效',
    `create_time`   bigint(20) unsigned NOT NULL COMMENT '创建时间',
    `update_time`   bigint(20) unsigned NOT NULL COMMENT '更新时间',
    `phone_number`  varchar(50) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '手机号(当前用户身份唯一标识)',
    `email`         varchar(50) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '邮箱 保留字段，考虑邮箱登录。',
    `salt_password` varchar(225) COLLATE utf8mb4_bin DEFAULT '' COMMENT '加盐后密码',
    `salt`          varchar(225) COLLATE utf8mb4_bin DEFAULT '' COMMENT '盐 扩展字段',
    `usr_name`      varchar(100) COLLATE utf8mb4_bin DEFAULT '' COMMENT '用户名称',
    PRIMARY KEY (`id`),
    UNIQUE KEY `phone_nuique` (`phone_number`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='账号表';