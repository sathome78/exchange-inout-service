package com.exrates.inout.domain.dto;

import com.exrates.inout.domain.enums.UserOperationAuthority;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;

import java.util.Locale;

@Getter
@Setter
public class UserOperationAuthorityOption {
    private UserOperationAuthority userOperationAuthority;
    private Boolean enabled;

    private String userOperationAuthorityLocalized;

    public void localize(MessageSource messageSource, Locale locale) {
        userOperationAuthorityLocalized = userOperationAuthority.toString(messageSource, locale);
    }

    @Override
    public String toString() {
        return "UserOperationAuthorityOption{" +
                ", userOperationAuthority=" + userOperationAuthority +
                ", enabled=" + enabled +
                '}';
    }
}