package com.exrates.inout.properties.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(builderClassName = "Builder", toBuilder = true)
@ToString
public class BitcoinNode {

    private boolean zmqEnabled;
    private boolean supportInstantSend;
    private boolean enabled;

    private String rpcProtocol;
    private String rpcHost;
    private String rpcPort;
    private String rpcUser;
    private String rpcPassword;

    private String httpAuthSchema;

    private String notificationAlertPort;
    private String notificationBlockPort;
    private String notificationWalletPort;
    private String notificationInstantSendPort;
}
