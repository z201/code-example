# Docker 配置检查清单

## 1. 容器命名规范

- [ ] 容器名称格式是否符合规范：`{服务名称}{版本号}-{模块名称}`
  - 示例：mysql8.0-distributed-lock
  - 示例：redis5.0.5-distributed-lock
  - 示例：zookeeper3.7.1-distributed-lock

## 2. 端口映射规划

### 2.1 MySQL服务端口映射
- [ ] 分布式锁模块：3310:3306
- [ ] MyBatis解释器模块：3311:3306
- [ ] MyBatis批处理模块：3312:3306
- [ ] 测试Mock模块：3313:3306

## 3. 容器资源限制

### 3.1 Java应用资源配置
- [ ] JVM内存配置是否合理
  - 最大堆内存：-Xmx512m
  - 初始堆内存：-Xms256m
  - 特殊模块（如分布式锁）：-Xmx1024m/-Xms512m

### 3.2 中间件资源限制
- [ ] CPU限制
  - 限制值：0.5 CPU
  - 预留值：0.25 CPU
- [ ] 内存限制
  - 限制值：512M
  - 预留值：256M

## 4. 日志配置

- [ ] 日志驱动配置：json-file
- [ ] 日志轮转配置
  - 单个日志文件大小限制：100m
  - 保留的日志文件数量：3

## 5. 健康检查配置

- [ ] 检查间隔：30s
- [ ] 超时时间：3s
- [ ] 重试次数：1
- [ ] 健康检查端点配置
  - Spring Boot应用：/actuator/health
  - 中间件：特定端口检查

## 6. 时区和本地化配置

- [ ] 容器时区设置：Asia/Shanghai
- [ ] 时区文件映射：/usr/share/zoneinfo/Asia/Shanghai

## 7. 数据持久化

- [ ] 数据目录挂载配置
  - MySQL：数据目录
  - Redis：配置文件和数据目录
  - Zookeeper：配置文件、数据目录和日志目录

## 8. 网络配置

- [ ] 网络模式：bridge
- [ ] 网络名称规范：network-{模块名称}
- [ ] 容器间通信配置

## 9. 安全配置

- [ ] 环境变量管理
  - 敏感信息使用环境变量传递
  - 避免在Dockerfile中硬编码敏感信息
- [ ] 文件权限设置
  - 配置文件权限
  - 数据目录权限

## 10. 镜像构建优化

- [ ] 基础镜像选择：openjdk:8-jdk-slim
- [ ] 多阶段构建（如果适用）
- [ ] 镜像大小优化
- [ ] 构建缓存利用

## 11. 启动配置

- [ ] 启动参数配置
  - JVM参数
  - 应用程序参数
  - 环境变量
- [ ] 优雅关闭配置
- [ ] 自动重启策略：unless-stopped

## 12. 版本管理

- [ ] 镜像版本标签规范
- [ ] 依赖服务版本统一
  - MySQL：8.0
  - Redis：5.0.5
  - Zookeeper：3.7.1

## 13. Dockerfile和docker-compose配置匹配检查

- [ ] Dockerfile基础镜像版本与docker-compose服务版本一致
- [ ] 端口配置匹配
  - Dockerfile中EXPOSE的端口与docker-compose中的expose/ports配置一致
  - 应用配置文件中的端口与容器映射端口一致
- [ ] 环境变量配置
  - Dockerfile中的ENV与docker-compose的environment配置同步
  - 环境变量在应用配置文件中正确引用
- [ ] 数据卷挂载点
  - Dockerfile中的VOLUME与docker-compose的volumes配置匹配
  - 挂载路径在容器内外一致
- [ ] 健康检查机制
  - Dockerfile的HEALTHCHECK与docker-compose的healthcheck配置一致
  - 检查命令和参数匹配

## 14. Java应用配置检查

- [ ] application.yml/properties配置
  - 数据库连接信息与docker-compose中的配置匹配
  - Redis连接信息与容器配置一致
  - 应用端口与容器映射端口对应
  - 日志路径与容器卷挂载配置匹配
- [ ] Spring Boot配置
  - actuator端点配置与健康检查路径一致
  - 应用上下文路径配置正确
  - 数据源配置使用环境变量
- [ ] 资源配置
  - JVM参数配置与容器资源限制匹配
  - 线程池配置合理
  - 连接池配置适当

## 注意事项

1. 在进行容器化部署前，请确保完成上述所有检查项
2. 定期检查和更新配置，确保符合最新的最佳实践
3. 保持配置的一致性，避免环境差异导致的问题
4. 重点关注资源限制和健康检查配置，确保容器的稳定运行
5. 合理规划端口映射，避免端口冲突
6. 确保Dockerfile和docker-compose配置文件的同步更新
7. 定期验证Java应用配置与容器环境的兼容性