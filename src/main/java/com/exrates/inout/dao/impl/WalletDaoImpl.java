package com.exrates.inout.dao.impl;

import com.exrates.inout.dao.TransactionDao;
import com.exrates.inout.dao.UserDao;
import com.exrates.inout.dao.WalletDao;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.*;
import com.exrates.inout.domain.other.WalletOperationData;
import com.exrates.inout.util.BigDecimalProcessing;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Repository
@Log4j2
public class WalletDaoImpl implements WalletDao {

    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;

    protected final RowMapper<Wallet> walletRowMapper = (resultSet, i) -> {

        final Wallet userWallet = new Wallet();
        userWallet.setId(resultSet.getInt("id"));
        userWallet.setName(resultSet.getString("name"));
        userWallet.setCurrencyId(resultSet.getInt("currency_id"));
        userWallet.setUser(userDao.getUserById(resultSet.getInt("user_id")));
        userWallet.setActiveBalance(resultSet.getBigDecimal("active_balance"));
        userWallet.setReservedBalance(resultSet.getBigDecimal("reserved_balance"));

        return userWallet;
    };

    public BigDecimal getWalletABalance(int walletId) {
        if (walletId == 0) {
            return new BigDecimal(0);
        }
        String sql = "SELECT active_balance FROM WALLET WHERE id = :walletId";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("walletId", String.valueOf(walletId));
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, BigDecimal.class);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public int getWalletId(int userId, int currencyId) {
        String sql = "SELECT id FROM WALLET WHERE user_id = :userId AND currency_id = :currencyId";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("userId", String.valueOf(userId));
        namedParameters.put("currencyId", String.valueOf(currencyId));
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
    public int getWalletIdAndBlock(Integer userId, Integer currencyId) {
        String sql = "SELECT id FROM WALLET WHERE user_id = :userId AND currency_id = :currencyId FOR UPDATE";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("userId", String.valueOf(userId));
        namedParameters.put("currencyId", String.valueOf(currencyId));
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    public Wallet findByUserAndCurrency(int userId, int currencyId) {
        final String sql = "SELECT WALLET.id,WALLET.currency_id,WALLET.user_id,WALLET.active_balance, WALLET.reserved_balance, CURRENCY.name as name FROM WALLET INNER JOIN CURRENCY On" +
                "  WALLET.currency_id = CURRENCY.id WHERE user_id = :userId and currency_id = :currencyId";
        final Map<String, Integer> params = new HashMap<>() {
            {
                put("userId", userId);
                put("currencyId", currencyId);
            }
        };
        try {
            return jdbcTemplate.queryForObject(sql, params, walletRowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Wallet findById(Integer walletId) {
        final String sql = "SELECT WALLET.id,WALLET.currency_id,WALLET.user_id,WALLET.active_balance, WALLET.reserved_balance, CURRENCY.name as name " +
                "FROM WALLET " +
                "INNER JOIN CURRENCY ON WALLET.currency_id = CURRENCY.id " +
                "WHERE WALLET.id = :id";
        final Map<String, Integer> params = Collections.singletonMap("id", walletId);
        return jdbcTemplate.queryForObject(sql, params, walletRowMapper);
    }

    public Wallet createWallet(User user, int currencyId) {
        final String sql = "INSERT INTO WALLET (currency_id,user_id) VALUES(:currId,:userId)";
        final KeyHolder keyHolder = new GeneratedKeyHolder();
        final MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("currId", currencyId)
                .addValue("userId", user.getId());
        if (jdbcTemplate.update(sql, parameters, keyHolder) > 0) {
            Wallet wallet = new Wallet();
            wallet.setActiveBalance(BigDecimal.valueOf(0));
            wallet.setReservedBalance(BigDecimal.valueOf(0));
            wallet.setId(keyHolder.getKey().intValue());
            wallet.setCurrencyId(currencyId);
            return wallet;
        }
        return null;
    }

    public boolean update(Wallet wallet) {
        final String sql = "UPDATE WALLET SET active_balance = :activeBalance, reserved_balance = :reservedBalance WHERE id = :id";
        final Map<String, Object> params = new HashMap<>() {
            {
                put("id", wallet.getId());
                put("activeBalance", wallet.getActiveBalance());
                put("reservedBalance", wallet.getReservedBalance());
            }
        };
        return jdbcTemplate.update(sql, params) == 1;
    }


    public WalletTransferStatus walletInnerTransfer(int walletId, BigDecimal amount, TransactionSourceType sourceType, int sourceId, String description) {
        String sql = "SELECT WALLET.id AS wallet_id, WALLET.currency_id, WALLET.active_balance, WALLET.reserved_balance" +
                "  FROM WALLET " +
                "  WHERE WALLET.id = :walletId " +
                "  FOR UPDATE"; //FOR UPDATE Important!
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("walletId", String.valueOf(walletId));
        Wallet wallet = null;
        try {
            wallet = jdbcTemplate.queryForObject(sql, namedParameters, new RowMapper<Wallet>() {
                @Override
                public Wallet mapRow(ResultSet rs, int rowNum) throws SQLException {
                    Wallet result = new Wallet();
                    result.setId(rs.getInt("wallet_id"));
                    result.setCurrencyId(rs.getInt("currency_id"));
                    result.setActiveBalance(rs.getBigDecimal("active_balance"));
                    result.setReservedBalance(rs.getBigDecimal("reserved_balance"));
                    return result;
                }
            });
        } catch (EmptyResultDataAccessException e) {
            return WalletTransferStatus.WALLET_NOT_FOUND;
        }
        /**/
        BigDecimal newActiveBalance = BigDecimalProcessing.doAction(wallet.getActiveBalance(), amount, ActionType.ADD);
        BigDecimal newReservedBalance = BigDecimalProcessing.doAction(wallet.getReservedBalance(), amount, ActionType.SUBTRACT);
        if (newActiveBalance.compareTo(BigDecimal.ZERO) == -1 || newReservedBalance.compareTo(BigDecimal.ZERO) == -1) {
            log.error(String.format("Negative balance: active %s, reserved %s ",
                    BigDecimalProcessing.formatNonePoint(newActiveBalance, false),
                    BigDecimalProcessing.formatNonePoint(newReservedBalance, false)));
            return WalletTransferStatus.CAUSED_NEGATIVE_BALANCE;
        }
        /**/
        sql = "UPDATE WALLET SET active_balance = :active_balance, reserved_balance = :reserved_balance WHERE id =:walletId";
        Map<String, Object> params = new HashMap<>() {
            {
                put("active_balance", newActiveBalance);
                put("reserved_balance", newReservedBalance);
                put("walletId", String.valueOf(walletId));
            }
        };
        if (jdbcTemplate.update(sql, params) <= 0) {
            return WalletTransferStatus.WALLET_UPDATE_ERROR;
        }
        /**/
        Transaction transaction = new Transaction();
        transaction.setOperationType(OperationType.WALLET_INNER_TRANSFER);
        transaction.setUserWallet(wallet);
        transaction.setCompanyWallet(null);
        transaction.setAmount(amount);
        transaction.setCommissionAmount(BigDecimal.ZERO);
        transaction.setCommission(null);
        Currency currency = new Currency();
        currency.setId(wallet.getCurrencyId());
        transaction.setCurrency(currency);
        transaction.setProvided(true);
        transaction.setActiveBalanceBefore(wallet.getActiveBalance());
        transaction.setReservedBalanceBefore(wallet.getReservedBalance());
        transaction.setCompanyBalanceBefore(null);
        transaction.setCompanyCommissionBalanceBefore(null);
        transaction.setSourceType(sourceType);
        transaction.setSourceId(sourceId);
        transaction.setDescription(description);
        try {
            transactionDao.create(transaction);
        } catch (Exception e) {
            log.error(e);
            return WalletTransferStatus.TRANSACTION_CREATION_ERROR;
        }
        /**/
        return WalletTransferStatus.SUCCESS;
    }


    public WalletTransferStatus walletBalanceChange(WalletOperationData walletOperationData) {
        BigDecimal amount = walletOperationData.getAmount();
        if (walletOperationData.getOperationType() == OperationType.OUTPUT) {
            amount = amount.negate();
        }
        /**/
        CompanyWallet companyWallet = new CompanyWallet();
        String sql = "SELECT WALLET.id AS wallet_id, WALLET.currency_id, WALLET.active_balance, WALLET.reserved_balance, " +
                "  COMPANY_WALLET.id AS company_wallet_id, COMPANY_WALLET.currency_id, COMPANY_WALLET.balance, COMPANY_WALLET.commission_balance " +
                "  FROM WALLET " +
                "  JOIN COMPANY_WALLET ON (COMPANY_WALLET.currency_id = WALLET.currency_id) " +
                "  WHERE WALLET.id = :walletId " +
                "  FOR UPDATE"; //FOR UPDATE Important!
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("walletId", String.valueOf(walletOperationData.getWalletId()));
        Wallet wallet = null;
        try {
            wallet = jdbcTemplate.queryForObject(sql, namedParameters, (rs, rowNum) -> {
                Wallet result = new Wallet();
                result.setId(rs.getInt("wallet_id"));
                result.setCurrencyId(rs.getInt("currency_id"));
                result.setActiveBalance(rs.getBigDecimal("active_balance"));
                result.setReservedBalance(rs.getBigDecimal("reserved_balance"));
                /**/
                companyWallet.setId(rs.getInt("company_wallet_id"));
                Currency currency = new Currency();
                currency.setId(rs.getInt("currency_id"));
                companyWallet.setCurrency(currency);
                companyWallet.setBalance(rs.getBigDecimal("balance"));
                companyWallet.setCommissionBalance(rs.getBigDecimal("commission_balance"));
                return result;
            });
        } catch (EmptyResultDataAccessException e) {
            log.error(ExceptionUtils.getStackTrace(e));
            return WalletTransferStatus.WALLET_NOT_FOUND;
        }
        /**/
        BigDecimal newActiveBalance;
        BigDecimal newReservedBalance;
        if (walletOperationData.getBalanceType() == WalletOperationData.BalanceType.ACTIVE) {
            newActiveBalance = BigDecimalProcessing.doAction(wallet.getActiveBalance(), amount, ActionType.ADD);
            newReservedBalance = wallet.getReservedBalance();
        } else {
            newActiveBalance = wallet.getActiveBalance();
            newReservedBalance = BigDecimalProcessing.doAction(wallet.getReservedBalance(), amount, ActionType.ADD);
        }
        if (newActiveBalance.compareTo(BigDecimal.ZERO) == -1 || newReservedBalance.compareTo(BigDecimal.ZERO) == -1) {
            log.error(String.format("Negative balance: active %s, reserved %s ",
                    BigDecimalProcessing.formatNonePoint(newActiveBalance, false),
                    BigDecimalProcessing.formatNonePoint(newReservedBalance, false)));
            return WalletTransferStatus.CAUSED_NEGATIVE_BALANCE;
        }
        /**/
        sql = "UPDATE WALLET SET active_balance = :active_balance, reserved_balance = :reserved_balance WHERE id =:walletId";
        Map<String, Object> params = new HashMap<>() {
            {
                put("active_balance", newActiveBalance);
                put("reserved_balance", newReservedBalance);
                put("walletId", String.valueOf(walletOperationData.getWalletId()));
            }
        };
        if (jdbcTemplate.update(sql, params) <= 0) {
            return WalletTransferStatus.WALLET_UPDATE_ERROR;
        }
        /**/
        if (walletOperationData.getTransaction() == null) {
            Transaction transaction = new Transaction();
            transaction.setOperationType(walletOperationData.getOperationType());
            transaction.setUserWallet(wallet);
            transaction.setCompanyWallet(companyWallet);
            transaction.setAmount(walletOperationData.getAmount());
            transaction.setCommissionAmount(walletOperationData.getCommissionAmount());
            transaction.setCommission(walletOperationData.getCommission());
            transaction.setCurrency(companyWallet.getCurrency());
            transaction.setProvided(true);
            transaction.setActiveBalanceBefore(wallet.getActiveBalance());
            transaction.setReservedBalanceBefore(wallet.getReservedBalance());
            transaction.setCompanyBalanceBefore(companyWallet.getBalance());
            transaction.setCompanyCommissionBalanceBefore(companyWallet.getCommissionBalance());
            transaction.setSourceType(walletOperationData.getSourceType());
            transaction.setSourceId(walletOperationData.getSourceId());
            transaction.setDescription(walletOperationData.getDescription());
            try {
                transactionDao.create(transaction);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                return WalletTransferStatus.TRANSACTION_CREATION_ERROR;
            }
            walletOperationData.setTransaction(transaction);
        } else {
            Transaction transaction = walletOperationData.getTransaction();
            transaction.setProvided(true);
            transaction.setUserWallet(wallet);
            transaction.setCompanyWallet(companyWallet);
            transaction.setActiveBalanceBefore(wallet.getActiveBalance());
            transaction.setReservedBalanceBefore(wallet.getReservedBalance());
            transaction.setCompanyBalanceBefore(companyWallet.getBalance());
            transaction.setCompanyCommissionBalanceBefore(companyWallet.getCommissionBalance());
            transaction.setSourceType(walletOperationData.getSourceType());
            transaction.setSourceId(walletOperationData.getSourceId());
            try {
                transactionDao.updateForProvided(transaction);
            } catch (Exception e) {
                log.error(ExceptionUtils.getStackTrace(e));
                return WalletTransferStatus.TRANSACTION_UPDATE_ERROR;
            }
            walletOperationData.setTransaction(transaction);
        }
        /**/
        return WalletTransferStatus.SUCCESS;
    }

}