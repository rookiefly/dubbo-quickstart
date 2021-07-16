# 安装手册

## 1、环境依赖

- `Java 8+`
- `MySQL 5.5+`
- `Redis 2.8+`
- `Maven 3.5+`
- `Apache Dubbo 2.7+`
- `Apache Dubbo Admin`
- `Apache Zookeeper 3.4+`
- `Alibaba Sentinel`

`注意：JVM启动参数设置：-Djasypt.encryptor.password=123456`

## DOCKER运行
- 运行下列命令可以在本地Docker中创建一个镜像：
```shell script
mvn package docker:build
```
- 运行makefile依赖镜像：
```shell script
make
```
- 然后运行docker-compose启动容器：
```shell script
cd docker-compose
docker-compose up
```