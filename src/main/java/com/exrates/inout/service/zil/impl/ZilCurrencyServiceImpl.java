package com.exrates.inout.service.zil.impl;

import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.ZilProperty;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.zil.ZilCurrencyService;
import com.firestack.laksaj.account.Wallet;
import com.firestack.laksaj.crypto.KeyTools;
import com.firestack.laksaj.jsonrpc.HttpProvider;
import com.firestack.laksaj.jsonrpc.Rep;
import com.firestack.laksaj.transaction.Transaction;
import com.firestack.laksaj.transaction.TransactionFactory;
import com.firestack.laksaj.utils.Bech32;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.security.InvalidAlgorithmParameterException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import static com.firestack.laksaj.account.Wallet.pack;

@Log4j2(topic = "zil_log")
@Service
public class ZilCurrencyServiceImpl implements ZilCurrencyService {

    public static final String DEFAULT_GAS_PRISE = "1000000000";
    private static final String DEFAULT_FACTOR = "1000000000000";

    private static final String CODE_FROM_AWS = "zil_coin\":\"";

    private static HttpProvider client;
    private ZilProperty zilProperty;

    private final AlgorithmService algorithmService;

    @Autowired
    public ZilCurrencyServiceImpl(CryptoCurrencyProperties ccp, AlgorithmService algorithmService){
        this.zilProperty = ccp.getZilCoins().getZil();
        this.algorithmService = algorithmService;
    }

    @PostConstruct
    private void init(){
        client = new HttpProvider("https://api.zilliqa.com/");
    }

    public String generatePrivateKey(){
        String privKey = "";
        try {
            privKey = KeyTools.generatePrivateKey();
        } catch (InvalidAlgorithmParameterException e) {
            log.error(e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
        } catch (NoSuchProviderException e) {
            log.error(e);
        }
        return privKey;
    }

    public String getPublicKeyFromPrivateKey(String privKey){
        return KeyTools.getPublicKeyFromPrivateKey(privKey, true);
    }

    public String getAddressFromPrivateKey(String privKey){
        String address = KeyTools.getAddressFromPrivateKey(privKey);
        try {
            return Bech32.toBech32Address(address);
        } catch (Exception e) {
            log.error(e);
        }
        return "";
    }

    public void createTransaction(RefillRequestAddressDto dto) throws Exception {

        String privKey = algorithmService.decodeByKey(CODE_FROM_AWS, dto.getPrivKey());
        String pubKey = dto.getPubKey();

        BigDecimal accountAmount = getAmount(Bech32.fromBech32Address(dto.getAddress()));
        BigDecimal fee = getFee();
        String amount = bigDecimalToTransactionString(accountAmount.subtract(fee));

        Wallet wallet = new Wallet();
        wallet.addByPrivateKey(privKey);
        Transaction transaction = Transaction.builder()
                .version(String.valueOf(pack(1, 1)))
                .toAddr(zilProperty.getZilMainaddress().toLowerCase())
                .senderPubKey(pubKey.toLowerCase())
                .amount(amount)
                .gasPrice(DEFAULT_GAS_PRISE)
                .gasLimit("1")
                .code("")
                .data("")
                .provider(new HttpProvider("https://api.zilliqa.com"))
                .build();
        transaction = wallet.sign(transaction);

        // Send a transaction to the network
        HttpProvider.CreateTxResult result = TransactionFactory.createTransaction(transaction);
    }

    public BigDecimal getAmount(String address) throws Exception {
        Rep<HttpProvider.BalanceResult> balance = client.getBalance(address);
        return new BigDecimal(balance.getResult().getBalance());
    }

    public BigDecimal getFee(){
        return new BigDecimal(DEFAULT_GAS_PRISE);
    }

    public BigDecimal scaleAmountToZilFormat(BigDecimal amount){
        BigDecimal factor = new BigDecimal(DEFAULT_FACTOR);
        return amount.divide(factor);
    }

    private String bigDecimalToTransactionString(BigDecimal num){
        String str = num.toString();
        if (str.contains(".")){
            return str.substring(0, str.indexOf("."));
        }
        return str;
    }
}
