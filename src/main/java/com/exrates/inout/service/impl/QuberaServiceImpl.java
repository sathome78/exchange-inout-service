package com.exrates.inout.service.impl;

import com.exrates.inout.dao.QuberaDao;
import com.exrates.inout.domain.constants.Constants;
import com.exrates.inout.domain.dto.AccountCreateDto;
import com.exrates.inout.domain.dto.AccountQuberaRequestDto;
import com.exrates.inout.domain.dto.AccountQuberaResponseDto;
import com.exrates.inout.domain.dto.QuberaRequestDto;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.enums.invoice.RefillStatusEnum;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.exceptions.NgDashboardException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestIdNeededException;
import com.exrates.inout.properties.models.QuberaConfig;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.QuberaService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.UserService;
import com.exrates.inout.service.component.KycHttpClient;
import com.google.common.collect.Maps;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class QuberaServiceImpl implements QuberaService {

    private static final Logger logger = LogManager.getLogger(QuberaServiceImpl.class);

    private final CurrencyService currencyService;
    private final GtagService gtagService;
    private final MerchantService merchantService;
    private final RefillService refillService;
    private final QuberaDao quberaDao;
    private final KycHttpClient kycHttpClient;
    private final UserService userService;

    private int thresholdLength;
    private int poolId;

    @Autowired
    public QuberaServiceImpl(CurrencyService currencyService, GtagService gtagService, MerchantService merchantService,
                             RefillService refillService, QuberaDao quberaDao, KycHttpClient kycHttpClient,
                             UserService userService, QuberaConfig quberaConfig) {
        this.currencyService = currencyService;
        this.gtagService = gtagService;
        this.merchantService = merchantService;
        this.refillService = refillService;
        this.quberaDao = quberaDao;
        this.kycHttpClient = kycHttpClient;
        this.userService = userService;

        this.thresholdLength = quberaConfig.getThresholdLength();
        this.poolId = quberaConfig.getPoolId();
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Integer requestId = request.getId();
        if (requestId == null) {
            throw new RefillRequestIdNeededException(request.toString());
        }
        Map<String, String> details = quberaDao.getUserDetailsForCurrency(request.getUserId(), request.getCurrencyId());
        Map<String, String> refillParams = Maps.newHashMap();
        String iban = details.getOrDefault("iban", "");
        String accountNumber = details.getOrDefault("accountNumber", "");
        refillParams.put("iban", iban);
        refillParams.put("currency", request.getCurrencyName());
        refillParams.put("accountNumber", accountNumber);
        return refillParams;
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        Currency currency = currencyService.findByName(params.get("currency"));
        Merchant merchant = merchantService.findByName("Qubera");
        int userId = quberaDao.findUserIdByAccountNumber(params.get("accountNumber"));

        String paymentAmount = params.getOrDefault("paymentAmount", "0");
        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .requestId(0)
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(new BigDecimal(paymentAmount))
                .merchantTransactionId(params.get("paymentId"))
                .toMainAccountTransferringConfirmNeeded(this.toMainAccountTransferringConfirmNeeded())
                .build();
        Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto, userId, 0, RefillStatusEnum.ACCEPTED_AUTO);
        requestAcceptDto.setRequestId(requestId);

        refillService.autoAcceptRefillRequest(requestAcceptDto);
        // todo send notification to transfer to master account

        final String username = refillService.getUsernameByRequestId(requestId);

        logger.debug("Process of sending data to Google Analytics...");
        gtagService.sendGtagEvents(paymentAmount, currency.getName(), username);
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        return null;
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return false;
    }

    @Override
    public boolean logResponse(QuberaRequestDto requestDto) {
        return quberaDao.logResponse(requestDto);
        //todo send email
    }

    @Override
    public AccountQuberaResponseDto createAccount(AccountCreateDto accountCreateDto) {
        String account = accountCreateDto.getStringFromParams();
        User user = userService.findByEmail(accountCreateDto.getEmail());
        Currency currency = currencyService.findByName(accountCreateDto.getCurrencyCode());
        if (account.length() >= thresholdLength) {
            String error = "Count chars of request is over limit {}" + account.length();
            logger.error(error);
            throw new NgDashboardException(error, Constants.ErrorApi.QUBERA_PARAMS_OVER_LIMIT);
        }

        AccountQuberaRequestDto requestDto = new AccountQuberaRequestDto(account, accountCreateDto.getCurrencyCode(), poolId);
        AccountQuberaResponseDto responseDto = kycHttpClient.createAccount(requestDto);
        boolean saveUserDetails = quberaDao.saveUserDetails(user.getId(), currency.getId(),
                responseDto.getAccountNumber(), responseDto.getIban());

        if (saveUserDetails) {
            return responseDto;
        } else {
            throw new NgDashboardException("Error while saving response",
                    Constants.ErrorApi.QUBERA_SAVE_ACCOUNT_RESPONSE_ERROR);
        }
    }
}
