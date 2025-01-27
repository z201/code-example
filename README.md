# Code Example 项目
> 明镜止水，见微知著

本项目是一个综合性的Java技术示例集合，涵盖了Spring Boot、Redis、MySQL等多个技术领域的实践案例。

## 中间件使用情况
> 运筹帷幄，决胜千里

### 数据存储
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| Redis | 5.0.6 | 分布式缓存、布隆过滤器、延迟队列、地理位置服务 |
| MySQL | latest | 关系型数据库，支持动态数据源、审计、批处理等功能 |

### 分布式协调
| 中间件名称 | 版本 | 用途说明 |
|-----------|------|----------|
| Zookeeper | latest | 分布式锁实现 |
| Redis Sentinel | 5.0.6 | Redis高可用方案 |

## 模块说明
> 纲举目张，条理分明

### Spring Boot 应用
| 模块名称 | 主要功能 | 核心依赖 |
|---------|---------|----------|
| SpringBoot-Redis-Bloom | Redis布隆过滤器实现 | spring-boot-starter-data-redis |
| SpringBoot-Redis-Geo | Redis地理位置服务 | spring-boot-starter-data-redis |
| SpringBoot-Redis-Scan | Redis扫描优化 | spring-boot-starter-data-redis |
| SpringBoot-Distributed-Lock-Redis | Redis分布式锁 | spring-boot-starter-data-redis |
| SpringBoot-Distributed-Lock-Zookeeper | Zookeeper分布式锁 | zookeeper |
| SpringBoot-Dynamic-Data-Source | 动态数据源切换 | mybatis-spring-boot-starter |
| SpringBoot-Mybatis-Audit | MyBatis审计功能 | mybatis-spring-boot-starter |
| SpringBoot-Test-Mock | 单元测试与Mock | spring-boot-starter-test |

### 性能测试
| 模块名称 | 主要功能 | 核心依赖 |
|---------|---------|----------|
| SpringBoot-JMH | JMH基准测试 | jmh-core |
| Jmh-Snowflake | 雪花算法性能测试 | jmh-core |

### 工具模块
| 模块名称 | 主要功能 | 核心依赖 |
|---------|---------|----------|
| Selenium-Tool | 自动化测试工具 | selenium-java |
| SpringBoot-WebClient | 响应式Web客户端 | spring-boot-starter-webflux |

## Docker配置
> 举重若轻，运转如意

项目包含多个基于Docker Compose的服务编排配置：

1. Redis主从复制
```yaml
services:
  redis-master:
    image: redis:5.0.6
    ports:
      - "6379:6379"
  redis-slave1:
    image: redis:5.0.6
    ports:
      - "6380:6379"
```

2. Redis Sentinel高可用
```yaml
services:
  redis-master:
    image: redis:5.0.6
  sentinel1:
    image: redis:5.0.6
    command: redis-sentinel
```

## 使用指南
> 循序渐进，知行合一

1. 克隆项目
```bash
git clone <repository-url>
```

2. 构建项目
```bash
mvn clean install
```

3. 运行Docker服务
```bash
cd Docker-Compose-Redis-Master-Slave
docker-compose up -d
```

4. 运行示例
- 选择感兴趣的模块
- 查看模块下的README文件获取详细说明
- 运行测试用例或启动应用

## 注意事项

- 确保已安装Java 8或更高版本
- 确保已安装Maven
- 确保已安装Docker和Docker Compose
- 部分模块可能需要额外的环境配置，请参考具体模块的说明