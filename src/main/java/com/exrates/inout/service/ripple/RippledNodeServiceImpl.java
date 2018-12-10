package com.exrates.inout.service.ripple;

import com.exrates.inout.domain.dto.RippleTransaction;
import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.MerchantException;
import com.exrates.inout.util.RestUtil;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Log4j2(topic = "ripple_log")
@Service
public class RippledNodeServiceImpl implements RippledNodeService {

    private static final String SIGN_RPC = "{\n" +
            "                     \"method\": \"sign\",\n" +
            "                     \"params\": [\n" +
            "                         {\n" +
            "                             \"offline\": false,\n" +
            "                             \"secret\": \"%s\",\n" +
            "                             \"tx_json\": {\n" +
            "                                 \"Account\": \"%s\",\n" +
            "                                 \"Sequence\": \"%d\",\n" +
            "                                 \"LastLedgerSequence\": \"%d\",\n" +
            "                                 \"Amount\":  \"%s\",\n" +
            "                                 \"Destination\": \"%s\",\n" +
            "                                 \"TransactionType\": \"Payment\"" +
            "%s" +
            "                             },\n" +
            "                             \"fee_mult_max\": 1000\n" +
            "                         }\n" +
            "                     ]\n" +
            "                 }";

    private static final String DESTINATION_TAG_FIELD = ", \n\"DestinationTag\": \"%d\"";

    private static final String SUBMIT_TRANSACTION_RPC = "{\n" +
            "                     \"method\": \"submit\",\n" +
            "                     \"params\": [\n" +
            "                         {\n" +
            "                             \"tx_blob\": \"%s\"\n" +
            "                         }\n" +
            "                     ]\n" +
            "                 }";

    private final static String GET_TRANSACTION_RPC = "{\"method\": \"tx\",\n" +
            "                     \"params\": [\n" +
            "                         {\n" +
            "                             \"transaction\": \"%s\",\n" +
            "                             \"binary\": false\n" +
            "                         }\n" +
            "                     ]}";

    private static final String GET_ACCOUNT_RPC = "{\n" +
            "                     \"method\": \"account_info\",\n" +
            "                     \"params\": [\n" +
            "                         {\n" +
            "                             \"account\": \"%s\",\n" +
            "                             \"strict\": true,\n" +
            "                             \"ledger_index\": \"current\",\n" +
            "                             \"queue\": true\n" +
            "                         }\n" +
            "                     ]\n" +
            "                 }";

    private static final String WALLET_PORPOSE_RPC = "{\"method\": \"wallet_propose\",\n" +
            "                                           \"params\": [\n" +
            "\n" +
            "                                           ]}";

    private static final String SERVER_STATE = "{\"method\": \"server_state\",\n" +
            "                                           \"id\": \"1\"}";

    @Value("${ripple.rippled-rpc-url}")
    private String rpcUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public void signTransaction(RippleTransaction transaction) {
        String destinationTagParam = transaction.getDestinationTag() == null ? "" : String.format(DESTINATION_TAG_FIELD, transaction.getDestinationTag());
        String requestBody = String.format(SIGN_RPC, transaction.getIssuerSecret(), transaction.getIssuerAddress(),
                transaction.getSequence(), transaction.getLastValidatedLedger(),
                transaction.getSendAmount(), transaction.getDestinationAddress(), destinationTagParam);
        ResponseEntity<String> response = restTemplate.postForEntity(rpcUrl, requestBody, String.class);
        if (RestUtil.isError(response.getStatusCode())) {
            throw new RuntimeException("cant sign transaction");
        }
        log.debug("resp {}", response.getBody());
        String blob = new JSONObject(response.getBody()).getJSONObject("result").getString("tx_blob");
        transaction.setBlob(blob);
        transaction.setTxSigned(true);
    }

    @Override
    public void submitTransaction(RippleTransaction transaction) {
        String requestBody = String.format(SUBMIT_TRANSACTION_RPC, transaction.getBlob());
        ResponseEntity<String> response = restTemplate.postForEntity(rpcUrl, requestBody, String.class);
        if (RestUtil.isError(response.getStatusCode())) {
            throw new RuntimeException("can't submit transaction");
        }
        JSONObject result = new JSONObject(response.getBody()).getJSONObject("result");
        log.debug("response {}", response.getBody());
        String engineResult = result.getString("engine_result");
        if (engineResult.equals("tesSUCCESS")) {
            JSONObject respTransaction = result.getJSONObject("tx_json");
            transaction.setTxHash(respTransaction.getString("hash"));
        } else if (result.getString("engine_result").equals("tecUNFUNDED_PAYMENT")) {
            throw new InsufficientCostsInWalletException("XRP BALANCE LOW");
        } else {
            throw new MerchantException(result.toString());
        }
    }

    @Override
    public JSONObject getTransaction(String txHash) {
        String requestBody = String.format(GET_TRANSACTION_RPC, txHash);
        ResponseEntity<String> response = restTemplate.postForEntity(rpcUrl, requestBody, String.class);
        JSONObject jsonResponse = new JSONObject(response.getBody()).getJSONObject("result");
        if (RestUtil.isError(response.getStatusCode())) {
            log.error("error checking transaction {}", response.getBody());
            throw new RuntimeException(jsonResponse.getString("error_message"));
        }
        return jsonResponse;
    }

    @Override
    public JSONObject getAccountInfo(String accountName) {
        String requestBody = String.format(GET_ACCOUNT_RPC, accountName);
        ResponseEntity<String> response = restTemplate.postForEntity(rpcUrl, requestBody, String.class);
        if (RestUtil.isError(response.getStatusCode())) {
            throw new RuntimeException("cant get account Info");
        }
        log.debug("xrp_acc {}", response.getBody());
        return new JSONObject(response.getBody()).getJSONObject("result");
    }

    @Override
    public JSONObject getServerState() {
        ResponseEntity<String> response = restTemplate.postForEntity(rpcUrl, SERVER_STATE, String.class);
        if (RestUtil.isError(response.getStatusCode())) {
            throw new RuntimeException("cant get server state xrp");
        }
        return new JSONObject(response.getBody()).getJSONObject("result");
    }
}
