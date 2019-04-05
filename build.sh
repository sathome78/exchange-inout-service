#!/bin/bash

git pull
mvn clean install
docker build --build-arg ENVIRONMENT=test -t exrates/exrates-inout-service:test .