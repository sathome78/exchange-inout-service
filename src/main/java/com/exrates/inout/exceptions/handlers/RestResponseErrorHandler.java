package com.exrates.inout.exceptions.handlers;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import com.exrates.inout.util.RestUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

//@Log4j2
public class RestResponseErrorHandler implements ResponseErrorHandler {

   private static final Logger log = LogManager.getLogger(RestResponseErrorHandler.class);


    public void handleError(ClientHttpResponse response) throws IOException {
        log.error("Response error: {} {}", response.getStatusCode(), response.getBody());
    }

    public boolean hasError(ClientHttpResponse response) throws IOException {
        return RestUtil.isError(response.getStatusCode());
    }
}
