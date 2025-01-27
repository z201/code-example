FROM openjdk:8-jdk-slim

LABEL maintainer="z201.code@gmail.com"

WORKDIR /app

# 设置时区
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 复制应用jar包
COPY target/*.jar app.jar

# 设置JVM参数
ENV JAVA_OPTS="-Xms512m -Xmx512m -Djava.security.egd=file:/dev/./urandom"

# 暴露应用端口
EXPOSE 9009

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD curl -f http://localhost:9009/redis/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["sh","-c","java $JAVA_OPTS -jar app.jar"]