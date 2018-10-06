FROM java:8
VOLUME /tmp
ARG APP_PATH=/wallet-checker-2

RUN mkdir -p wallet-checker-2
COPY ./target/wallet-checker-app.jar ${APP_PATH}/wallet-checker-app.jar

WORKDIR ${APP_PATH}

EXPOSE 8080
CMD java -jar wallet-checker-app.jar