-- 创建数据库
CREATE DATABASE IF NOT EXISTS redis_cache_breakdown DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;


SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for person
-- ----------------------------
DROP TABLE IF EXISTS redis_cache_breakdown.`person`;
CREATE TABLE redis_cache_breakdown.`person` (
                          `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
                          `full_name` varchar(10) NOT NULL COMMENT '全名',
                          `email` varchar(50) NOT NULL COMMENT '邮箱',
                          `telephone_number` bigint(11) unsigned NOT NULL COMMENT '手机号',
                          `sex` bit(1) NOT NULL DEFAULT b'0' COMMENT '性别',
                          `birth_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '出生日期',
                          `identity_card_number` bigint(18) unsigned NOT NULL COMMENT '身份证号',
                          PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=92035 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of person
-- ----------------------------
BEGIN;
INSERT INTO redis_cache_breakdown.`person` VALUES (1, '灵芸黄', 'lingyun.huang@gmail.com', 13842681484, b'1', '1996-05-24 05:17:45', 361405197211160626);
INSERT INTO redis_cache_breakdown.`person` VALUES (2, '碧琪刘', 'biqiliu@163.com', 13959090127, b'1', '1991-06-10 12:29:43', 141201198503249678);
INSERT INTO redis_cache_breakdown.`person` VALUES (3, '黛玉赵', 'daiyu.zhao@gmail.com', 13100077310, b'1', '1998-03-02 20:23:08', 460501198803127323);
INSERT INTO redis_cache_breakdown.`person` VALUES (4, '黛玉苏', 'daiyu.su@gmail.com', 13020424186, b'1', '1995-02-04 20:02:45', 441805200405173723);
INSERT INTO redis_cache_breakdown.`person` VALUES (5, '俊佳沈', 'junjiashen@126.com', 13637717889, b'0', '1993-03-29 20:12:17', 231205198211131920);
INSERT INTO redis_cache_breakdown.`person` VALUES (6, '宝钗蒋', 'jiang@qq.com', 13922446455, b'1', '1992-07-15 00:47:33', 212210199710050077);
INSERT INTO redis_cache_breakdown.`person` VALUES (7, '国辉黄', 'guohuihuang@sina.com', 13374609237, b'0', '1999-08-15 07:06:36', 232912198902016550);
INSERT INTO redis_cache_breakdown.`person` VALUES (8, '小慧楚', 'xiaohuichu@qq.com', 13894603053, b'1', '1994-08-16 12:38:17', 151909197812037208);
INSERT INTO redis_cache_breakdown.`person` VALUES (9, '正宇陈', 'chen@qq.com', 13290878895, b'0', '1997-01-21 10:38:13', 712204200205051417);
INSERT INTO redis_cache_breakdown.`person` VALUES (10, '俊佳钱', 'junjiaqian@126.com', 13154203168, b'0', '1992-01-25 16:54:24', 531903197106100754);
INSERT INTO redis_cache_breakdown.`person` VALUES (11, '蒹葭钱', 'jianjiaqian@gmail.com', 13633362585, b'1', '1990-01-30 08:27:17', 440809198805015014);
INSERT INTO redis_cache_breakdown.`person` VALUES (12, '祖英周', 'zuying.zhou@sina.com', 13860127639, b'1', '1994-06-05 05:28:30', 230902197811237241);
INSERT INTO redis_cache_breakdown.`person` VALUES (13, '国富黄', 'guofu.huang@163.com', 13171281329, b'0', '1993-11-05 07:53:06', 632508199206090554);
INSERT INTO redis_cache_breakdown.`person` VALUES (14, '宝钗楚', 'baochaichu@qq.com', 13665591686, b'1', '1993-12-16 05:18:26', 123007198009223687);
INSERT INTO redis_cache_breakdown.`person` VALUES (15, '嘉斌钱', 'jiabin.qian@163.com', 13990475433, b'0', '1989-11-15 00:38:02', 511603197802105966);
INSERT INTO redis_cache_breakdown.`person` VALUES (16, '析丹卫', 'xidanwei@163.com', 13934994939, b'1', '1993-03-28 19:29:14', 431412199403074139);
INSERT INTO redis_cache_breakdown.`person` VALUES (17, '正宇钱', 'qian@sohu.com', 13049503258, b'0', '1992-11-13 14:56:01', 631307198602032834);
COMMIT;
SET FOREIGN_KEY_CHECKS = 1;
