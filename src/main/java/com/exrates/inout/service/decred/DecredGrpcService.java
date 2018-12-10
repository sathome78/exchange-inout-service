package com.exrates.inout.service.decred;


import com.exrates.inout.service.decred.grpc.DecredApi;

import java.util.Iterator;

public interface DecredGrpcService {

    DecredApi.NextAddressResponse getNewAddress();

    Iterator<DecredApi.GetTransactionsResponse> getTransactions(int startBlock, int endBlockHeight);

    DecredApi.BestBlockResponse getBlockInfo();
}
