#!/usr/bin/env bash

export DOCKER_ENDPOINT="739878518990.dkr.ecr.ap-south-1.amazonaws.com"
export VERSION="0.0.9"
export AWS_DEFAULT_REGION="ap-south-1"
export APPLICATION_NAME="provider"

./gradlew build docker
if [ $? == 0 ]; then
`aws ecr get-login --no-include-email --region ap-south-1`
docker tag co.arctern.api/providers:latest $DOCKER_ENDPOINT/$APPLICATION_NAME:$VERSION
docker push $DOCKER_ENDPOINT/$APPLICATION_NAME:$VERSION
fi
