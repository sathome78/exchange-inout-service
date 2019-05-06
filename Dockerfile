FROM java:8
VOLUME /tmp
ARG APP_PATH=/input-output-service
ARG ENVIRONMENT

RUN mkdir -p input-output-service
RUN mkdir /data
RUN mkdir /data/ethereum
RUN mkdir /data/ethereum/keystore
COPY ./target/input-output-service.jar ${APP_PATH}/input-output-service.jar
COPY ./target/config/${ENVIRONMENT}/application.yml ${APP_PATH}/application.yml
COPY ./target/config/${ENVIRONMENT}/merchants ${APP_PATH}/

WORKDIR ${APP_PATH}
RUN readlink -f application.yml
EXPOSE 8080
CMD java -jar input-output-service.jar
