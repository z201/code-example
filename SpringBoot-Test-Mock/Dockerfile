FROM openjdk:8-jdk-slim
MAINTAINER QF Zeng <z201.coding@gmail.com>
LABEL org.opencontainers.image.authors="z201"

# 设置容器资源限制
ENV JAVA_OPTS="-Xmx512m -Xms256m"

# 创建工作目录
WORKDIR /app

# 设置时区
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo Asia/Shanghai > /etc/timezone

# 复制应用文件
COPY target/SpringBoot-Test-Mock-1.0.0-SNAPSHOT.jar docker-app.jar

# 配置健康检查
HEALTHCHECK --interval=30s --timeout=3s \
  CMD curl -f http://localhost:8080/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh","-c","java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=test -jar docker-app.jar"]