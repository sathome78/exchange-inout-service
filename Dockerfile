FROM java:8
VOLUME /tmp
ARG APP_PATH=/input-output
ARG ENVIRONMENT

RUN mkdir -p input-output
COPY ./target/input-output.jar ${APP_PATH}/input-output.jar
COPY ./target/config/${ENVIRONMENT}/application.yml ${APP_PATH}/application.yml

WORKDIR ${APP_PATH}

ARG CONFIG_FILE_PATH="-Dspring.config.location="${ENVIRONMENT}"/application.yml"

EXPOSE 8080
CMD java -jar input-output.jar $CONFIG_FILE_PATH