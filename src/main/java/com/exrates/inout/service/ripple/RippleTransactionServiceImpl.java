package com.exrates.inout.service.ripple;

import com.exrates.inout.domain.dto.RippleAccount;
import com.exrates.inout.domain.dto.RippleTransaction;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.RippleCheckConsensusException;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.Map;

@Log4j2(topic = "ripple_log")
@Service
public class RippleTransactionServiceImpl implements RippleTransactionService {

    private static final Integer XRP_AMOUNT_MULTIPLIER = 1000000;
    private static final Integer XRP_DECIMALS = 6;
    private static final BigDecimal XRP_MIN_BALANCE = new BigDecimal(21);
    private static final String SEQUENCE_PARAM = "sequence";
    private static final String LEDGER = "ledger";

    @Value("${ripple.node.account.address}")
    private String address;
    @Value("${ripple.node.account.secret}")
    private String secret;

    @Autowired
    private RippledNodeService rippledNodeService;

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        RippleAccount account = RippleAccount.builder().name(address).secret(secret).build();
        BigDecimal accountFromBalance = getAccountBalance(address);
        if (accountFromBalance.compareTo(XRP_MIN_BALANCE) < 0) {
            throw new InsufficientCostsInWalletException("XRP BALANCE LOW");
        }
        Integer destinationTag = StringUtils.isEmpty(withdrawMerchantOperationDto.getDestinationTag()) ?
                null : Integer.parseInt(withdrawMerchantOperationDto.getDestinationTag());
        RippleTransaction transaction = this.sendMoney(account, new BigDecimal(withdrawMerchantOperationDto.getAmount()),
                withdrawMerchantOperationDto.getAccountTo(), destinationTag);
        log.debug("xrp transaction sended {}", transaction);
        return rippleTransactionToMap(transaction);
    }

    /*send xrp*/
    @Transactional
    private RippleTransaction sendMoney(RippleAccount account, BigDecimal amount, String destinationAccount, Integer destinationTag) {
        RippleTransaction transaction = prepareTransaction(amount, account, destinationAccount, destinationTag);
        rippledNodeService.signTransaction(transaction);
        rippledNodeService.submitTransaction(transaction);
        return transaction;
    }

    /*https://ripple.com/build/reliable-transaction-submission/*/
    private RippleTransaction prepareTransaction(BigDecimal amount, RippleAccount account, String destinationAccount, Integer destinationTag) {
        Integer lastValidatedLedger = rippledNodeService.getServerState()
                .getJSONObject("state")
                .getJSONObject("validated_ledger")
                .getInt("seq") + 4;
        Integer sequence = rippledNodeService.getAccountInfo(address)
                .getJSONObject("account_data")
                .getInt("Sequence");
        /*todo check if destination account exist? because we spent comisiion if trying transfer to such address*/
        return RippleTransaction.builder()
                .amount(amount)
                .sendAmount(normalizeAmountToXRPString(amount))
                .destinationAddress(destinationAccount)
                .issuerAddress(account.getName())
                .issuerSecret(account.getSecret())
                .destinationTag(destinationTag)
                .lastValidatedLedger(lastValidatedLedger)
                .sequence(sequence)
                .build();
    }

    private String normalizeAmountToXRPString(BigDecimal amount) {
        return amount
                .setScale(XRP_DECIMALS, RoundingMode.HALF_DOWN)
                .multiply(new BigDecimal(XRP_AMOUNT_MULTIPLIER))
                .toBigInteger()
                .toString();
    }

    @Override
    public BigDecimal normalizeAmountToDecimal(String amount) {
        return new BigDecimal(amount)
                .divide(new BigDecimal(XRP_AMOUNT_MULTIPLIER))
                .setScale(XRP_DECIMALS, RoundingMode.HALF_DOWN);
    }

    @Override
    public BigDecimal getAccountBalance(String accountName) {
        JSONObject accountData = rippledNodeService.getAccountInfo(accountName)
                .getJSONObject("account_data");
        return normalizeAmountToDecimal(accountData.getString("Balance"));
    }

    /*this check is based on https://ripple.com/build/reliable-transaction-submission/*/
    @Override
    public boolean checkSendedTransactionConsensus(String txHash, String additionalParams) {
        JSONObject params = new JSONObject(additionalParams);
        JSONObject responseBody = rippledNodeService.getTransaction(txHash);
        log.debug("tx_response {}", responseBody);
        String result = responseBody.getJSONObject("meta").getString("TransactionResult");
        if (responseBody.has("inLedger")) {
            if (result.equals("tesSUCCESS")) {
                if (responseBody.getBoolean("validated")) {
                    return true;
                }
            } else {
                throw new RippleCheckConsensusException();
            }
        } else if (params.getInt(LEDGER) > responseBody.getInt("ledger_index")) {
            return false;
        } else {
            throw new RippleCheckConsensusException();
        }
        return false;
    }

    private Map<String, String> rippleTransactionToMap(RippleTransaction rippleTransaction) {
        Map<String, String> map = new HashMap<>();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(SEQUENCE_PARAM, rippleTransaction.getSequence());
        jsonObject.put(LEDGER, rippleTransaction.getLastValidatedLedger());
        log.debug("transaction {}", jsonObject + " " + rippleTransaction.getTxHash());
        map.put("hash", rippleTransaction.getTxHash());
        map.put("params", jsonObject.toString());
        return map;
    }
}
