#!/usr/bin/env bash

export SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/emr?useSSL=false
export SPRING_DATASOURCE_USERNAME=root
export SPRING_DATASOURCE_PASSWORD=example
export SPRING_RABBITMQ_HOST=127.0.0.1
export SPRING_RABBITMQ_PORT=5672
export SPRING_RABBITMQ_USERNAME=test
export SPRING_RABBITMQ_PASSWORD=test
export SPRING_RABBITMQ_QUEUE_NAME=emr-updates-simple-dev
export JVM_OPTS="-XX:+UnlockExperimentalVMOptions -XX:+UseCGroupMemoryLimitForHeap -XX:MaxRAMFraction=1 -Xms64M -Xmx512M"
JAVA_OPTS="--add-modules java.xml.bind" ./gradlew bootRun