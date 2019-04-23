#!/bin/bash
docker kill $(docker ps -q)

git pull
mvn clean install
docker build --build-arg ENVIRONMENT=test -t exrates/exrates-inout-service:test .
docker run -d -it -p 8090:8080 -v /opt/properties:/opt/properties/ exrates/exrates-inout-service:test