## DOCKER运行
- 运行下列命令可以在本地Docker中创建一个镜像：
```shell script
mvn package docker:build
```
- 然后运行Docker容器：
```shell script
docker run -d -p 8787:8787 --name dubbo-quickstart -e jasypt.encryptor.password=123456 rookiefly/dubbo-quickstart:latest
```

## 添加skywalking全链路跟踪
- 将agent目录拷贝到部署spring boot项目的机器里，修改agent的配置，配置在agent/config/agent.config：
```properties
agent.service_name=${SW_AGENT_NAME:dubbo-quickstart}
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:11800}

# Logging file_name
logging.file_name=${SW_LOGGING_FILE_NAME:skywalking-api.log}

# Logging level
logging.level=${SW_LOGGING_LEVEL:DEBUG}
```
- agent.service_name填写springboot的application.name即可，collector.backend_service填写trace receiver service地址。

- 以javaagent的形式启动工程：
```shell script
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar -jar dubbo-quickstart-1.0.0-SNAPSHOT.jar
```
[Setup java agent](https://github.com/apache/skywalking/blob/v8.2.0/docs/en/setup/service-agent/java-agent/README.md)