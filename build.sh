#!/bin/bash

git pull
mvn clean install
docker build --build-arg ENVIRONMENT=test -t exrates/exrates-inout-service:test .
# docker run -p 8090:8080 -v /opt/properties:/opt/properties/