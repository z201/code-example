-- 创建审计数据库
CREATE DATABASE IF NOT EXISTS audit_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE audit_db;

-- 创建操作日志表
CREATE TABLE IF NOT EXISTS operation_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id VARCHAR(64) NOT NULL COMMENT '操作用户ID',
    username VARCHAR(50) NOT NULL COMMENT '操作用户名',
    operation VARCHAR(50) NOT NULL COMMENT '操作类型',
    method VARCHAR(200) NOT NULL COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    time BIGINT NOT NULL COMMENT '执行时长(毫秒)',
    ip VARCHAR(64) COMMENT 'IP地址',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_user_id(user_id),
    INDEX idx_operation(operation),
    INDEX idx_created_time(created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 创建数据变更记录表
CREATE TABLE IF NOT EXISTS data_change_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    table_name VARCHAR(50) NOT NULL COMMENT '表名',
    record_id VARCHAR(64) NOT NULL COMMENT '记录ID',
    change_type VARCHAR(20) NOT NULL COMMENT '变更类型(INSERT/UPDATE/DELETE)',
    old_value TEXT COMMENT '原始值',
    new_value TEXT COMMENT '新值',
    changed_fields TEXT COMMENT '变更字段',
    user_id VARCHAR(64) NOT NULL COMMENT '操作用户ID',
    created_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_table_record(table_name, record_id),
    INDEX idx_user_id(user_id),
    INDEX idx_created_time(created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据变更记录表';