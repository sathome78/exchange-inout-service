package com.exrates.inout.service.merchant.impl;

import com.exrates.inout.domain.dto.RefillRequestAcceptDto;
import com.exrates.inout.domain.dto.RefillRequestAddressDto;
import com.exrates.inout.domain.dto.RefillRequestCreateDto;
import com.exrates.inout.domain.dto.WithdrawMerchantOperationDto;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Merchant;
import com.exrates.inout.exceptions.AddressUnusedException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.exceptions.RefillRequestGeneratingAdditionalAddressNotAvailableException;
import com.exrates.inout.service.CurrencyService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.merchant.IotaService;
import jota.IotaAPI;
import jota.model.Transfer;
import jota.utils.Checksum;
import jota.utils.IotaUnitConverter;
import jota.utils.IotaUnits;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


@Log4j2(topic = "iota_log")
@Service
@PropertySource("classpath:/merchants/iota.properties")
public class IotaServiceImpl implements IotaService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private RefillService refillService;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private CurrencyService currencyService;

    private IotaAPI iotaClient;

    private @Value("${iota.protocol}")
    String PROTOCOL;
    private @Value("${iota.host}")
    String HOST;
    private @Value("${iota.port}")
    String PORT;
    private @Value("${iota.seed}")
    String SEED;
    private @Value("${iota.message}")
    String MESSAGE;
    private @Value("${iota.tag}")
    String TAG;
    private @Value("${iota.mode}")
    String MODE;

    private static List<String> ADDRESSES = new ArrayList<>();

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
        if (oldAddress.isPresent()){
            if (!refillService.existsClosedRefillRequestForAddress(oldAddress.get(), merchant.getId(), currency.getId())) {
                throw new AddressUnusedException("Can`t generate, previous address unused!");
            }
        }

        Map<String, String> mapAddress = new HashMap<>();
        String address = "";
        try {
            address = iotaClient.getNewAddress(SEED, 2, 0, true, 0, false).getAddresses().get(0);
            List<Transfer> transfers = new ArrayList<>();
            transfers.add(new jota.model.Transfer(address, 0, MESSAGE, TAG));
            iotaClient.sendTransfer(SEED, 2, 9, 15, transfers, null, null, true);
            ADDRESSES.add(address);

        } catch (Exception e) {
            log.error(e);
        }

        List<RefillRequestAddressDto> addressList = refillService.findByAddressMerchantAndCurrency(address, merchant.getId(), currency.getId());
        if (!addressList.isEmpty()){
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
    public void init(){

        currency = currencyService.findByName("IOTA");
        merchant = merchantService.findByName("IOTA");

        ADDRESSES = refillService.findAllAddresses(merchant.getId(), currency.getId());

        if (MODE.equals("main")){
            log.info("Iota starting...");
            try {
                iotaClient = new IotaAPI.Builder()
                        .protocol(PROTOCOL)
                        .host(HOST)
                        .port(PORT)
                        .build();
               /*Do not delete!1
               GetNodeInfoResponse response = iotaClient.getNodeInfo();
                System.out.println(response.toString());*/

                scheduler.scheduleAtFixedRate(new Runnable() {
                    public void run() {
                        checkIncomingTransactions();
                    }
                }, 3, 120, TimeUnit.MINUTES);
            }catch (Exception e){
                log.error(e);
            }
        }else {
            log.info("Iota test mode...");
        }
    }

    private void checkIncomingTransactions(){
        try {
            log.info("Checking IOTA transactions...");
            log.info(new java.util.Date());
            String[] stockArr = new String[ADDRESSES.size()];
            stockArr = ADDRESSES.toArray(stockArr);

            iotaClient.findTransactionObjectsByAddresses(stockArr).stream()
                    .filter(t -> {
                        try {
                            return !refillService.getRequestIdByAddressAndMerchantIdAndCurrencyIdAndHash(Checksum.addChecksum(t.getAddress())
                                    ,merchant.getId(),currency.getId(),t.getHash()).isPresent();
                        }catch (Exception e){
                            return false;
                        }
                    })
                    .filter(t -> {
                        try {
                            return ADDRESSES.contains(Checksum.addChecksum(t.getAddress()));
                        }catch (Exception e){
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
                        }catch (Exception e){
                            log.error(e);
                        }

                        log.info("IOTA transaction hash-" + transaction.getHash() + ", sum-" + transaction.getValue() + " provided!");
            });

            log.info(new java.util.Date());

        } catch (Exception e) {
            log.error(e);
        }
    }

    @PreDestroy
    public void destroy() {
        log.debug("Destroying IOTA");
        scheduler.shutdown();
        log.debug("IOTA destroyed");
    }
}
