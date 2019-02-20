package com.exrates.inout.service.decred;


import java.util.Iterator;

public interface DecredGrpcService {
    Api.NextAddressResponse getNewAddress();

    Iterator<Api.GetTransactionsResponse> getTransactions(int startBlock, int endBlockHeight);

    Api.BestBlockResponse getBlockInfo();
}
