package com.exrates.inout.util;

import com.exrates.inout.domain.enums.UserRole;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Component
public class RequestUtils {
    public static int extractUserId(HttpServletRequest request){
        return Integer.parseInt(request.getHeader("user_id"));
    }

    public static UserRole extractUserRole(HttpServletRequest request){
        return UserRole.valueOf(request.getHeader("user_role"));
    }

    public static Locale extractUserLocale(HttpServletRequest request){
        return new Locale(request.getHeader("locale"));
    }
}
