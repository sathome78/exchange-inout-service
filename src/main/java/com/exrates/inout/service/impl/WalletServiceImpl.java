package com.exrates.inout.service.impl;

import com.exrates.inout.dao.WalletDao;
import com.exrates.inout.domain.dto.TransferDto;
import com.exrates.inout.domain.dto.WalletInnerTransferDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.other.WalletOperationData;
import com.exrates.inout.exceptions.*;
import com.exrates.inout.properties.EndpointProperties;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static java.math.BigDecimal.ROUND_HALF_UP;
import static java.math.BigDecimal.ZERO;

//@Log4j2
@Service
@Transactional
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

   private static final Logger log = LogManager.getLogger(WalletServiceImpl.class);


    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.S");

    private static final int decimalPlaces = 9;
    private static final String FIND_WALLET_BY_USER_AND_CURRENCY = "/findWalletByUserAndCurrency";
    private static final String CHECK_WALLET_ENOUGH_MONEY = "/isEnoughWalletMoney";
    private static final String GET_WALLET_ABALANCE = "/getWalletABalance";
    private static final String WALLET_INNER_TRANSFER = "/walletInnerTransfer";

    @Autowired
    private WalletDao walletDao;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private UserService userService;
    @Autowired
    private CommissionService commissionService;
    @Autowired
    private CompanyWalletService companyWalletService;
    @Autowired
    private NotificationService notificationService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RestTemplate template;
    @Autowired
    private EndpointProperties endpoints;

    @Override
    public int getWalletId(int userId, int currencyId) {
        Wallet wallet = findByUserAndCurrency(userId, currencyId);

        return wallet == null ? 0 : wallet.getId();
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public BigDecimal getWalletABalance(int walletId) {
        HttpHeaders headers = getHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoints.getStock() + endpoints.getInoutPrefix() + GET_WALLET_ABALANCE)
                .queryParam("walletId", walletId);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<BigDecimal> response = template.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                BigDecimal.class);
        return response.getBody();
    }

    @Transactional(readOnly = true)
    @Override
    public boolean ifEnoughMoney(int walletId, BigDecimal amountForCheck) {
        HttpHeaders headers = getHeaders();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoints.getStock() + endpoints.getInoutPrefix() + CHECK_WALLET_ENOUGH_MONEY)
                .queryParam("walletId", walletId)
                .queryParam("amountForCheck", amountForCheck);

        HttpEntity<?> entity = new HttpEntity<>(headers);

        ResponseEntity<Boolean> response = template.exchange(
                builder.toUriString(),
                HttpMethod.GET,
                entity,
                Boolean.class);
        return response.getBody();
    }

    @Override
    @Transactional(readOnly = true)
    @SneakyThrows
    public Wallet findByUserAndCurrency(int userId, int currencyId) {
        try {
            HttpHeaders headers = getHeaders();

            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoints.getStock() + endpoints.getInoutPrefix() + FIND_WALLET_BY_USER_AND_CURRENCY)
                    .queryParam("userId", userId)
                    .queryParam("currencyId", currencyId);

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<String> response = template.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    String.class);
            String body = response.getBody();
            System.out.println("Body = " + body);//debug, todo remove
            return new ObjectMapper().readValue(body, Wallet.class);
        } catch (Exception e){
            log.error(e);
            throw e;
        }
    }

    @Override
    public Wallet create(User user, Currency currency) {
        final Wallet wallet = walletDao.createWallet(user, currency.getId());
        wallet.setName(currency.getName());
        return wallet;
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void depositActiveBalance(final Wallet wallet, final BigDecimal sum) {
        walletDao.addToWalletBalance(wallet.getId(), sum, BigDecimal.ZERO);
    }

    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void withdrawReservedBalance(final Wallet wallet, final BigDecimal sum) {
        wallet.setReservedBalance(wallet.getReservedBalance().subtract(sum).setScale(decimalPlaces, ROUND_HALF_UP));
        if (wallet.getReservedBalance().compareTo(ZERO) < 0) {
            throw new NotEnoughUserWalletMoneyException("Not enough money to withdraw on user wallet " + wallet);
        }
        walletDao.addToWalletBalance(wallet.getId(), BigDecimal.ZERO, sum.negate());
    }

    @Override
    @SneakyThrows
    public WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description) {
        HttpHeaders headers = getHeaders();

        WalletInnerTransferDto walletInnerTransferDto = WalletInnerTransferDto.builder()
                .walletId(walletId)
                .amount(amount)
                .description(description)
                .sourceId(sourceId)
                .sourceType(sourceType)
                .build();

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(endpoints.getStock() + endpoints.getInoutPrefix() + WALLET_INNER_TRANSFER);

        HttpEntity<?> entity = new HttpEntity<>(new ObjectMapper().writeValueAsString(walletInnerTransferDto), headers);


        ResponseEntity<WalletTransferStatus> response = template.exchange(
                builder.toUriString(),
                HttpMethod.POST,
                entity,
                WalletTransferStatus.class);

        return response.getBody();
    }

    @Override
    public WalletTransferStatus walletBalanceChange(final WalletOperationData walletOperationData) {
        return walletDao.walletBalanceChange(walletOperationData);
    }

    private void changeWalletActiveBalance(BigDecimal amount, Wallet wallet, OperationType operationType,
                                           TransactionSourceType transactionSourceType) {
        changeWalletActiveBalance(amount, wallet, operationType, transactionSourceType, null, null);
    }

    private void changeWalletActiveBalance(BigDecimal amount, Wallet wallet, OperationType operationType,
                                           TransactionSourceType transactionSourceType,
                                           BigDecimal specialCommissionAmount, Integer sourceId) {
        WalletOperationData walletOperationData = new WalletOperationData();
        walletOperationData.setWalletId(wallet.getId());
        walletOperationData.setAmount(amount);
        walletOperationData.setBalanceType(WalletOperationData.BalanceType.ACTIVE);
        walletOperationData.setOperationType(operationType);
        walletOperationData.setSourceId(sourceId);
        Commission commission = commissionService.findCommissionByTypeAndRole(operationType, userService.getUserRoleFromSecurityContext());
        walletOperationData.setCommission(commission);
        BigDecimal commissionAmount = specialCommissionAmount == null ?
                BigDecimalProcessing.doAction(amount, commission.getValue(), ActionType.MULTIPLY_PERCENT) : specialCommissionAmount;
        walletOperationData.setCommissionAmount(commissionAmount);
        walletOperationData.setSourceType(transactionSourceType);
        WalletTransferStatus status = walletBalanceChange(walletOperationData);
        if (status != WalletTransferStatus.SUCCESS) {
            throw new BalanceChangeException(status.name());
        }
        if (commissionAmount.signum() > 0) {

            CompanyWallet companyWallet = companyWalletService.findByCurrency(currencyService.getById(wallet.getCurrencyId()));
            companyWalletService.deposit(companyWallet, BigDecimal.ZERO, commissionAmount);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransferDto transferCostsToUser(Integer fromUserWalletId, Integer toUserId, BigDecimal amount,
                                           BigDecimal commissionAmount, Locale locale, int sourceId) {
        if (amount.signum() <= 0) {
            throw new InvalidAmountException(messageSource.getMessage("transfer.negativeAmount", null, locale));
        }
        Wallet fromUserWallet = walletDao.findById(fromUserWalletId);
        Integer currencyId = fromUserWallet.getCurrencyId();
        BigDecimal inputAmount = BigDecimalProcessing.doAction(amount, commissionAmount, ActionType.SUBTRACT);
        log.debug(commissionAmount.toString());
        log.debug(inputAmount.toString());
        if (inputAmount.compareTo(fromUserWallet.getActiveBalance()) > 0) {
            throw new InvalidAmountException(messageSource.getMessage("transfer.invalidAmount", null, locale));
        }
        Wallet toUserWallet = walletDao.findByUserAndCurrency(toUserId, currencyId);
        if (toUserWallet == null) {
            throw new WalletNotFoundException(messageSource.getMessage("transfer.walletNotFound", null, locale));
        }
        changeWalletActiveBalance(amount, fromUserWallet, OperationType.OUTPUT,
                TransactionSourceType.USER_TRANSFER, commissionAmount, sourceId);
        changeWalletActiveBalance(inputAmount, toUserWallet, OperationType.INPUT,
                TransactionSourceType.USER_TRANSFER, BigDecimal.ZERO, sourceId);
        String notyAmount = inputAmount.setScale(decimalPlaces, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
        return TransferDto.builder()
                .comissionAmount(commissionAmount)
                .notyAmount(notyAmount)
                .walletUserFrom(fromUserWallet)
                .walletUserTo(toUserWallet)
                .initialAmount(amount)
                .currencyId(currencyId)
                .userFromId(fromUserWallet.getUser().getId())
                .userToId(toUserId)
                .build();
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String transferCostsToUser(Integer userId, Integer fromUserWalletId, Integer toUserId, BigDecimal amount,
                                      BigDecimal comission, Locale locale, int sourceId) {
        User toUser = userService.getUserById(toUserId);
        String toUserNickname = toUser.getNickname() != null ? toUser.getNickname() : toUser.getEmail();
        if (toUserId == 0) {
            throw new UserNotFoundException(messageSource.getMessage("transfer.userNotFound", new Object[]{toUserNickname}, locale));
        }
        TransferDto dto = transferCostsToUser(fromUserWalletId, toUserId, amount, comission, locale, sourceId);
        String currencyName = currencyService.getCurrencyName(dto.getCurrencyId());
        String result = messageSource.getMessage("transfer.successful", new Object[]{dto.getNotyAmount(), currencyName, toUserNickname}, locale);
        sendNotificationsAboutTransfer(userId, dto.getNotyAmount(), currencyName, dto.getUserToId(), toUserNickname);
        return result;
    }


    private void sendNotificationsAboutTransfer(int fromUserId, String notyAmount, String currencyName, int toUserId, String toNickName) {
        log.debug("from {} to {}", fromUserId, toUserId);
        notificationService.notifyUser(fromUserId, NotificationEvent.IN_OUT, "wallets.transferTitle",
                "transfer.successful", new Object[]{notyAmount, currencyName, toNickName});
        notificationService.notifyUser(toUserId, NotificationEvent.IN_OUT, "wallets.transferTitle",
                "transfer.received", new Object[]{notyAmount, currencyName});
    }


    @Transactional(rollbackFor = Exception.class)
    public void performTransferCostsToUser(Wallet fromUserWallet, Wallet toUserWallet,
                                           BigDecimal initialAmount, BigDecimal totalAmount, BigDecimal commissionAmount,
                                           Integer sourceId, TransactionSourceType sourceType, Locale locale) {
        if (totalAmount.compareTo(fromUserWallet.getActiveBalance()) > 0) {
            throw new InvalidAmountException(messageSource.getMessage("transfer.invalidAmount", null, locale));
        }
        if (Integer.compare(fromUserWallet.getCurrencyId(), toUserWallet.getCurrencyId()) != 0) {
            throw new BalanceChangeException("ncorrect wallets");
        }

    }

    @Override
    public int getWalletIdAndBlock(Integer userId, Integer currencyId) {
        return walletDao.getWalletIdAndBlock(userId, currencyId);
    }



    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        return headers;
    }
}
