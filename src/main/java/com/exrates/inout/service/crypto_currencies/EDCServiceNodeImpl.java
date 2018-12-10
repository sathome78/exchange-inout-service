package com.exrates.inout.service.crypto_currencies;

import com.exrates.inout.dao.EDCAccountDao;
import com.exrates.inout.domain.main.EDCAccount;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.exceptions.InsufficientCostsInWalletException;
import com.exrates.inout.exceptions.InvalidAccountException;
import com.exrates.inout.exceptions.MerchantException;
import com.exrates.inout.exceptions.MerchantInternalException;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.service.EDCServiceNode;
import com.exrates.inout.service.TransactionService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Log4j2(topic = "edc_log")
@Service
public class EDCServiceNodeImpl implements EDCServiceNode {

    private final String PENDING_PAYMENT_HASH = "1fc3403096856798ab8992f73f241334a4fe98ce";
    private final BigDecimal BTS = new BigDecimal(1000L);
    private final int DEC_PLACES = 2;

    private final OkHttpClient HTTP_CLIENT = new OkHttpClient();
    private final MediaType MEDIA_TYPE = MediaType.parse("application/x-www-form-urlencoded");

    private final String ACCOUNT_PREFIX = "ex1f";
    private final String REGISTER_NEW_ACCOUNT_RPC = "{\"method\":\"register_account\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", 0, \"true\"], \"id\":%s}";
    private final String NEW_KEY_PAIR_RPC = "{\"method\": \"suggest_brain_key\", \"jsonrpc\": \"2.0\", \"params\": [], \"id\": %d}";
    private final String IMPORT_KEY = "{\"method\": \"import_key\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\",\"%s\"], \"id\": %s}";
    private final String TRANSFER_EDC = "{\"method\":\"transfer\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\", \"%s\", \"%s\", \"%s\", \"%s\", \"true\"], \"id\":%s}";
    private final Pattern pattern = Pattern.compile("\"brain_priv_key\":\"([\\w\\s]+)+\",\"wif_priv_key\":\"(\\S+)\",\"pub_key\":\"(\\S+)\"");

    private final BlockingQueue<String> rawTransactions = new LinkedBlockingQueue<>();
    private final BlockingQueue<Pair<String, String>> incomingPayments = new LinkedBlockingQueue<>();
    private final ExecutorService workers = Executors.newFixedThreadPool(2);
    private volatile boolean isRunning = true;
    private volatile boolean debugLog = true;

    @Autowired
    private CryptoCurrencyProperties ccp;
    @Autowired
    TransactionService transactionService;
    @Autowired
    EDCAccountDao edcAccountDao;

    public void changeDebugLogStatus(final boolean status) {
        debugLog = true;
    }

    private void handleRawTransactions(final String tx) {
        try {
            final String transactions = tx.substring(tx.indexOf("transactions"));
            final String[] operationses = transactions.split("operations");
            for (String str : operationses) {
                final int extensions = str.indexOf("extensions");
                if (extensions > 0) {
                    str = str.substring(0, extensions);
                    if (str.contains("\":[[0,{\"fee\"")) {
                        str = str.substring(str.indexOf("to"));
                        final String accountId = str.substring(str.indexOf("to") + 5, str.indexOf("amount") - 3);
                        final String amount = str.substring(str.lastIndexOf("amount") + 8, str.indexOf("asset_id") - 2);
                        incomingPayments.put(Pair.of(accountId, amount));
                    }
                }
            }
        } catch (InterruptedException e) {
            log.info("Method acceptTransaction InterruptedException........................................... error: ");
            log.error(e);
        } catch (Exception e) {
            log.info("Method acceptTransaction Exception........................................... error: ");
            log.error(e);
        }
    }

    @Transactional
    @Override
    public void rescanUnusedAccounts() {
        List<EDCAccount> list = edcAccountDao.getUnusedAccounts();
        for (EDCAccount account : list) {
            final String responseImportKey;
            try {
                responseImportKey = makeRpcCallFast(IMPORT_KEY, account.getAccountId(), account.getWifPrivKey(), account.getTransactionId());
                if (responseImportKey.contains("true")) {
                    String accountBalance = extractBalance(account.getAccountId(), account.getTransactionId());
                    if (Double.valueOf(accountBalance) > 0) {
                        final String responseTransfer = makeRpcCallFast(TRANSFER_EDC, account.getAccountId(), ccp.getOtherCoins().getEdc().getAccountMain(), accountBalance, "EDC", "Inner transfer", String.valueOf(account.getTransactionId()));
                        if (responseTransfer.contains("error")) {
                            throw new InterruptedException("Could not transfer money to main account!\n" + responseTransfer);
                        }
                        edcAccountDao.setAccountUsed(account.getTransactionId());
            /*
              TODO REFILL
              Не вникал для чего этот метод rescanUnusedAccounts, но удалять в любом случае нельзя
            Optional<PendingPayment> payment = paymentDao.findByInvoiceId(account.getTransactionId());
            if (payment.isPresent()) {

              paymentDao.delete(account.getTransactionId());
            }
            */
            /*
            TODO REFILL
            этот код удалить. Но сам не стал, чтобы был перед глазами при изменении метода rescanUnusedAccounts в целом

            Transaction transaction = transactionService.findById(account.getTransactionId());
            if (!transaction.isProvided()) {

              final BigDecimal targetAmount = transaction.getAmount().add(transaction.getCommissionAmount()).setScale(DEC_PLACES, ROUND_HALF_UP);
              final BigDecimal currentAmount = new BigDecimal(accountBalance).add(new BigDecimal("0.001")).setScale(DEC_PLACES, ROUND_HALF_UP);
              if (targetAmount.compareTo(currentAmount) != 0) {
                transactionService.updateTransactionAmount(transaction, currentAmount);
              }
              transactionService.provideTransaction(transaction);
            }*/
                    } else {
                        edcAccountDao.setAccountUsed(account.getTransactionId());
                    }
                }
            } catch (IOException e) {
                log.error(e);
            } catch (InterruptedException e) {
                log.error(e);
            }
        }
    }

    @PostConstruct
    public void init() {
        // cache warm
    /*

    TODO REFILL
    try {

      paymentDao.findAllByHash(PENDING_PAYMENT_HASH)
          .forEach(payment -> pendingPayments.put(payment.getAddress(), payment));
      workers.submit(() -> {  // processing json with transactions from server
        while (isRunning) {
          final String poll = rawTransactions.poll();
          if (poll != null) {
            handleRawTransactions(poll);
          }
        }
      });
      workers.submit(() -> {
        while (isRunning) { // accepting transactions
          final BiTuple<String, String> poll = incomingPayments.poll();
          if (poll != null) {
            acceptTransaction(poll);
          }
        }
      });
    } catch (Exception e) {
      LOG.info("Method init Exception........................................... error: ");
      LOG.error(e);
    }*/
    }

    @PreDestroy
    public void destroy() {
        isRunning = false;
        workers.shutdown();
    }

    @Override
    public void submitTransactionsForProcessing(String list) {
        try {
            rawTransactions.put(list);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String extractAccountId(final String account, final int invoiceId) throws IOException {
        final String GET_ACCOUNT_ID_RPC = "{\"method\": \"get_account_id\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\"], \"id\": %d}";
        final String response = makeRpcCallDelayed(GET_ACCOUNT_ID_RPC, account, invoiceId);
        final ObjectMapper mapper = new ObjectMapper();
        final Map<String, String> result = mapper.readValue(response, new TypeReference<Map<String, String>>() {
        });
        return result.get("result");
    }

    private String createAccount(final int id) throws Exception {
        log.info("Start method createAccount");
        final String accountName = (ACCOUNT_PREFIX + id + UUID.randomUUID()).toLowerCase();
        final EnumMap<KEY_TYPE, String> keys = extractKeys(makeRpcCallFast(NEW_KEY_PAIR_RPC, id)); // retrieve public and private from server
        final String response = makeRpcCallFast(REGISTER_NEW_ACCOUNT_RPC, accountName, keys.get(KEY_TYPE.PUBLIC), keys.get(KEY_TYPE.PUBLIC), ccp.getOtherCoins().getEdc().getAccountRegistrar(), ccp.getOtherCoins().getEdc().getAccountReferrer(), String.valueOf(id));
        log.info("bit_response: " + response.toString());
        if (response.contains("error")) {
            throw new Exception("Could not create new account!\n" + response);
        }
        final EDCAccount edcAccount = new EDCAccount();
        edcAccount.setTransactionId(id);
        edcAccount.setBrainPrivKey(keys.get(KEY_TYPE.BRAIN));
        edcAccount.setPubKey(keys.get(KEY_TYPE.PUBLIC));
        edcAccount.setWifPrivKey(keys.get(KEY_TYPE.PRIVATE));
        edcAccount.setAccountName(accountName);
        edcAccountDao.create(edcAccount);
        return accountName;
    }

    private EnumMap<KEY_TYPE, String> extractKeys(final String json) {
        final Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            final EnumMap<KEY_TYPE, String> result = new EnumMap<>(KEY_TYPE.class);
            result.put(KEY_TYPE.BRAIN, matcher.group(1));
            result.put(KEY_TYPE.PRIVATE, matcher.group(2));
            result.put(KEY_TYPE.PUBLIC, matcher.group(3));
            return result;
        }
        throw new RuntimeException("Invalid response from server:\n" + json);
    }

    private String extractBalance(final String accountId, final int invoiceId) throws IOException {
        final String LIST_ACCOUNT_BALANCE = "{\"method\": \"list_account_balances\", \"jsonrpc\": \"2.0\", \"params\": [\"%s\"], \"id\": %s}";
        final String response = makeRpcCallFast(LIST_ACCOUNT_BALANCE, accountId, invoiceId);
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(response).getAsJsonObject();
        JsonArray result = object.getAsJsonArray("result");
        if (result.size() != 1) {
            return new BigDecimal("0.000").toString();
        }
        BigDecimal amount = BigDecimal.valueOf(result.get(0).getAsJsonObject().get("amount").getAsLong()).divide(BTS);

        return amount.subtract(new BigDecimal("0.001")).toString();
    }

    @Override
    public void transferToMainAccount(final String accountId, final Transaction tx) throws IOException, InterruptedException {

        final EDCAccount edcAccount = edcAccountDao.findByTransactionId(tx.getId());
        final String responseImportKey = makeRpcCallFast(IMPORT_KEY, accountId, edcAccount.getWifPrivKey(), tx.getId());
        if (responseImportKey.contains("true")) {
            String accountBalance = extractBalance(accountId, tx.getId());
            final String responseTransfer = makeRpcCallFast(TRANSFER_EDC, accountId, ccp.getOtherCoins().getEdc().getAccountMain(), accountBalance, "EDC", "Inner transfer", String.valueOf(tx.getId()));
            if (responseTransfer.contains("error")) {
                throw new InterruptedException("Could not transfer money to main account!\n" + responseTransfer);
            }
        }
    }

    @Override
    public void transferFromMainAccount(final String accountName, final String amount) throws IOException {
        final String responseImportKey = makeRpcCallFast(IMPORT_KEY, ccp.getOtherCoins().getEdc().getAccountMain(), ccp.getOtherCoins().getEdc().getAccountPrivateKey(), 1);
        if (responseImportKey.contains("true")) {
            final String responseTransfer = makeRpcCallFast(TRANSFER_EDC, ccp.getOtherCoins().getEdc().getAccountMain(), accountName, amount, "EDC", "Output transfer", 1);
            if (responseTransfer.contains("error")) {
                log.error(responseTransfer);
                if (responseTransfer.contains("rec && rec->name == account_name_or_id")) {
                    throw new InvalidAccountException();
                }
                if (responseTransfer.contains("Insufficient Balance")) {
                    throw new InsufficientCostsInWalletException();
                }
                throw new MerchantException(responseTransfer);
            }
        }
    }

    private String makeRpcCallDelayed(String rpc, Object... args) throws IOException {
   /* final String rpcCall = String.format(rpc, args);
    final Request request = new Request.Builder()
        .url(rpcUrlDelayed)
        .post(RequestBody.create(MEDIA_TYPE, rpcCall))
        .build();
    return HTTP_CLIENT.newCall(request)
        .execute()
        .body()
        .string();*/
        return "";
    }

    private String makeRpcCallFast(String rpc, Object... args) throws IOException {
        final String rpcCall = String.format(rpc, args);
        final Request request = new Request.Builder()
                .url(ccp.getOtherCoins().getEdc().getBlockchainHostFast())
                .post(RequestBody.create(MEDIA_TYPE, rpcCall))
                .build();
        return HTTP_CLIENT.newCall(request)
                .execute()
                .body()
                .string();
    }

    private enum KEY_TYPE {
        BRAIN("brain_priv_key"),
        PUBLIC("pub_key"),
        PRIVATE("wif_priv_key");

        public final String type;

        KEY_TYPE(final String type) {
            this.type = type;
        }
    }

    private String getAddress() {
        final OkHttpClient client = new OkHttpClient();
        client.setReadTimeout(60, TimeUnit.SECONDS);

        final FormEncodingBuilder formBuilder = new FormEncodingBuilder();
        formBuilder.add("account", ccp.getOtherCoins().getEdc().getAccountMain());
        formBuilder.add("hook", ccp.getOtherCoins().getEdc().getHook());

        final Request request = new Request.Builder()
                .url("https://receive.edinarcoin.com/new-account/" + ccp.getOtherCoins().getEdc().getToken())
                .post(formBuilder.build())
                .build();
        final String returnResponse;

        try {
            returnResponse = client
                    .newCall(request)
                    .execute()
                    .body()
                    .string();
        } catch (IOException e) {
            throw new MerchantInternalException(e);
        }
        JsonParser parser = new JsonParser();
        JsonObject object = parser.parse(returnResponse).getAsJsonObject();

        return object.get("address").getAsString();
    }
}
