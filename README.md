# exrates-inout-service

mvn clean install
docker build --build-arg ENVIRONMENT=test -t exrates/exrates-inout-service:test .
docker run -p 81:8080 ***
