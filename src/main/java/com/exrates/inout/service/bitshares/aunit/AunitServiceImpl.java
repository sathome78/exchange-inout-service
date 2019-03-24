package com.exrates.inout.service.bitshares.aunit;

import com.exrates.inout.service.bitshares.BitsharesServiceImpl;
import org.springframework.stereotype.Service;

import javax.websocket.ClientEndpoint;

@Service("aunitServiceImpl")
@ClientEndpoint
public class AunitServiceImpl extends BitsharesServiceImpl {

    private final static String name = "AUNIT";
    private static final int DECIMAL = 5;

    public AunitServiceImpl() {
        super(name, name, "merchants/aunit.properties", 7, DECIMAL);
    }
}
