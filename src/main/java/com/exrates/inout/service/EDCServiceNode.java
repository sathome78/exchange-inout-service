package com.exrates.inout.service;

import java.io.IOException;

public interface EDCServiceNode {

    void transferFromMainAccount(String accountName, String amount) throws IOException, InterruptedException;
}
