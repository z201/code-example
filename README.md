# code-example

> 整理博客内容中的代码相关内容

blog related code

## 中间件使用情况

### 存储类中间件
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| MySQL | 8.0+ | 用于分布式锁实现、动态数据源、MyBatis相关示例等场景的关系型数据库 |
| Redis | 6.0+ | 用于分布式锁、布隆过滤器、延迟队列、地理位置、缓存等功能的内存数据库 |

### 协调服务类中间件
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| Zookeeper | 3.7+ | 用于分布式锁实现的分布式协调服务 |
| Redisson | 3.16+ | Redis分布式服务框架，提供分布式对象和服务 |

### 通信类中间件
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| Netty | 4.1+ | 异步事件驱动的网络应用框架，用于网络通信示例 |

### 搜索引擎类中间件
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| Lucene | 8.11+ | 全文检索引擎，用于实现搜索功能 |

### 数据访问类中间件
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| MyBatis | 3.5+ | ORM框架，用于数据库操作、批处理、执行计划分析等 |

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

# Docker 配置优化指南

## 容器命名规范

为确保不同模块间容器名称唯一，采用以下命名规则：
- 格式：`{服务名称}{版本号}-{模块名称}`
- 示例：
  - MySQL容器：`mysql8.0-distributed-lock`
  - Redis容器：`redis5.0.5-distributed-lock`
  - Zookeeper容器：`zookeeper3.7.1-distributed-lock`

## 端口映射规划

为避免服务端口冲突，各服务端口映射如下：

### MySQL服务
- 分布式锁模块：3310:3306
- MyBatis解释器模块：3311:3306
- MyBatis批处理模块：3312:3306
- 测试Mock模块：3313:3306

### Redis服务
- 分布式锁模块：6379:6379

### Zookeeper服务
- 单节点模式：2181:2181
- 集群模式：
  - 节点1：2181:2181, 2888:2888, 3888:3888
  - 节点2：2182:2181, 2889:2888, 3889:3888
  - 节点3：2183:2181, 2890:2888, 3890:3888

## 数据库版本统一

所有MySQL服务统一使用8.0版本，配置示例：
```yaml
image: mysql:8.0
command: [
  '--character-set-server=utf8mb4',
  '--collation-server=utf8mb4_unicode_ci',
  '--lower_case_table_names=1',
  '--default-time-zone=+8:00',
  '--default-authentication-plugin=mysql_native_password'
]
```

## 资源限制配置

### MySQL资源配置
```yaml
deploy:
  resources:
    limits:
      cpus: '0.8'
      memory: 1024M
    reservations:
      cpus: '0.4'
      memory: 512M
```

### Redis资源配置
```yaml
deploy:
  resources:
    limits:
      cpus: '0.5'
      memory: 512M
    reservations:
      cpus: '0.25'
      memory: 256M
```

### Zookeeper资源配置
```yaml
deploy:
  resources:
    limits:
      cpus: '0.5'
      memory: 512M
    reservations:
      cpus: '0.25'
      memory: 256M
```

## 日志配置

所有服务统一的日志配置：
```yaml
logging:
  driver: "json-file"
  options:
    max-size: "100m"
    max-file: "3"
```

## 健康检查配置

各服务统一的健康检查配置：
```yaml
healthcheck:
  test: "/bin/netstat -anpt|grep {PORT}"
  interval: 30s
  timeout: 3s
  retries: 1
```

## 数据持久化

### MySQL数据持久化
```yaml
volumes:
  - ./docker-config/mysql/my.cnf:/etc/mysql/my.cnf
  - ./docker-config/mysql/init:/docker-entrypoint-initdb.d
```

### Redis数据持久化
```yaml
volumes:
  - ./docker-config/redis/redis.conf:/etc/redis.conf
```

### Zookeeper数据持久化
```yaml
volumes:
  - ./docker-config/zookeeper/data:/data
  - ./docker-config/zookeeper/datalog:/datalog
```


## 使用说明

1. 先构建BOM项目;在回到项目根目录构建项目。

* `mvn clean test -P dev  `         => 只执行单侧
* `mvn clean verify -P integration-test`  => 只执行集测
* `mvn clean test -P test`          => 执行单侧+集测
   

