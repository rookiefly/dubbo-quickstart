FROM openjdk:8-jdk-alpine

MAINTAINER rookiefly@163.com

ARG version
ARG port

# sentinel version
ENV SENTINEL_VERSION ${version:-1.8.2}
#PORT
ENV PORT ${port:-8280}
ENV JAVA_OPTS=""
#
ENV PROJECT_NAME sentinel-dashboard
ENV SERVER_HOST localhost
ENV SERVER_PORT 8280
ENV USERNAME sentinel
ENV PASSWORD sentinel


# sentinel home
ENV SENTINEL_HOME  /opt/
ENV SENTINEL_LOGS  /opt/logs

#tme zone
RUN rm -rf /etc/localtime \
&& ln -s /usr/share/zoneinfo/Asia/Shanghai /etc/localtime

# create logs
RUN mkdir -p ${SENTINEL_LOGS}

# get the jar form github
RUN cd /  \
 && wget https://github.com/alibaba/Sentinel/releases/download/v${SENTINEL_VERSION}/sentinel-dashboard-${SENTINEL_VERSION}.jar -O sentinel-dashboard.jar \
 && mv sentinel-dashboard.jar ${SENTINEL_HOME} \
 && chmod -R +x ${SENTINEL_HOME}/*jar

# copy the jar from local
#COPY ./sentinel-dashboard-1.8.1.jar ${SENTINEL_HOME}/sentinel-dashboard.jar
#RUN chmod -R +x ${SENTINEL_HOME}/*jar

VOLUME ${SENTINEL_LOGS}

WORKDIR  ${SENTINEL_HOME}

EXPOSE ${PORT}

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar sentinel-dashboard.jar"]