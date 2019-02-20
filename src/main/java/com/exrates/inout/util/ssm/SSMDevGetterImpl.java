package com.exrates.inout.util.ssm;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile("dev")
public class SSMDevGetterImpl implements SSMGetter {

    @Override
    public String lookup(String password) {
        return "root"; //TODO
    }
}
