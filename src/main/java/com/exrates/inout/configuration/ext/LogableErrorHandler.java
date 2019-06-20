package com.exrates.inout.configuration.ext;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
//@Log4j2(topic = "inout")
public class LogableErrorHandler implements ResponseErrorHandler {

   private static final Logger log = LogManager.getLogger("inout");

    @Override
    public boolean hasError(ClientHttpResponse clientHttpResponse) throws IOException {
        return clientHttpResponse.getStatusCode().value() != 200;
    }

    @Override
    public void handleError(ClientHttpResponse clientHttpResponse) throws IOException {
        log.info("Error restTemplate. Status: " + clientHttpResponse.getStatusCode().value());
        log.info("Body: " + IOUtils.toString(clientHttpResponse.getBody()));
    }

}

