FROM openjdk:8-jre
MAINTAINER rookiefly@163.com
LABEL maintainer="rookiefly@163.com"
RUN apt-get update && apt-get install -y tini
COPY ./dubbo-admin-0.3.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["tini","--","java","-XX:+UnlockExperimentalVMOptions","-XX:+UseCGroupMemoryLimitForHeap","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
EXPOSE 8080
