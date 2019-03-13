package com.exrates.inout.controller.interceptor;

import com.exrates.inout.properties.SsmProperties;
import lombok.Getter;
import me.exrates.SSMGetter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Getter
public class TokenInterceptor extends HandlerInterceptorAdapter {
    private final String AUTH_TOKEN_VALUE;
    private final String AUTH_TOKEN_NAME = "AUTH_TOKEN";

    public TokenInterceptor(SSMGetter ssmGetter, SsmProperties ssmProperties) {
        this.AUTH_TOKEN_VALUE = ssmGetter.lookup(ssmProperties.getPath());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String token = request.getHeader(AUTH_TOKEN_NAME);
        if(token == null || !token.equals(AUTH_TOKEN_VALUE)) {
            response.setStatus(403);
            response.getWriter().write("Incorrect token");
            return false;
        }
        else return true;
    }
}