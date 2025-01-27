# code-example

> 整理博客内容中的代码相关内容

blog related code

## 模块说明

| 模块名称 | 功能说明 |
|---------|----------|
| bom | 项目依赖版本管理模块 |
| distributed-lock-mysql | 基于MySQL实现的分布式锁示例 |
| distributed-lock-redis | 基于Redis实现的分布式锁示例 |
| distributed-lock-zookeeper | 基于Zookeeper原生API实现的分布式锁示例 |
| distributed-lock-zookeeper-curator | 基于Zookeeper Curator框架实现的分布式锁示例 |
| jmh | JMH基准测试示例 |
| jmh-snowflake | 雪花算法性能基准测试 |
| junit | JUnit单元测试示例 |
| netty-learning | Netty框架学习示例 |
| selenium-tool | Selenium自动化测试工具示例 |
| spring-aop-annotation | Spring AOP注解使用示例 |
| spring-programming-model | Spring编程模型示例 |
| spring-spi | Spring SPI机制示例 |
| springboot-aop-log | SpringBoot整合AOP实现日志记录 |
| springboot-blocking-queue | SpringBoot阻塞队列实现示例 |
| springboot-delayed-message-queue | SpringBoot延迟消息队列实现 |
| springboot-dynamic-data-source | SpringBoot动态数据源实现 |
| springboot-email | SpringBoot邮件服务示例 |
| springboot-jmh | SpringBoot整合JMH基准测试 |
| springboot-lucene | SpringBoot整合Lucene搜索引擎 |
| springboot-mdc-log | SpringBoot MDC日志链路追踪 |
| springboot-mybatis-audit | SpringBoot整合MyBatis实现审计功能 |
| springboot-mybatis-batch | SpringBoot整合MyBatis批量操作示例 |
| springboot-mybatis-explain | SpringBoot整合MyBatis执行计划分析 |
| springboot-mybatis-snowflake | SpringBoot整合MyBatis的雪花算法实现 |
| springboot-mysql-redis | SpringBoot整合MySQL和Redis示例 |
| springboot-redis-bloom | SpringBoot整合Redis布隆过滤器 |
| springboot-redis-cache-breakdown | SpringBoot Redis缓存击穿解决方案 |
| springboot-redis-delayed-queue | SpringBoot Redis延迟队列实现 |
| springboot-redis-geo | SpringBoot Redis地理位置功能示例 |
| springboot-redis-scan | SpringBoot Redis扫描优化示例 |
| springboot-redisson | SpringBoot整合Redisson示例 |
| springboot-scheduling-tasks | SpringBoot定时任务示例 |
| springboot-test-mock | SpringBoot测试Mock示例 |
| springboot-webclient | SpringBoot WebClient示例 |

## 其他工具

| 工具名称 | 说明 |
|---------|------|
| docker-compose-redis-master-slave | Redis主从复制Docker部署配置 |
| docker-compose-redis-sentinel | Redis哨兵模式Docker部署配置 |

## 使用说明

1. 先构建BOM项目;在回到项目根目录构建项目。

* `mvn clean test -P dev  `         => 只执行单侧
* `mvn clean verify -P integration-test`  => 只执行集测
* `mvn clean test -P test`          => 执行单侧+集测
   

