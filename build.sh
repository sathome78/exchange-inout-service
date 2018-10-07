#!/usr/bin/env bash


docker build -t input-output-service .

docker run -p 80:8080 input-output-service