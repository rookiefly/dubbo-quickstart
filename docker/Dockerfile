FROM openjdk:8-jdk-alpine
ENV JAVA_OPTS=""
VOLUME /tmp
ADD dubbo-quickstart-1.0.0-SNAPSHOT.jar /app.jar
ADD conf /conf
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar --spring.config.location=/conf/application.yml"]
EXPOSE 8787