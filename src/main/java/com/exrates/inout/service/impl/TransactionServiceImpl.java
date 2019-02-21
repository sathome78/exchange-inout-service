package com.exrates.inout.service.impl;

import com.exrates.inout.dao.TransactionDao;
import com.exrates.inout.domain.dto.OperationViewDto;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.TransactionType;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.exceptions.TransactionPersistException;
import com.exrates.inout.exceptions.TransactionProvidingException;
import com.exrates.inout.service.*;
import com.exrates.inout.util.BigDecimalProcessing;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.math.BigDecimal.ROUND_HALF_UP;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOG = LogManager.getLogger(TransactionServiceImpl.class);
    private static final int decimalPlaces = 8;


    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CompanyWalletService companyWalletService;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public Transaction createTransactionRequest(CreditsOperation creditsOperation) {
        final Currency currency = creditsOperation.getCurrency();
        final User user = creditsOperation.getUser();
        final String currencyName = currency.getName();

        CompanyWallet companyWallet = companyWalletService.findByCurrency(currency);
        companyWallet = companyWallet == null ? companyWalletService.create(currency) : companyWallet;

        Wallet userWallet = walletService.findByUserAndCurrency(user, currency);
        userWallet = userWallet == null ? walletService.create(user, currency) : userWallet;

        Transaction transaction = new Transaction();
        transaction.setAmount(creditsOperation.getAmount());
        transaction.setCommissionAmount(creditsOperation.getCommissionAmount());
        transaction.setCommission(creditsOperation.getCommission());
        transaction.setCompanyWallet(companyWallet);
        transaction.setUserWallet(userWallet);
        transaction.setCurrency(currency);
        transaction.setDatetime(LocalDateTime.now());
        transaction.setMerchant(creditsOperation.getMerchant());
        transaction.setOperationType(creditsOperation.getOperationType());
        transaction.setProvided(false);
        transaction.setConfirmation((currencyName).equals("BTC") ? -1 : -1);
        transaction.setSourceType(creditsOperation.getTransactionSourceType());
        transaction = transactionDao.create(transaction);
        if (transaction == null) {
            throw new TransactionPersistException("Failed to provide transaction ");
        }
        LOG.info("Transaction created:" + transaction);
        return transaction;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Transaction findById(int id) {
        return transactionDao.findById(id);
    }

    private BigDecimal calculateCommissionFromAmpunt(BigDecimal amount, BigDecimal commissionRate, int scale) {
        BigDecimal mass = BigDecimal.valueOf(100L).add(commissionRate);
        return amount.multiply(commissionRate)
                .divide(mass, scale, ROUND_HALF_UP).setScale(scale, ROUND_HALF_UP);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public void provideTransaction(Transaction transaction) {
        switch (transaction.getOperationType()) {
            case INPUT:
                walletService.depositActiveBalance(transaction.getUserWallet(), transaction.getAmount());
                companyWalletService.deposit(transaction.getCompanyWallet(), transaction.getAmount(),
                        transaction.getCommissionAmount());
                break;
            case OUTPUT:
                walletService.withdrawReservedBalance(transaction.getUserWallet(), transaction.getAmount().add(transaction.getCommissionAmount()));
                companyWalletService.withdraw(transaction.getCompanyWallet(), transaction.getAmount(),
                        transaction.getCommissionAmount());
                break;
        }
        if (!transactionDao.provide(transaction.getId())) {
            throw new TransactionProvidingException("Failed to provide transaction #" + transaction.getId());
        }
    }

    private void setTransactionMerchantAndOrder(OperationViewDto view, Transaction transaction) {
        TransactionSourceType sourceType = transaction.getSourceType();
        OperationType operationType = transaction.getOperationType();
        BigDecimal amount = transaction.getAmount();
        view.setOperationType(TransactionType.resolveFromOperationTypeAndSource(sourceType, operationType, amount));
        if (sourceType == TransactionSourceType.REFILL || sourceType == TransactionSourceType.WITHDRAW) {
            view.setMerchant(transaction.getMerchant());
        } else {
            view.setMerchant(new Merchant(0, sourceType.name(), sourceType.name()));
        }

    }


    @Override
    @Transactional
    public void setSourceId(Integer trasactionId, Integer sourceId) {
        transactionDao.setSourceId(trasactionId, sourceId);
    }

    @Override
    @Transactional
    public boolean setStatusById(Integer trasactionId, Integer statusId) {
        if(trasactionId == 0) return true;
        return transactionDao.setStatusById(trasactionId, statusId);
    }

    private List<String> convertTrListToString(List<OperationViewDto> transactions) {
        List<String> transactionsResult = new ArrayList<>();
        transactionsResult.add(getCSVTransactionsHeader());
        transactionsResult.add("\n");
        transactions.forEach(i -> {
            StringBuilder sb = new StringBuilder();
            sb.append(i.getDatetime())
                    .append(";")
                    .append(i.getOperationType())
                    .append(";")
                    .append(i.getStatus())
                    .append(";")
                    .append(i.getCurrency())
                    .append(";")
                    .append(BigDecimalProcessing.formatNonePoint(i.getAmount(), false))
                    .append(";")
                    .append(BigDecimalProcessing.formatNonePoint(i.getCommissionAmount(), false))
                    .append(";")
                    .append(i.getMerchant().getName())
                    .append(";")
                    .append(i.getOrder() != null ? i.getOrder().getId() : 0);
            transactionsResult.add(sb.toString());
            transactionsResult.add("\n");
        });
        return transactionsResult;
    }

    private String getCSVTransactionsHeader() {
        return "Date;Operation Type;Status;Currency;Amount;Comission;Merchant;Source Id";
    }

    private void setTransactionMerchant(Transaction transaction) {
        LOG.debug(transaction);
        TransactionSourceType sourceType = transaction.getSourceType();
        if (sourceType == TransactionSourceType.REFILL || sourceType == TransactionSourceType.WITHDRAW) {
            transaction.setMerchant(transaction.getMerchant());
        } else {
            transaction.setMerchant(new Merchant(0, sourceType.name(), sourceType.name()));
        }

    }

}