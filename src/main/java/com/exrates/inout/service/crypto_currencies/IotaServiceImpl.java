package com.exrates.inout.service.crypto_currencies;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.AddressUnusedException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestGeneratingAdditionalAddressNotAvailableException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.utils.WithdrawUtils;
import jota.IotaAPI;
import jota.model.Transfer;
import jota.utils.Checksum;
import jota.utils.IotaUnitConverter;
import jota.utils.IotaUnits;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Log4j2(topic = "iota_log")
@Service
public class IotaServiceImpl implements IotaService {

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private RefillService refillService;
    @Autowired
    private MerchantService merchantService;
    @Autowired
    private CurrencyService currencyService;
    @Autowired
    private WithdrawUtils withdrawUtils;

    private IotaAPI iotaClient;
    private static List<String> addresses = new ArrayList<>();

    private Merchant merchant;
    private Currency currency;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public Map<String, String> withdraw(WithdrawMerchantOperationDto withdrawMerchantOperationDto) {
        return new HashMap<>();
    }

    @Override
    public void processPayment(Map<String, String> params) throws RefillRequestAppropriateNotFoundException {

        BigDecimal amount = BigDecimal.valueOf(IotaUnitConverter.convertAmountTo(Long.parseLong(params.get("amount")), IotaUnits.MEGA_IOTA));

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
    }

    @Override
    @Transactional
    public Map<String, String> refill(RefillRequestCreateDto request) {
        Optional<String> oldAddress = refillService.getAddressByMerchantIdAndCurrencyIdAndUserId(merchant.getId(), currency.getId(), request.getUserId());
        if (oldAddress.isPresent()) {
            if (!refillService.existsClosedRefillRequestForAddress(oldAddress.get(), merchant.getId(), currency.getId())) {
                throw new AddressUnusedException("Can`t generate, previous address unused!");
            }
        }
        Map<String, String> mapAddress = new HashMap<>();
        String address = "";
        try {
            address = iotaClient.getNewAddress(ccp.getOtherCoins().getIota().getSeed(), 2, 0, true, 0, false).getAddresses().get(0);
            List<Transfer> transfers = new ArrayList<>();
            transfers.add(new Transfer(address, 0, ccp.getOtherCoins().getIota().getMessage(), ccp.getOtherCoins().getIota().getTag()));
            iotaClient.sendTransfer(ccp.getOtherCoins().getIota().getSeed(), 2, 9, 15, transfers, null, null, true);
            addresses.add(address);
        } catch (Exception e) {
            log.error(e);
        }
        List<RefillRequestAddressDto> addressList = refillService.findByAddressMerchantAndCurrency(address, merchant.getId(), currency.getId());
        if (!addressList.isEmpty()) {
            throw new RefillRequestGeneratingAdditionalAddressNotAvailableException("Need generete new address!");
        }

        String message = messageSource.getMessage("merchants.refill.btc",
                new Object[]{address}, request.getLocale());

        mapAddress.put("message", message);
        mapAddress.put("address", address);
        mapAddress.put("qr", mapAddress.get("address"));

        return mapAddress;
    }

    @PostConstruct
    public void init() {
        currency = currencyService.findByName("IOTA");
        merchant = merchantService.findByName("IOTA");

        addresses = refillService.findAllAddresses(merchant.getId(), currency.getId());

        if (ccp.getOtherCoins().getIota().getMode().equals("main")) {
            log.info("Iota starting...");
            try {
                iotaClient = new IotaAPI.Builder()
                        .protocol(ccp.getOtherCoins().getIota().getProtocol())
                        .host(ccp.getOtherCoins().getIota().getHost())
                        .port(ccp.getOtherCoins().getIota().getPort())
                        .build();
               /*Do not delete!1
               GetNodeInfoResponse response = iotaClient.getNodeInfo();
                System.out.println(response.toString());*/

                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        checkIncomingTransactions();
                    }
                }, 3, 120, TimeUnit.MINUTES);
            } catch (Exception e) {
                log.error(e);
            }
        } else {
            log.info("Iota test mode...");
        }
    }

    private void checkIncomingTransactions() {
        try {
            log.info("Checking IOTA transactions...");
            log.info(new Date());
            String[] stockArr = new String[addresses.size()];
            stockArr = addresses.toArray(stockArr);

            iotaClient.findTransactionObjectsByAddresses(stockArr).stream()
                    .filter(t -> {
                        try {
                            return !refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(Checksum.addChecksum(t.getAddress())
                                    , merchant.getId(), currency.getId(), t.getHash()).isPresent();
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .filter(t -> {
                        try {
                            return addresses.contains(Checksum.addChecksum(t.getAddress()));
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .filter(t -> t.getValue() > 0)
                    .filter(t -> {
                        try {
                            return iotaClient.getLatestInclusion(new String[]{t.getHash()}).getStates()[0];
                        } catch (Exception e) {
                            return false;
                        }
                    })
                    .forEach(transaction -> {
                        try {

                            String addressWithChecksum = Checksum.addChecksum(transaction.getAddress());

                            Map<String, String> mapPayment = new HashMap<>();
                            mapPayment.put("address", addressWithChecksum);
                            mapPayment.put("hash", transaction.getHash());
                            mapPayment.put("amount", String.valueOf(transaction.getValue()));

                            processPayment(mapPayment);
                        } catch (Exception e) {
                            log.error(e);
                        }
                        log.info("IOTA transaction hash-" + transaction.getHash() + ", sum-" + transaction.getValue() + " provided!");
                    });
            log.info(new Date());
        } catch (Exception e) {
            log.error(e);
        }
    }

    @Override
    public boolean isValidDestinationAddress(String address) {
        return withdrawUtils.isValidDestinationAddress(address);
    }

    @PreDestroy
    public void destroy() {
        log.debug("Destroying IOTA");
        scheduler.shutdown();
        log.debug("IOTA destroyed");
    }
}