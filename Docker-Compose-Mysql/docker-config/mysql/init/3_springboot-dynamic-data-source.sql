CREATE
DATABASE IF NOT EXISTS `docker_dynamic_data` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

DROP TABLE IF EXISTS docker_dynamic_data.`tenant_info`;
CREATE TABLE docker_dynamic_data.`tenant_info`
(
    `id`                  BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键id',
    `is_enable`           bit(1) NOT NULL DEFAULT b'1' COMMENT '数据是否有效 1 有效 0 无效',
    `create_time`         bigint(20) unsigned NOT NULL COMMENT '创建时间',
    `update_time`         bigint(20) unsigned NOT NULL COMMENT '更新时间',
    `tenant_id`           VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户id',
    `tenant_name`         VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '租户名称',
    `datasource_url`      VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源url',
    `datasource_username` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源用户名',
    `datasource_password` VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源密码',
    `datasource_driver`   VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '数据源驱动',
    `system_account`      VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '系统账号',
    `system_password`     VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '账号密码',
    `system_project`      VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '系统PROJECT',
    PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

CREATE UNIQUE INDEX tenant_id_unique on docker_dynamic_data.`tenant_info` (tenant_id);
CREATE UNIQUE INDEX tenant_name_unique on docker_dynamic_data.`tenant_info` (tenant_name);
