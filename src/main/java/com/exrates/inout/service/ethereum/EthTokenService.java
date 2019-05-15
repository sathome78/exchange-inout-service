package com.exrates.inout.service.ethereum;


import org.web3j.protocol.core.methods.response.Transaction;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by Maks on 19.09.2017.
 */
public interface EthTokenService {

    List<String> getContractAddress();

    void tokenTransaction(Transaction transaction);

    void checkTransaction(BigInteger txBlock);

    EthTokenServiceImpl.TransferEventResponse extractData(List<String> topics, String data);

    default Integer currencyId(){
        return null;
    }

    ExConvert.Unit getUnit();

    default String getMerchantName(){
        throw new NotImplementedException();
    };
}
