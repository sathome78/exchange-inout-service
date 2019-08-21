package com.exrates.inout.service.btcCore;

import com.exrates.inout.domain.dto.*;
import com.exrates.inout.domain.dto.datatable.DataTableParams;
import com.exrates.inout.domain.main.PagingData;
import com.exrates.inout.properties.models.BitcoinNode;
import com.neemre.btcdcli4j.core.BitcoindException;
import com.neemre.btcdcli4j.core.CommunicationException;
import reactor.core.publisher.Flux;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Created by OLEG on 14.03.2017.
 */
public interface CoreWalletService {

  void initCoreClient(BitcoinNode node, boolean supportSubtractFee, boolean supportReferenceLine, boolean useSendManyForWithdraw);

  void initBtcdDaemon(boolean zmqEnabled);

  String getNewAddress(String walletPassword);

  void backupWallet(String backupFolder);

  void shutdown();

  Optional<BtcTransactionDto> handleTransactionConflicts(String txId);

  BtcTransactionDto getTransaction(String txId);

  BtcWalletInfoDto getWalletInfo();

  List<TxReceivedByAddressFlatDto> listReceivedByAddress(Integer minConfirmations);

  List<BtcTransactionHistoryDto> listAllTransactions();

  List<BtcPaymentFlatDto> listSinceBlockEx(@Nullable String blockHash, Integer merchantId, Integer currencyId);

  List<BtcPaymentFlatDto> listSinceBlock(String blockHash, Integer merchantId, Integer currencyId);

  BigDecimal estimateFee(int blockCount);

  BigDecimal getActualFee();

  void setTxFee(BigDecimal fee);

  void submitWalletPassword(String password);

  String sendToAddressAuto(String address, BigDecimal amount, String walletPassword);

  BtcPaymentResultDto sendToMany(Map<String, BigDecimal> payments, boolean subtractFeeFromAmount);

  Flux<BtcBlockDto> blockFlux();

  Flux<BtcTransactionDto> walletFlux();

  Flux<BtcTransactionDto> instantSendFlux();

  BtcPreparedTransactionDto prepareRawTransaction(Map<String, BigDecimal> payments);

  BtcPreparedTransactionDto prepareRawTransaction(Map<String, BigDecimal> payments, @Nullable String oldTxHex);

  BtcPaymentResultDto signAndSendRawTransaction(String hex);

  String getTxIdByHex(String hex);

  String getLastBlockHash();

  BtcBlockDto getBlockByHash(String blockHash);

  long getBlocksCount() throws BitcoindException, CommunicationException;

  Long getLastBlockTime() throws BitcoindException, CommunicationException;

  List<BtcTransactionHistoryDto> listTransaction(int page);

  PagingData<List<BtcTransactionHistoryDto>> listTransaction(DataTableParams dataTableParams);

  List<BtcTransactionHistoryDto> getTransactionsByPage(int page, int transactionsPerPage) throws BitcoindException, CommunicationException;

  List<BtcTransactionHistoryDto> getTransactionsForPagination(int start, int length) throws BitcoindException, CommunicationException;

  List<BtcTransactionHistoryDto> findTransactions(String value) throws BitcoindException, CommunicationException;
}
