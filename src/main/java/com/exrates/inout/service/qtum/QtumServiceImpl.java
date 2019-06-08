package com.exrates.inout.service.qtum;

import com.exrates.inout.dao.MerchantSpecParamsDao;
import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.domain.other.ProfileData;
import com.exrates.inout.domain.qtum.QtumTransaction;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.OtherQtumProperty;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.GtagService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.util.WithdrawUtils;
import lombok.Synchronized;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

//@Log4j2(topic = "qtum_log")
@Service("qtumServiceImpl")
public class QtumServiceImpl implements QtumService {

    private static final Logger log = LogManager.getLogger("qtum_log");

    private final static String QTUM_MERCHANT_NAME = "Qtum";
    private final static String QTUM_CURRENCY_NAME = "QTUM";

    @Autowired
    private QtumNodeService qtumNodeService;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private MerchantSpecParamsDao specParamsDao;
    @Autowired
    private RefillService refillService;
    @Autowired
    private WithdrawUtils withdrawUtils;
    @Autowired
    private GtagService gtagService;

    private Merchant merchant;
    private Currency currency;

    private final String qtumSpecParamName = "LastRecievedBlock";

    private ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private Integer minConfirmations;
    private BigDecimal minTransferAmount;
    private String mainAddressForTransfer;
    private String endpoint;

    public QtumServiceImpl(CryptoCurrencyProperties cryptoCurrencyProperties){
        OtherQtumProperty qtumProperty = cryptoCurrencyProperties.getOtherCoins().getQtum();
        this.minConfirmations = qtumProperty.getMinConfirmations();
        this.minTransferAmount = qtumProperty.getMinTransferAmount();
        this.mainAddressForTransfer = qtumProperty.getMainAddressForTransfer();
        this.endpoint = qtumProperty.getEndpoint();
    }

    @PostConstruct
    private void init() {
        merchant = merchantService.findByName(QTUM_MERCHANT_NAME);
        currency = currencyService.findByName(QTUM_CURRENCY_NAME);


        scheduler.scheduleAtFixedRate(() -> {
            try {
                scanBlocks();
            } catch (Exception e) {
                log.error(e);
            }

        }, 5L, 25L, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                checkBalanceAndTransfer();
            } catch (Exception e) {
                log.error(e);
            }

        }, 90L, 120L, TimeUnit.MINUTES);

        scheduler.scheduleAtFixedRate(() -> {
            try {
                backupWallet();
            } catch (Exception e) {
                log.error(e);
            }

        }, 1L, 12L, TimeUnit.HOURS);
    }

    @Override
    public Map<String, String> refill(RefillRequestCreateDto request) {
        String address = qtumNodeService.getNewAddress();

        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[]{address}, request.getLocale());
        return new HashMap<String, String>() {{
            put("message", message);
            put("address", address);
            put("qr", address);
        }};
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        final BigDecimal amount = new BigDecimal(params.get("amount"));

        RefillRequestAcceptDto requestAcceptDto = RefillRequestAcceptDto.builder()
                .address(params.get("address"))
                .merchantId(merchant.getId())
                .currencyId(currency.getId())
                .amount(amount)
                .merchantTransactionId(params.get("hash"))
                .build();

        Integer requestId = refillService.createRefillRequestByFact(requestAcceptDto);
        requestAcceptDto.setRequestId(requestId);

        refillService.autoAcceptRefillRequest(requestAcceptDto);

        final String username = refillService.getUsernameByRequestId(requestId);

        log.debug("Process of sending data to Google Analytics...");
        gtagService.sendGtagEvents(amount.toString(), currency.getName(), username);
    }

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) throws Exception {
        return new HashMap<>();
    }

    @Synchronized
    private void scanBlocks() {

        log.debug("Start scanning blocks Qtum");
        ProfileData profileData = new ProfileData(500);

        final int lastReceivedBlock = Integer.parseInt(specParamsDao.getByMerchantNameAndParamName(merchant.getName(),
                qtumSpecParamName).getParamValue());
        Set<String> addresses = refillService.findAllAddresses(merchant.getId(), currency.getId()).stream().distinct().collect(Collectors.toSet());

        String blockNumberHash = qtumNodeService.getBlockHash(lastReceivedBlock);

        List<QtumTransaction> transactions = qtumNodeService.listSinceBlock(blockNumberHash).get().getTransactions();
        log.info("qtum transactions: " + transactions.toString());
        transactions.stream()
                .filter(t -> addresses.contains(t.getAddress()))
                .filter(t -> !refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(t.getAddress(), merchant.getId(), currency.getId(), t.getTxid()).isPresent())
                .filter(t -> transactions.stream().filter(tInner -> tInner.getTxid().equals(t.getTxid())).count() < 2)
                .filter(t -> t.getCategory().equals("receive"))
                .filter(t -> t.getConfirmations() >= minConfirmations)
                .filter(t -> t.getWalletconflicts().size() == 0)
                .filter(t -> t.isTrusted())
                .filter(t -> t.getAmount() > 0)
                .filter(t -> t.getVout() < 10)
                .forEach(t -> {
                            try {
                                log.info("before qtum transfer " + t.toString());
                                Map<String, String> mapPayment = new HashMap<>();
                                mapPayment.put("address", t.getAddress());
                                mapPayment.put("hash", t.getTxid());
                                mapPayment.put("amount", String.valueOf(t.getAmount()));
                                processPayment(mapPayment);
                                log.info("after qtum transfer");
                                specParamsDao.updateParam(merchant.getName(), qtumSpecParamName, String.valueOf(qtumNodeService.getBlock(t.getBlockhash()).getHeight()));
                            } catch (Exception e) {
                                log.error(e);
                            }
                        }
                );
        profileData.setTime1();
        log.debug("Profile results: " + profileData);
    }

    private void checkBalanceAndTransfer() {
        log.debug("Start checking balance");
        ProfileData profileData = new ProfileData(500);

        qtumNodeService.setWalletPassphrase();

        BigDecimal balance = qtumNodeService.getBalance();
        if (balance.compareTo(minTransferAmount) > 0) {
            qtumNodeService.transfer(mainAddressForTransfer, balance.subtract(minTransferAmount));
        }
        profileData.setTime1();
        log.debug("Profile results: " + profileData);
    }

    private void backupWallet() {
        log.debug("Start backup wallet");
        ProfileData profileData = new ProfileData(500);

        qtumNodeService.setWalletPassphrase();

        qtumNodeService.backupWallet();

        profileData.setTime1();
        log.debug("Profile results: " + profileData);
    }

    @Override
    public boolean isValidDestinationAddress(String address) {

        return withdrawUtils.isValidDestinationAddress(address);
    }

    @PreDestroy
    public void shutdown() {
        scheduler.shutdown();
    }
}
