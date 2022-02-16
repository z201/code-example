-- 创建数据库
CREATE DATABASE IF NOT EXISTS `docker_mybatis_audit` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
DROP TABLE IF EXISTS docker_mybatis_audit.`biz_audit_log`;
CREATE TABLE IF NOT EXISTS  docker_mybatis_audit.`biz_audit_log`
(
    `id`               BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `is_enable`           bit(1) NOT NULL DEFAULT b'1' COMMENT '数据是否有效 1 有效 0 无效',
    `create_time`         bigint(20) unsigned NOT NULL COMMENT '创建时间',
    `update_time`         bigint(20) unsigned NOT NULL COMMENT '更新时间',
    `event_type`  varchar(100) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '事件类型 insert update delete select 等等',
    `event_title`  varchar(100) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '事件标题 xxx查看了什么记录 xx修改了什么记录',
    `event_description`  varchar(100) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '事件描述',
    `event_time`         bigint(20) unsigned NOT NULL COMMENT '时间记录时间',
    `op_trace_id`  varchar(100) COLLATE utf8mb4_bin  DEFAULT '' COMMENT '操作标志',
    `user_id`  BIGINT(20) unsigned NOT NULL DEFAULT '0' COMMENT '操作用户id',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='数据审计日志';