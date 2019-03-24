package com.exrates.inout.controller.admin;

import com.exrates.inout.domain.CoreWalletDto;
import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTable;
import com.exrates.inout.exceptions.NoRequestedBeansFoundException;
import com.exrates.inout.exceptions.RefillRequestAppropriateNotFoundException;
import com.exrates.inout.service.BitcoinService;
import com.exrates.inout.service.IMerchantService;
import com.exrates.inout.service.MerchantService;
import com.exrates.inout.service.RefillService;
import com.exrates.inout.service.impl.MerchantServiceContext;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/2a8fy7b07dxe44/bitcoinWallet/{merchantName}")
public class AdminBitcoinCoinController {

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private MerchantServiceContext merchantServiceContext;

    @RequestMapping(value = "/transactions", method = RequestMethod.GET)
    @ResponseBody
    public List<BtcTransactionHistoryDto> getBtcTransactions(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).listAllTransactions();
    }

    @ResponseBody
    @RequestMapping(value = "/transactions/pagination", method = GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public DataTable<List<BtcTransactionHistoryDto>> getAllTransactionByCoinLikeBitcoin(@PathVariable String merchantName, @RequestParam Map<String, String> tableParams)  throws BitcoindException, CommunicationException {
        return getBitcoinServiceByMerchantName(merchantName).listTransactions(tableParams);
    }

    @RequestMapping(value = "/walletInfo", method = RequestMethod.GET)
    @ResponseBody
    public BtcWalletInfoDto getWalletInfo(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).getWalletInfo();
    }

    @RequestMapping(value = "/estimatedFee", method = RequestMethod.GET)
    @ResponseBody
    public String getEstimatedFee(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).getEstimatedFeeString();
    }

    @RequestMapping(value = "/actualFee", method = RequestMethod.GET)
    @ResponseBody
    public BigDecimal getActualFee(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).getActualFee();
    }

    @RequestMapping(value = "/setFee", method = RequestMethod.POST)
    @ResponseBody
    public void setFee(@PathVariable String merchantName, @RequestParam BigDecimal fee) {
        getBitcoinServiceByMerchantName(merchantName).setTxFee(fee);
    }

    @RequestMapping(value = "/isRawTxEnabled", method = RequestMethod.GET)
    @ResponseBody
    public boolean isRawTxEnabled(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).isRawTxEnabled();
    }

    @RequestMapping(value = "/unlock", method = RequestMethod.POST)
    @ResponseBody
    public void submitPassword(@PathVariable String merchantName, @RequestParam String password) {
        getBitcoinServiceByMerchantName(merchantName).submitWalletPassword(password);
    }

    @RequestMapping(value = "/sendToMany", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BtcAdminPaymentResponseDto sendToMany(@PathVariable String merchantName, @RequestBody List<BtcWalletPaymentItemDto> payments) {
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        BtcAdminPaymentResponseDto responseDto = new BtcAdminPaymentResponseDto();
        responseDto.setResults(walletService.sendToMany(payments));
        responseDto.setNewBalance(walletService.getWalletInfo().getBalance());
        return responseDto;
    }

    @RequestMapping(value = "/transaction/details", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Map<String, RefillRequestBtcInfoDto> getTransactionDetails(@PathVariable String merchantName,
                                                                      @RequestParam("currency") String currencyName,
                                                                      @RequestParam String hash,
                                                                      @RequestParam String address) {
        throw new RuntimeException("Implemented in monolit. For future.");
    }

    @RequestMapping(value = "/transaction/create", method = RequestMethod.POST)
    @ResponseBody
    public void createBtcRefillRequest(@PathVariable String merchantName, @RequestParam Map<String, String> params) throws RefillRequestAppropriateNotFoundException {
        getBitcoinServiceByMerchantName(merchantName).processPayment(params);
    }

    @RequestMapping(value = "/newAddress", method = RequestMethod.GET)
    @ResponseBody
    public String getNewAddress(@PathVariable String merchantName) {
        return getBitcoinServiceByMerchantName(merchantName).getNewAddressForAdmin();
    }

    @RequestMapping(value = "/checkPayments", method = RequestMethod.POST,
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public void checkPayments(@PathVariable String merchantName,
                              @RequestParam(required = false) String blockhash) {
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        walletService.scanForUnprocessedTransactions(blockhash);
    }

    @ResponseBody
    @RequestMapping(value = "/getSubtractFeeStatus", method = GET)
    public Boolean getSubtractFeeFromAmount(@PathVariable String merchantName) {
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        return walletService.getSubtractFeeFromAmount();
    }

    @ResponseBody
    @RequestMapping(value = "/setSubtractFee", method = POST)
    public void setSubtractFeeFromAmount(@PathVariable String merchantName,
                                         @RequestParam Boolean subtractFee) {
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        walletService.setSubtractFeeFromAmount(subtractFee);
    }

    //TODO
    @RequestMapping(value = "/prepareRawTx", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BtcAdminPreparedTxDto prepareRawTransactions(@PathVariable String merchantName,
                                                        @RequestBody List<BtcWalletPaymentItemDto> payments,
                                                        HttpServletRequest request) {
        /*long uniqueAddressesCount = payments.stream().map(BtcWalletPaymentItemDto::getAddress).distinct().count();
        if (uniqueAddressesCount != payments.size()) {
          throw new InvalidBtcPaymentDataException("Only unique addresses allowed in single payment!");
        }*/
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        HttpSession session = request.getSession();
        List<BtcPreparedTransactionDto> preparedTransactions = (List<BtcPreparedTransactionDto>) session.getAttribute("PREPARED_RAW_TXES");
        BtcAdminPreparedTxDto result;

        if (preparedTransactions != null) {
            result = walletService.updateRawTransactions(preparedTransactions);
        } else {
            result = walletService.prepareRawTransactions(payments);
        }
        final Object mutex = WebUtils.getSessionMutex(session);
        synchronized (mutex) {
            session.setAttribute("PREPARED_RAW_TXES", result.getPreparedTransactions());
        }
        return result;
    }

    //TODO
    @RequestMapping(value = "/sendRawTx", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public BtcAdminPaymentResponseDto sendRawTransactions(@PathVariable String merchantName,
                                                          HttpServletRequest request) {
        HttpSession session = request.getSession();
        final Object mutex = WebUtils.getSessionMutex(session);
        Optional<List<BtcPreparedTransactionDto>> preparedTransactionsOptional = Optional.empty();
        synchronized (mutex) {
            preparedTransactionsOptional = Optional.ofNullable(((List<BtcPreparedTransactionDto>) session.getAttribute("PREPARED_RAW_TXES")));
            session.removeAttribute("PREPARED_RAW_TXES");
        }
        if (!preparedTransactionsOptional.isPresent()) {
            throw new IllegalStateException("No prepared transactions stored in session!");
        }
        List<BtcPreparedTransactionDto> preparedTransactions = preparedTransactionsOptional.get();
        BitcoinService walletService = getBitcoinServiceByMerchantName(merchantName);
        BtcAdminPaymentResponseDto responseDto = new BtcAdminPaymentResponseDto();
        responseDto.setResults(walletService.sendRawTransactions(preparedTransactions));
        responseDto.setNewBalance(walletService.getWalletInfo().getBalance());
        return responseDto;
    }

    @RequestMapping(value = "/2a8fy7b07dxe44/bitcoinWallet/listWallets", method = GET)
    @ResponseBody
    public List<CoreWalletDto> listCoreWallets(HttpServletRequest request) {
        throw new RuntimeException("Implemented in monolit. For future.");
    }

    private BitcoinService getBitcoinServiceByMerchantName(String merchantName) {
        String serviceBeanName = merchantService.findByName(merchantName).getServiceBeanName();
        IMerchantService merchantService = merchantServiceContext.getMerchantService(serviceBeanName);
        if (merchantService == null || !(merchantService instanceof BitcoinService)) {
            throw new NoRequestedBeansFoundException(serviceBeanName);
        }
        return (BitcoinService) merchantService;
    }

}
