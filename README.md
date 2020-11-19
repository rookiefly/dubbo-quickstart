- 将agent目录拷贝到部署spring boot项目的机器里，修改agent的配置，配置在agent/config/agent.config：
```properties
agent.service_name=${SW_AGENT_NAME:dubbo-quickstart}
collector.backend_service=${SW_AGENT_COLLECTOR_BACKEND_SERVICES:127.0.0.1:11800}

# Logging file_name
logging.file_name=${SW_LOGGING_FILE_NAME:skywalking-api.log}

# Logging level
logging.level=${SW_LOGGING_LEVEL:DEBUG}
```
- agent.service_name填写springboot的application.name即可，也可自定义名字。
collector.backend_service填写trace receiver service地址。

- 以javaagent的形式启动springboot工程：
```shell script
java -javaagent:/path/to/skywalking-agent/skywalking-agent.jar -jar yourApp.jar
```
[Setup java agent](https://github.com/apache/skywalking/blob/v8.2.0/docs/en/setup/service-agent/java-agent/README.md)