package com.exrates.inout.service.impl;

import me.exrates.model.enums.OperationType;
import me.exrates.service.AlgorithmService;
import me.exrates.service.CommissionService;
import me.exrates.service.CurrencyService;
import me.exrates.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.yandex.money.api.utils.Numbers.bytesToHex;
import static java.math.BigDecimal.ROUND_HALF_UP;

/**
 * @author Denis Savin (pilgrimm333@gmail.com)
 */
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    private static final int decimalPlaces = 8;
    private static final BigDecimal HUNDRED = new BigDecimal(100L).setScale(decimalPlaces, ROUND_HALF_UP);
    private static final BigDecimal SATOSHI = new BigDecimal(100_000_000L);

    @Autowired
    private CommissionService commissionService;

    @Autowired
    private UserService userService;

    @Autowired
    private CurrencyService currencyService;



    @Override
    public String computeMD5Hash(String string) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[] digest = md5.digest(string.getBytes("UTF-8"));
            return byteArrayToHexString(digest);
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException ignore) {
            return null;
        }
    }

    @Override
    public byte[] computeMD5Byte(String string) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(string.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException|NoSuchAlgorithmException ignore) {
            return null;
        }
    }

    @Override
    public String sha1(final String string) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] digest = sha1.digest(string.getBytes("UTF-8"));
            return byteArrayToHexString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String sha256(final String string) {
        try{
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());
            return bytesToHex(md.digest());
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public String base64Encode(final String string) {
        return Base64
            .getEncoder()
            .encodeToString(string.getBytes());
    }

    @Override
    public String base64Decode(final String string) {
        return new String(Base64.getDecoder().decode(string));
    }

    @Override
    public BigDecimal computeAmount(final BigDecimal amount, final BigDecimal commission, final OperationType type) {
        switch (type) {
            case INPUT:
                return amount.add(commission).setScale(decimalPlaces, ROUND_HALF_UP);
            case OUTPUT:
                return amount.subtract(commission).setScale(decimalPlaces, ROUND_HALF_UP);
            default:
                throw new IllegalArgumentException(type + " is not defined operation for this method");
        }
    }

    @Override
    public BigDecimal computeCommission(final BigDecimal amount, final OperationType type) {
        BigDecimal commission = commissionService.findCommissionByTypeAndRole(type, userService.getUserRoleFromSecurityContext()).getValue();
        return amount.multiply(commission.divide(HUNDRED).setScale(decimalPlaces, ROUND_HALF_UP)).setScale(decimalPlaces, ROUND_HALF_UP);
    }

    @Override
    public BigDecimal fromSatoshi(final String amount) {
        return new BigDecimal(amount).setScale(decimalPlaces, ROUND_HALF_UP).divide(SATOSHI).setScale(decimalPlaces, ROUND_HALF_UP);
    }

    @Override
    public BigDecimal toBigDecimal(final String value) {
        return new BigDecimal(value).setScale(decimalPlaces, ROUND_HALF_UP);
    }

    private String byteArrayToHexString(byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
