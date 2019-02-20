package com.exrates.inout.service.ethereum;

import com.exrates.inout.service.IMerchantService;
import com.exrates.inout.service.IRefillable;
import com.exrates.inout.service.IWithdrawable;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;

import java.util.Set;

/**
 * Created by ajet on
 */
public interface EthereumCommonService extends IMerchantService, IRefillable, IWithdrawable {

    @Override
    default Boolean createdRefillRequestRecordNeeded() {
        return false;
    }

    @Override
    default Boolean needToCreateRefillRequestRecord() {
        return false;
    }

    @Override
    default Boolean toMainAccountTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default Boolean generatingAdditionalRefillAddressAvailable() {
        return true;
    }

    @Override
    default Boolean additionalTagForWithdrawAddressIsUsed() {
        return false;
    }

    @Override
    default Boolean withdrawTransferringConfirmNeeded() {
        return false;
    }

    @Override
    default Boolean additionalFieldForRefillIsUsed() {
        return false;
    }

    @Override
    default Boolean storeSameAddressForParentAndTokens() {
        return true;
    }

    Web3j getWeb3j();

    Set<String> getAccounts();

    void saveLastBlock(String block);

    String loadLastBlock();

    String getMainAddress();

    Credentials getCredentialsMain();

    Integer minConfirmationsRefill();

    String getTransferAccAddress();

}
