package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.dao.RefillRequestDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.RefillRequestFlatDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.NotImplimentedMethod;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestIdNeededException;
import com.exrates.inout.exceptions.RefillRequestNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.AlgorithmService;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.merchant.InterkassaService;
import com.exrates.inout.service.utils.WithdrawUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

@Service
public class InterkassaServiceImpl implements InterkassaService {

    private static final Logger LOGGER = LogManager.getLogger("merchant");

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    private AlgorithmService algorithmService;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private RefillRequestDao refillRequestDao;
    @Autowired
    private WithdrawUtils withdrawUtils;

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        throw new NotImplimentedMethod("for " + withdrawMerchantOperationDto);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer requestId = request.getId();
        if (requestId == null) {
            throw new RefillRequestIdNeededException(request.toString());
        }
        BigDecimal sum = request.getAmount();
        String currency = request.getCurrencyName();
        BigDecimal amountToPay = sum.setScale(2, BigDecimal.ROUND_HALF_UP);
        /**/
        final Map<String, String> map = new TreeMap<>();

        map.put("ik_am", String.valueOf(amountToPay));
        map.put("ik_co_id", ccp.getPaymentSystemMerchants().getInterkassa().getCheckoutId());
        map.put("ik_cur", currency);
        map.put("ik_desc", "Exrates input");
        map.put("ik_ia_m", "post");
        map.put("ik_ia_u", ccp.getPaymentSystemMerchants().getInterkassa().getStatustUrl());
        map.put("ik_pm_no", String.valueOf(requestId));
        map.put("ik_pnd_m", "post");
        map.put("ik_pnd_u", ccp.getPaymentSystemMerchants().getInterkassa().getStatustUrl());
        map.put("ik_suc_u", ccp.getPaymentSystemMerchants().getInterkassa().getSuccesstUrl());
        map.put("ik_suc_m", "post");

        map.put("ik_sign", getSignature(map));

        Properties properties = new Properties();
        properties.putAll(map);
        /**/
        return generateFullUrlMap(ccp.getPaymentSystemMerchants().getInterkassa().getUrl(), "POST", properties);
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Integer requestId = Integer.valueOf(params.get("ik_pm_no"));
        String merchantTransactionId = params.get("ik_trn_id");
        Currency currency = currencyService.findByName(params.get("ik_cur"));
        Merchant merchant = merchantService.findByName("Interkassa");
        BigDecimal amount = BigDecimal.valueOf(Double.parseDouble(params.get("ik_am"))).setScale(9);
        String signature = params.get("ik_sign");
        params.remove("ik_sign");
        String checkSignature = getSignature(new TreeMap<String, String>(params));

        RefillRequestFlatDto refillRequest = refillRequestDao.getFlatByIdAndBlock(requestId)
                .orElseThrow(() -> new RefillRequestNotFoundException(String.format("refill request id: %s", requestId)));
        if (checkSignature.equals(signature)
                && params.get("ik_co_id").equals(ccp.getPaymentSystemMerchants().getInterkassa().getCheckoutId())
                && params.get("ik_inv_st").equals("success")
                && refillRequest.getAmount().equals(amount)) {
            RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                    .requestId(requestId)
                    .merchantId(merchant.getId())
                    .currencyId(currency.getId())
                    .amount(amount)
                    .merchantTransactionId(merchantTransactionId)
                    .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                    .build();
            refillService.autoAcceptRefillRequest(requestAcceptDto);
        }
    }

    private String getSignature(final Map<String, String> params) {
        List<String> listValues = new ArrayList<String>(params.values());

        listValues.add(ccp.getPaymentSystemMerchants().getInterkassa().getSecretKey());
        String stringValues = StringUtils.join(listValues, ":");
        byte[] signMD5 = algorithmService.computeMD5Byte(stringValues);

        return Base64.getEncoder().encodeToString(signMD5);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }
}
