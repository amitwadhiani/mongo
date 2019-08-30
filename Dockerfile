FROM openjdk:11-slim

VOLUME /tmp
ARG JAR_FILE
COPY newrelic .
ADD ${JAR_FILE} app.jar
ENTRYPOINT java $JVM_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar