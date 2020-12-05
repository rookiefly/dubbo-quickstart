FROM openjdk:8-jdk-alpine
ENV JAVA_OPT="-Djava.security.egd=file:/dev/./urandom"
VOLUME /tmp
ADD dubbo-quickstart-1.0.0-SNAPSHOT.jar /app.jar
ENTRYPOINT ["java", "${JAVA_OPT}", "-jar","/app.jar"]