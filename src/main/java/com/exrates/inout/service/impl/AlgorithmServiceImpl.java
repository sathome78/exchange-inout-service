package com.exrates.inout.service.impl;

import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.exrates.inout.properties.CryptoCurrencyProperties;
import com.exrates.inout.properties.models.AwsProperty;
import com.exrates.inout.service.AlgorithmService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static com.yandex.money.api.utils.Numbers.bytesToHex;

@Log4j2
@Service
public class AlgorithmServiceImpl implements AlgorithmService {

    private static final int decimalPlaces = 8;

    private AwsProperty awsProperty;

    @Autowired
    public AlgorithmServiceImpl(CryptoCurrencyProperties ccp){
        this.awsProperty = ccp.getAwsServer().getAws();
    }

    //    У инстанса должна быть iam policy, на чтение aws секретов!!!!!
    public String getSecret(String сode) {
        String region = "us-east-2";

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        String secret = null;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(awsProperty.getEnv());
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException e) {
            log.error(e);
            throw e;
        } catch (InternalServiceErrorException e) {
            log.error(e);
            throw e;
        } catch (InvalidParameterException e) {
            log.error(e);
            throw e;
        } catch (InvalidRequestException e) {
            log.error(e);
            throw e;
        } catch (ResourceNotFoundException e) {
            log.error(e);
            throw e;
        }

        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        }
        else {
            secret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }

        secret = secret.substring(secret.indexOf(сode) + сode.length());
        secret = secret.substring(0, secret.indexOf("\""));
        return secret;
    }

    public String computeMD5Hash(String string) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            final byte[] digest = md5.digest(string.getBytes(StandardCharsets.UTF_8));
            return byteArrayToHexString(digest);
        } catch (NoSuchAlgorithmException ignore) {
            return null;
        }
    }

    public byte[] computeMD5Byte(String string) {
        try {
            final MessageDigest md5 = MessageDigest.getInstance("MD5");
            return md5.digest(string.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException ignore) {
            return null;
        }
    }

    public String sha1(final String string) {
        try {
            final MessageDigest sha1 = MessageDigest.getInstance("SHA1");
            byte[] digest = sha1.digest(string.getBytes(StandardCharsets.UTF_8));
            return byteArrayToHexString(digest);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String sha256(final String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(string.getBytes());
            return bytesToHex(md.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String base64Encode(final String string) {
        return Base64
                .getEncoder()
                .encodeToString(string.getBytes());
    }

    private String byteArrayToHexString(byte[] bytes) {
        final StringBuilder result = new StringBuilder();
        for (byte aByte : bytes) {
            result.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
        }
        return result.toString();
    }
}
