package com.exrates.inout.service.impl;

import com.exrates.inout.dao.TransactionDao;
import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.CreditsOperation;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
import com.exrates.inout.exceptions.TransactionPersistException;
import com.exrates.inout.exceptions.TransactionProvidingException;
import com.exrates.inout.service.CompanyWalletService;
import com.exrates.inout.service.TransactionService;
import com.exrates.inout.service.WalletService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger LOG = LogManager.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private WalletService walletService;
    @Autowired
    private CompanyWalletService companyWalletService;

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

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public Transaction findById(int id) {
        return transactionDao.findById(id);
    }

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

    @Transactional
    public void setSourceId(Integer trasactionId, Integer sourceId) {
        transactionDao.setSourceId(trasactionId, sourceId);
    }

    @Override
    public boolean setStatusById(int transactionId, int status) {
        return false;
    }
}
