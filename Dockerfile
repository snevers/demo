FROM frolvlad/alpine-oraclejdk8:slim
MAINTAINER TGJ tonggangjin2@163.com
VOLUME ["/opt/caches/", "/opt/logs/", "/opt/static/"]
#docker-maven-plugin
#ADD rykj-official-0.0.1-SNAPSHOT.war app.war
#docker tools
COPY ["target/rykj-official-0.0.1-SNAPSHOT.war", "/app.war"]
RUN sh -c 'touch /app.war'
ENV JAVA_OPTS=""
ENV LANG C.UTF-8
LABEL version="1.0"
LABEL description="rykj of official"
EXPOSE 80 8000
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.war"]