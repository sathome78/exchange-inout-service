FROM java:8
VOLUME /tmp
ARG APP_PATH=/input-output
ARG ENVIRONMENT

RUN mkdir -p input-output
COPY ./target/input-output.jar ${APP_PATH}/input-output.jar
COPY ./target/config/${ENVIRONMENT}/application.yml ${APP_PATH}/application.yml
COPY ./target/config/${ENVIRONMENT}/job.properties ${APP_PATH}/job.properties
COPY ./target/config/${ENVIRONMENT}/mail.properties ${APP_PATH}/mail.properties
COPY ./target/config/${ENVIRONMENT}/merchants.properties ${APP_PATH}/merchants.properties
COPY ./target/config/${ENVIRONMENT}/merchants /merchants
RUN echo "$PWD"
RUN ls -l
RUN cd input-output
RUN echo "$PWD"
RUN ls -l
COPY ./target/config/${ENVIRONMENT}/node_config ${APP_PATH}/node_config

WORKDIR ${APP_PATH}

ARG CONFIG_FILE_PATH="-Dspring.config.location="${ENVIRONMENT}"/application.yml"

EXPOSE 8080
CMD java -jar input-output.jar $CONFIG_FILE_PATH