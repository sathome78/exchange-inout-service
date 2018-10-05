package com.exrates.inout.service.impl;

import com.exrates.inout.dao.CompanyWalletDao;
import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.exceptions.NotEnoughUserWalletMoneyException;
import com.exrates.inout.exceptions.WalletPersistException;
import com.exrates.inout.service.CompanyWalletService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static com.exrates.inout.domain.enums.ActionType.SUBTRACT;
import static com.exrates.inout.util.BigDecimalProcessing.doAction;

@Log4j2
@Service
public class CompanyWalletServiceImpl implements CompanyWalletService {

    private final CompanyWalletDao companyWalletDao;

    @Autowired
    public CompanyWalletServiceImpl(CompanyWalletDao companyWalletDao) {
        this.companyWalletDao = companyWalletDao;
    }

    public CompanyWallet create(Currency currency) {
        return companyWalletDao.create(currency);
    }

    @Transactional(readOnly = true)
    public CompanyWallet findByCurrency(Currency currency) {
        return companyWalletDao.findByCurrencyId(currency);
    }

    @Transactional(propagation = Propagation.NESTED)
    public void deposit(CompanyWallet companyWallet, BigDecimal amount, BigDecimal commissionAmount) {
        final BigDecimal newBalance = companyWallet.getBalance().add(amount);
        final BigDecimal newCommissionBalance = companyWallet.getCommissionBalance().add(commissionAmount);
        companyWallet.setBalance(newBalance);
        companyWallet.setCommissionBalance(newCommissionBalance);
        if (!companyWalletDao.update(companyWallet)) {
            throw new WalletPersistException("Failed deposit on company wallet " + companyWallet.toString());
        }
    }

    @Override
    public boolean substractCommissionBalanceById(Integer id, BigDecimal amount) {

        return companyWalletDao.substarctCommissionBalanceById(id, amount);
    }



    @Override
    @Transactional(propagation = Propagation.NESTED)
    public void withdrawReservedBalance(CompanyWallet companyWallet, BigDecimal amount) {

        BigDecimal newReservedBalance = doAction(companyWallet.getCommissionBalance(), amount, SUBTRACT);
        if (newReservedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughUserWalletMoneyException("POTENTIAL HACKING! Not enough money on Company Account for operation!" + companyWallet.toString());
        }
        companyWallet.setCommissionBalance(newReservedBalance);
        if (!companyWalletDao.update(companyWallet)) {
            throw new WalletPersistException("Failed withdraw on company wallet " + companyWallet.toString());
        }

    }

    @Transactional(propagation = Propagation.NESTED)
    public void withdraw(CompanyWallet companyWallet, BigDecimal amount, BigDecimal commissionAmount) {
        final BigDecimal newBalance = companyWallet.getBalance().subtract(amount);
        final BigDecimal newCommissionBalance = companyWallet.getCommissionBalance().add(commissionAmount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughUserWalletMoneyException("POTENTIAL HACKING! Not enough money on Company Account for operation!" + companyWallet.toString());
        }
        companyWallet.setBalance(newBalance);
        companyWallet.setCommissionBalance(newCommissionBalance);
        if (!companyWalletDao.update(companyWallet)) {
            throw new WalletPersistException("Failed withdraw on company wallet " + companyWallet.toString());
        }
    }
}
