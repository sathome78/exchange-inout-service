package com.exrates.inout.dao.impl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import com.exrates.inout.dao.CommissionDao;
import com.exrates.inout.dao.CurrencyDao;
import com.exrates.inout.dao.TransactionDao;
import com.exrates.inout.dao.UserDao;
import com.exrates.inout.dao.WalletDao;
import com.exrates.inout.domain.ExternalWalletsDto;
import com.exrates.inout.domain.dto.MyWalletsDetailedDto;
import com.exrates.inout.domain.dto.WalletsForOrderCancelDto;
import com.exrates.inout.domain.enums.ActionType;
import com.exrates.inout.domain.enums.OperationType;
import com.exrates.inout.domain.enums.TransactionSourceType;
import com.exrates.inout.domain.enums.WalletTransferStatus;
import com.exrates.inout.domain.main.CompanyWallet;
import com.exrates.inout.domain.main.Currency;
import com.exrates.inout.domain.main.Transaction;
import com.exrates.inout.domain.main.User;
import com.exrates.inout.domain.main.Wallet;
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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.exrates.inout.domain.enums.OperationType.SELL;

@Repository
//@Log4j2
public class WalletDaoImpl implements WalletDao {

   private static final Logger log = LogManager.getLogger(WalletDaoImpl.class);


    @Autowired
    private CommissionDao commissionDao;
    @Autowired
    private TransactionDao transactionDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private CurrencyDao currencyDao;
    @Autowired
    @Qualifier(value = "masterTemplate")
    private NamedParameterJdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier(value = "slaveTemplate")
    private NamedParameterJdbcTemplate slaveJdbcTemplate;

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

    private RowMapper<WalletsForOrderCancelDto> getWalletsForOrderCancelDtoMapper(OperationType operationType) {
        return (rs, i) -> {
            WalletsForOrderCancelDto result = new WalletsForOrderCancelDto();
            result.setOrderId(rs.getInt("order_id"));
            result.setOrderStatusId(rs.getInt("order_status_id"));
            BigDecimal reservedAmount = operationType == SELL ? rs.getBigDecimal("amount_base") :
                    BigDecimalProcessing.doAction(rs.getBigDecimal("amount_convert"), rs.getBigDecimal("commission_fixed_amount"),
                            ActionType.ADD);

            result.setReservedAmount(reservedAmount);
            result.setWalletId(rs.getInt("wallet_id"));
            result.setActiveBalance(rs.getBigDecimal("active_balance"));
            result.setActiveBalance(rs.getBigDecimal("reserved_balance"));
            return result;
        };
    }

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

    public BigDecimal getWalletRBalance(int walletId) {
        String sql = "SELECT reserved_balance FROM WALLET WHERE id = :walletId";
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

    public int createNewWallet(Wallet wallet) {
        String sql = "INSERT INTO WALLET (currency_id,user_id,active_balance) VALUES(:currId,:userId,:activeBalance)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        MapSqlParameterSource parameters = new MapSqlParameterSource()
                .addValue("currId", wallet.getCurrencyId())
                .addValue("userId", wallet.getUser().getId())
                .addValue("activeBalance", wallet.getActiveBalance());
        int result = jdbcTemplate.update(sql, parameters, keyHolder);
        int id = (int) keyHolder.getKey().longValue();
        if (result <= 0) {
            id = 0;
        }
        return id;
    }

    public List<MyWalletsDetailedDto> getAllWalletsForUserDetailed(String email, List<Integer> currencyIds, List<Integer> withdrawStatusIds, Locale locale) {
        String currencyFilterClause = currencyIds.isEmpty() ? "" : " AND WALLET.currency_id IN(:currencyIds)";
        final String sql =
                " SELECT wallet_id, user_id, currency_id, currency_name, currency_description, active_balance, reserved_balance, " +
                        "   SUM(amount_base+amount_convert+commission_fixed_amount) AS reserved_balance_by_orders, " +
                        "   SUM(withdraw_amount) AS reserved_balance_by_withdraw, " +
                        "   SUM(input_confirmation_amount+input_confirmation_commission) AS on_input_cofirmation, " +
                        "   SUM(input_confirmation_stage) AS input_confirmation_stage, SUM(input_count) AS input_count" +
                        " FROM " +
                        " ( " +
                        " SELECT WALLET.id AS wallet_id, WALLET.user_id AS user_id, CURRENCY.id AS currency_id, CURRENCY.name AS currency_name, CURRENCY.description AS currency_description, " +
                        "WALLET.active_balance AS active_balance, WALLET.reserved_balance AS reserved_balance,   " +
                        " IFNULL(SELL.amount_base,0) as amount_base, 0 as amount_convert, 0 AS commission_fixed_amount, " +
                        " 0 AS withdraw_amount, 0 AS withdraw_commission,  " +
                        " 0 AS input_confirmation_amount, 0 AS input_confirmation_commission, 0 AS input_confirmation_stage, 0 AS input_count  " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " LEFT JOIN CURRENCY_PAIR CP1 ON (CP1.currency1_id = WALLET.currency_id) " +
                        " LEFT JOIN EXORDERS SELL ON (SELL.operation_type_id=3) AND (SELL.user_id=USER.id) AND (SELL.currency_pair_id = CP1.id) AND (SELL.status_id = 2) " +
                        " WHERE USER.email =  :email AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id, WALLET.user_id, CURRENCY.id, CURRENCY.name, CURRENCY.description, WALLET.active_balance, " +
                        " WALLET.reserved_balance,   " +
                        " IFNULL(SOSELL.amount_base,0), 0, 0, " +
                        " 0, 0,  " +
                        " 0, 0, 0, 0  " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " LEFT JOIN CURRENCY_PAIR CP1 ON (CP1.currency1_id = WALLET.currency_id) " +
                        " LEFT JOIN STOP_ORDERS SOSELL ON (SOSELL.operation_type_id=3) AND (SOSELL.user_id=USER.id) AND (SOSELL.currency_pair_id = CP1.id) AND (SOSELL.status_id = 2) " +
                        " WHERE USER.email =  :email AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id, WALLET.user_id, CURRENCY.id, CURRENCY.name, CURRENCY.description, WALLET.active_balance, WALLET.reserved_balance,   " +
                        " 0, IFNULL(SOBUY.amount_convert,0), IFNULL(SOBUY.commission_fixed_amount,0), " +
                        " 0, 0, " +
                        " 0, 0, 0, 0 " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " LEFT JOIN CURRENCY_PAIR CP2 ON (CP2.currency2_id = WALLET.currency_id) " +
                        " LEFT JOIN STOP_ORDERS SOBUY ON (SOBUY.operation_type_id=4) AND (SOBUY.user_id=USER.id) AND (SOBUY.currency_pair_id = CP2.id) AND (SOBUY.status_id = 2) " +
                        " WHERE USER.email =  :email  AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id, WALLET.user_id, CURRENCY.id, CURRENCY.name, CURRENCY.description, WALLET.active_balance, WALLET.reserved_balance,   " +
                        " 0, IFNULL(BUY.amount_convert,0), IFNULL(BUY.commission_fixed_amount,0), " +
                        " 0, 0, " +
                        " 0, 0, 0, 0 " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " LEFT JOIN CURRENCY_PAIR CP2 ON (CP2.currency2_id = WALLET.currency_id) " +
                        " LEFT JOIN EXORDERS BUY ON (BUY.operation_type_id=4) AND (BUY.user_id=USER.id) AND (BUY.currency_pair_id = CP2.id) AND (BUY.status_id = 2) " +
                        " WHERE USER.email =  :email  AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id, WALLET.user_id, CURRENCY.id, CURRENCY.name, CURRENCY.description, WALLET.active_balance, WALLET.reserved_balance,   " +
                        " 0, 0, 0, " +
                        " IFNULL(WITHDRAW_REQUEST.amount, 0), IFNULL(WITHDRAW_REQUEST.commission, 0), " +
                        " 0, 0, 0, 0 " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " JOIN WITHDRAW_REQUEST ON WITHDRAW_REQUEST.user_id = USER.id AND WITHDRAW_REQUEST.currency_id = WALLET.currency_id AND WITHDRAW_REQUEST.status_id NOT IN (:status_id_list) " +
                        " WHERE USER.email =  :email AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id, WALLET.user_id, CURRENCY.id, CURRENCY.name, CURRENCY.description, WALLET.active_balance, WALLET.reserved_balance,   " +
                        " 0, 0, 0, " +
                        " IFNULL(TRANSFER_REQUEST.amount, 0), IFNULL(TRANSFER_REQUEST.commission, 0), " +
                        " 0, 0, 0, 0 " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " LEFT JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " JOIN TRANSFER_REQUEST ON TRANSFER_REQUEST.user_id = USER.id AND TRANSFER_REQUEST.currency_id = WALLET.currency_id AND TRANSFER_REQUEST.status_id = 4 " +
                        " WHERE USER.email =  :email AND CURRENCY.hidden != 1 " + currencyFilterClause +
                        "  " +
                        " UNION ALL " +
                        "  " +
                        " SELECT WALLET.id AS wallet_id, WALLET.user_id AS user_id, CURRENCY.id AS currency_id, CURRENCY.name AS currency_name, CURRENCY.description AS currency_description, " +
                        " WALLET.active_balance AS active_balance, WALLET.reserved_balance AS reserved_balance,   " +
                        " 0 AS amount_base, 0 AS amount_convert, 0 AS commission_fixed_amount, " +
                        " 0 AS withdraw_amount, 0 AS withdraw_commission,  " +
                        " SUM(RR.amount), 0, 0, COUNT(RR.id) " +
                        " FROM USER " +
                        " JOIN WALLET ON (WALLET.user_id = USER.id)  " +
                        " JOIN CURRENCY ON (CURRENCY.id = WALLET.currency_id) " +
                        " JOIN REFILL_REQUEST RR ON RR.user_id = USER.id AND RR.currency_id = CURRENCY.id AND RR.status_id = 6 " +
                        " WHERE USER.email =  :email  AND CURRENCY.hidden != 1" + currencyFilterClause +
                        " GROUP BY wallet_id, user_id, currency_id, currency_name,  active_balance, reserved_balance, " +
                        "          amount_base, amount_convert, commission_fixed_amount, " +
                        "          withdraw_amount, withdraw_commission " +
                        " ) W " +
                        " GROUP BY wallet_id, user_id, currency_id, currency_name, currency_description, active_balance, reserved_balance " +
                        "ORDER BY currency_name ASC ";
        final Map<String, Object> params = new HashMap<String, Object>() {{
            put("email", email);
            put("currencyIds", currencyIds);
            put("status_id_list", withdrawStatusIds);
        }};
        return slaveJdbcTemplate.query(sql, params, (rs, rowNum) -> {
            MyWalletsDetailedDto myWalletsDetailedDto = new MyWalletsDetailedDto();
            myWalletsDetailedDto.setId(rs.getInt("wallet_id"));
            myWalletsDetailedDto.setUserId(rs.getInt("user_id"));
            myWalletsDetailedDto.setCurrencyId(rs.getInt("currency_id"));
            myWalletsDetailedDto.setCurrencyName(rs.getString("currency_name"));
            myWalletsDetailedDto.setCurrencyDescription(rs.getString("currency_description"));
            myWalletsDetailedDto.setActiveBalance(BigDecimalProcessing.formatLocale(rs.getBigDecimal("active_balance"), locale, 2));
            myWalletsDetailedDto.setOnConfirmation(BigDecimalProcessing.formatLocale(rs.getBigDecimal("on_input_cofirmation"), locale, 2));
            myWalletsDetailedDto.setOnConfirmationStage(BigDecimalProcessing.formatLocale(rs.getBigDecimal("input_confirmation_stage"), locale, 0));
            myWalletsDetailedDto.setOnConfirmationCount(BigDecimalProcessing.formatLocale(rs.getBigDecimal("input_count"), locale, 0));
            myWalletsDetailedDto.setReservedBalance(BigDecimalProcessing.formatLocale(rs.getBigDecimal("reserved_balance"), locale, 2));
            myWalletsDetailedDto.setReservedByOrders(BigDecimalProcessing.formatLocale(rs.getBigDecimal("reserved_balance_by_orders"), locale, 2));
            myWalletsDetailedDto.setReservedByMerchant(BigDecimalProcessing.formatLocale(rs.getBigDecimal("reserved_balance_by_withdraw"), locale, 2));
            return myWalletsDetailedDto;
        });
    }

    @Override
    public Wallet findByUserAndCurrency(int userId, int currencyId) {
        final String sql = "SELECT WALLET.id,WALLET.currency_id,WALLET.user_id,WALLET.active_balance, WALLET.reserved_balance, CURRENCY.name as name FROM WALLET INNER JOIN CURRENCY On" +
                "  WALLET.currency_id = CURRENCY.id WHERE user_id = :userId and currency_id = :currencyId";
        final Map<String, Integer> params = new HashMap<String, Integer>() {
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

    @Override
    public Wallet findById(Integer walletId) {
        final String sql = "SELECT WALLET.id,WALLET.currency_id,WALLET.user_id,WALLET.active_balance, WALLET.reserved_balance, CURRENCY.name as name " +
                "FROM WALLET " +
                "INNER JOIN CURRENCY ON WALLET.currency_id = CURRENCY.id " +
                "WHERE WALLET.id = :id";
        final Map<String, Integer> params = Collections.singletonMap("id", walletId);
        return jdbcTemplate.queryForObject(sql, params, walletRowMapper);
    }

    @Override
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

    @Override
    public boolean update(Wallet wallet) {
        final String sql = "UPDATE WALLET SET active_balance = :activeBalance, reserved_balance = :reservedBalance WHERE id = :id";
        final Map<String, Object> params = new HashMap<String, Object>() {
            {
                put("id", wallet.getId());
                put("activeBalance", wallet.getActiveBalance());
                put("reservedBalance", wallet.getReservedBalance());
            }
        };
        return jdbcTemplate.update(sql, params) == 1;
    }


    public int getUserIdFromWallet(int walletId) {
        final String sql = "SELECT user_id FROM WALLET WHERE id = :walletId";
        Map<String, String> namedParameters = new HashMap<>();
        namedParameters.put("walletId", String.valueOf(walletId));
        try {
            return jdbcTemplate.queryForObject(sql, namedParameters, Integer.class);
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

    @Override
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
        Map<String, Object> params = new HashMap<String, Object>() {
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
        Map<String, Object> params = new HashMap<String, Object>() {
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

    @Override
    public void addToWalletBalance(Integer walletId, BigDecimal addedAmountActive, BigDecimal addedAmountReserved) {
        String sql = "UPDATE WALLET SET active_balance = active_balance + :add_active, " +
                "reserved_balance = reserved_balance + :add_reserved WHERE id = :id";
        Map<String, Number> params = new HashMap<>();
        params.put("id", walletId);
        params.put("add_active", addedAmountActive);
        params.put("add_reserved", addedAmountReserved);
        jdbcTemplate.update(sql, params);
    }

    @Override
    public List<ExternalWalletsDto> getExternalWallets() {
        String sql = "SELECT COMPANY_WALLET_EXTERNAL." +
                "currency_id, CURRENCY.name as currency_name, main_wallet_balance, reserve_wallet_balance, " +
                " cold_wallet_balance, rate_usd_additional, IFNULL(MC.merchant_id, 0) as merchant_id FROM COMPANY_WALLET_EXTERNAL\n" +
                " join CURRENCY on (COMPANY_WALLET_EXTERNAL.currency_id = CURRENCY.id AND CURRENCY.hidden = 0) " +
                "LEFT JOIN \n" +
                "(SELECT merchant_id, currency_id FROM MERCHANT_CURRENCY\n" +
                "join MERCHANT on (MERCHANT_CURRENCY.merchant_id = MERCHANT.id) \n" +
                "where process_type = 'CRYPTO') as MC on MC.currency_id = CURRENCY.id " +
                "order by currency_id;";
        return slaveJdbcTemplate.query(sql, (rs, row) -> {
            ExternalWalletsDto dto = new ExternalWalletsDto();
            dto.setCurrencyId(rs.getInt("currency_id"));
            dto.setMerchantId(rs.getInt("merchant_id"));
            dto.setCurrencyName(rs.getString("currency_name"));
            dto.setRateUsdAdditional(rs.getBigDecimal("rate_usd_additional"));
            dto.setMainWalletBalance(rs.getBigDecimal("main_wallet_balance"));
            dto.setReservedWalletBalance(rs.getBigDecimal("reserve_wallet_balance"));
            dto.setColdWalletBalance(rs.getBigDecimal("cold_wallet_balance"));
            return dto;
        });
    }
}