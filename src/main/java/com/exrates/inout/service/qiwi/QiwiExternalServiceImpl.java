package com.exrates.inout.service.qiwi;

import com.exrates.inout.domain.dto.qiwi.request.QiwiRequest;
import com.exrates.inout.domain.dto.qiwi.request.QiwiRequestGetTransactions;
import com.exrates.inout.domain.dto.qiwi.request.QiwiRequestHeader;
import com.exrates.inout.domain.dto.qiwi.response.QiwiResponse;
import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseP2PInvoice;
import com.exrates.inout.domain.dto.qiwi.response.QiwiResponseTransaction;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.QiwiProperty;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

@Service
//@Log4j2(topic = "Qiwi")
@Profile("!dev")
public class QiwiExternalServiceImpl implements QiwiExternalService{

    private static final Logger log = LogManager.getLogger("Qiwi");

    private final static String URL_GET_TRANSACTIONS = "/transfer/get-merchant-tx";
    private final static String URL_GENERATE_P2P_INVOICE_WITH_UNIQ_MEMO = "/transfer/tx-merchant-wallet";

    private String baseUrl;
    private int startPosition;
    private int limit;

    private RestTemplate qiwiRestTemplate;

    @Autowired
    public QiwiExternalServiceImpl(CryptoCurrencyProperties cryptoCurrencyProperties, @Qualifier(value = "qiwiRestTemplate") RestTemplate qiwiRestTemplate){
        QiwiProperty qiwiProperty = cryptoCurrencyProperties.getPaymentSystemMerchants().getQiwi();

        this.baseUrl = qiwiProperty.getProductionUrl();
        this.startPosition = qiwiProperty.getTransactionStartedPosition();
        this.limit = qiwiProperty.getTransactionLimit();

        this.qiwiRestTemplate = qiwiRestTemplate;
    }

    public String generateUniqMemo(int userId) {
        QiwiRequestHeader requestHeader = new QiwiRequestHeader("p2pInvoiceRequest");

        QiwiRequest request = new QiwiRequest(requestHeader, null);

        ResponseEntity<QiwiResponseP2PInvoice> response = qiwiRestTemplate.postForEntity(baseUrl+URL_GENERATE_P2P_INVOICE_WITH_UNIQ_MEMO, request , QiwiResponseP2PInvoice.class );

        log.info("*** Qiwi *** | Generate new uniq memo. UserId:"+userId+" | Memo:"+response.getBody().getResponseData().getComment());

        return response.getBody().getResponseData().getComment();
    }

    public List<QiwiResponseTransaction> getLastTransactions() {
        QiwiRequestHeader requestHeader = new QiwiRequestHeader("fetchMerchTx");
        QiwiRequestGetTransactions requestBody = new QiwiRequestGetTransactions(startPosition, limit);

        QiwiRequest request = new QiwiRequest(requestHeader, requestBody);

        ResponseEntity<QiwiResponse> response = qiwiRestTemplate.postForEntity(baseUrl+URL_GET_TRANSACTIONS, request , QiwiResponse.class );

        QiwiResponseTransaction[] trans = response.getBody().getResponseData().getTransactions();

        return Arrays.asList(trans);
    }
}
