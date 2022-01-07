FROM openjdk:8
MAINTAINER QF Zeng <z201.coding@gmail.com>
LABEL org.opencontainers.image.authors="z201"
VOLUME /tmp
ADD target/Redis-Cache-Breakdown-1.0.0-SNAPSHOT.jar docker-app.jar
RUN ln -snf /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo Asis/Shanghai > /etc/timezone
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=test","-jar","/docker-app.jar"]