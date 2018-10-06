FROM java:8
VOLUME /tmp
ARG APP_PATH=/input-output-service

RUN mkdir -p input-output-service
COPY ./target/input-output-service.jar ${APP_PATH}/input-output-service.jar

WORKDIR ${APP_PATH}

EXPOSE 8080
CMD java -jar input-output-service.jar