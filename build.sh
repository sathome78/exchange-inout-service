#!/bin/bash

git pull
mvn clean install
docker build --build-arg ENVIRONMENT=test -v /opt/properties:/opt/properties/ -t exrates/exrates-inout-service:test .