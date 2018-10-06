package com.exrates.inout.service.ethereum.ethTokensWrappers;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.*;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.3.1.
 */
public class BCS extends Contract implements ethTokenERC20 {
    private static final String BINARY = "606060405234156200001057600080fd5b6040516040806200155a83398101604052808051919060200180519150505b5b5b5b5b5b60008054600160a060020a03191633600160a060020a03161790555b60008054600160a060020a03168152600160208190526040909120805460ff191690911790555b5b5b5b60408051908101604052600f81527f424353686f702e696f20546f6b656e000000000000000000000000000000000060208201526002908051620000c392916020019062000175565b506040805190810160405260038082527f424353000000000000000000000000000000000000000000000000000000000060208301529080516200010c92916020019062000175565b506004805460ff80841660ff19928316179283905591909116600a0a83026005819055600160a060020a033316600090815260066020908152604080832093909355600e805485166001908117909155600c90915291902080549092161790555b50506200021f565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f10620001b857805160ff1916838001178555620001e8565b82800160010185558215620001e8579182015b82811115620001e8578251825591602001919060010190620001cb565b5b50620001f7929150620001fb565b5090565b6200021c91905b80821115620001f7576000815560010162000202565b5090565b90565b61132b806200022f6000396000f300606060405236156101645763ffffffff60e060020a60003504166306fdde038114610169578063095ea7b3146101f457806312686aae1461022a578063151eeb551461025157806318160ddd1461028457806322e63356146102a957806323b872dd146102ca578063313ce5671461030657806342966c681461032f5780634e98323c146103475780635333c5071461036857806359b0a1741461038e57806360cce8a5146103bf57806370a08231146103e457806378fc3cb3146104155780637d9f298e146104485780638da5cb5b1461046257806395d89b4114610491578063a084ee761461051c578063a1b16e5c1461053d578063a5e90eee14610570578063a9059cbb14610596578063c0956fd9146105cc578063c47af732146105f4578063da9425e21461061a578063dcdbbe371461064d578063dd62ed3e14610671578063f2fde38b146106a8578063f92c45b7146106c9578063fdff9b4d146106ee575b600080fd5b341561017457600080fd5b61017c610721565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156101b95780820151818401525b6020016101a0565b50505050905090810190601f1680156101e65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156101ff57600080fd5b610216600160a060020a03600435166024356107bf565b604051901515815260200160405180910390f35b341561023557600080fd5b61021661082c565b604051901515815260200160405180910390f35b341561025c57600080fd5b610216600160a060020a0360043516610835565b604051901515815260200160405180910390f35b341561028f57600080fd5b61029761084a565b60405190815260200160405180910390f35b34156102b457600080fd5b6102c8600160a060020a0360043516610851565b005b34156102d557600080fd5b610216600160a060020a03600435811690602435166044356108a2565b604051901515815260200160405180910390f35b341561031157600080fd5b61031961096e565b60405160ff909116815260200160405180910390f35b341561033a57600080fd5b6102c8600435610977565b005b341561035257600080fd5b6102c8600160a060020a0360043516610ab1565b005b341561037357600080fd5b6102c8600160a060020a03600435166024351515610afd565b005b341561039957600080fd5b610297600160a060020a0360043516610b4b565b60405190815260200160405180910390f35b34156103ca57600080fd5b610297610b5d565b60405190815260200160405180910390f35b34156103ef57600080fd5b610297600160a060020a0360043516610b71565b60405190815260200160405180910390f35b341561042057600080fd5b610216600160a060020a0360043516610b90565b604051901515815260200160405180910390f35b341561045357600080fd5b6102c86004351515610be7565b005b341561046d57600080fd5b610475610c1e565b604051600160a060020a03909116815260200160405180910390f35b341561049c57600080fd5b61017c610c2d565b60405160208082528190810183818151815260200191508051906020019080838360005b838110156101b95780820151818401525b6020016101a0565b50505050905090810190601f1680156101e65780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561052757600080fd5b6102c8600160a060020a0360043516610ccb565b005b341561054857600080fd5b610216600160a060020a0360043516610d14565b604051901515815260200160405180910390f35b341561057b57600080fd5b6102c8600160a060020a03600435166024351515610d29565b005b34156105a157600080fd5b610216600160a060020a0360043516602435610db4565b604051901515815260200160405180910390f35b34156105d757600080fd5b610297600435610e27565b60405190815260200160405180910390f35b34156105ff57600080fd5b6102c8600160a060020a03600435166024351515610e38565b005b341561062557600080fd5b610216600160a060020a0360043516610f39565b604051901515815260200160405180910390f35b341561065857600080fd5b6102c8600160a060020a0360043516602435610f4e565b005b341561067c57600080fd5b610297600160a060020a0360043581169060243516610f9a565b60405190815260200160405180910390f35b34156106b357600080fd5b6102c8600160a060020a0360043516610fc7565b005b34156106d457600080fd5b610297611026565b60405190815260200160405180910390f35b34156106f957600080fd5b610216600160a060020a036004351661102c565b604051901515815260200160405180910390f35b60028054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107b75780601f1061078c576101008083540402835291602001916107b7565b820191906000526020600020905b81548152906001019060200180831161079a57829003601f168201915b505050505081565b600160a060020a03338116600081815260076020908152604080832094871680845294909152808220859055909291907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a35060015b92915050565b600e5460ff1681565b600c6020526000908152604090205460ff1681565b6005545b90565b600160a060020a03331660009081526001602052604090205460ff16151561087557fe5b6008805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383161790555b5b50565b6000600160a060020a03831615156108b957600080fd5b600160a060020a03808516600090815260076020908152604080832033909416835292905220546108ea9083611041565b600160a060020a038086166000908152600760209081526040808320339094168352929052205561091c848484611058565b82600160a060020a031684600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405190815260200160405180910390a35060015b9392505050565b60045460ff1681565b600160a060020a03331660009081526001602052604090205460ff16151561099b57fe5b600160a060020a033316600090815260066020526040902054819010156109c157600080fd5b600854600160a060020a031615610a3d57600854600160a060020a0316630963b12d338360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401600060405180830381600087803b1515610a2857600080fd5b6102c65a03f11515610a3957600080fd5b5050505b33600160a060020a03811660009081526006602052604090819020805484900390556005805484900390557fcc16f5dbb4873280815c1ee09dbd06736cffcc184412cf7a71a0fdb75d397ca59190839051600160a060020a03909216825260208201526040908101905180910390a15b5b50565b600160a060020a03331660009081526001602052604090205460ff161515610ad557fe5b600160a060020a0381166000908152600b60205260409020805460ff191660011790555b5b50565b600160a060020a03331660009081526001602052604090205460ff161515610b2157fe5b600160a060020a0382166000908152600c60205260409020805460ff19168215151790555b5b5050565b600d6020526000908152604090205481565b6000600a54610b6a61084a565b0390505b90565b600160a060020a0381166000908152600660205260409020545b919050565b600e5460009060ff1615610bc05750600160a060020a0381166000908152600c602052604090205460ff16610b8b565b50600160a060020a0381166000908152600d60205260409020544211610b8b565b5b919050565b600160a060020a03331660009081526001602052604090205460ff161515610c0b57fe5b600e805460ff19168215151790555b5b50565b600054600160a060020a031681565b60038054600181600116156101000203166002900480601f0160208091040260200160405190810160405280929190818152602001828054600181600116156101000203166002900480156107b75780601f1061078c576101008083540402835291602001916107b7565b820191906000526020600020905b81548152906001019060200180831161079a57829003601f168201915b505050505081565b600160a060020a03331660009081526001602052604090205460ff161515610cef57fe5b600160a060020a0381166000908152600b60205260409020805460ff191690555b5b50565b600b6020526000908152604090205460ff1681565b60005433600160a060020a03908116911614610d4157fe5b600160a060020a03821660009081526001602052604090819020805460ff19168315151790557fff83ce179bad4fbdb0e98074011487cde624295a52d8189d92d5d8b06c914eda908390839051600160a060020a039092168252151560208201526040908101905180910390a15b5b5050565b6000600160a060020a0383161515610dcb57600080fd5b610dd6338484611058565b82600160a060020a031633600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405190815260200160405180910390a35060015b92915050565b60045460ff16600a0a81025b919050565b600160a060020a03331660009081526001602052604081205460ff161515610e5c57fe5b610e6583610b71565b600854909150600160a060020a031615610ee457600854600160a060020a0316630963b12d848360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401600060405180830381600087803b1515610ecf57600080fd5b6102c65a03f11515610ee057600080fd5b5050505b8115610efe57610ef6600a548261107d565b600a55610f0e565b610f0a600a5482611041565b600a555b600160a060020a0383166000908152600960205260409020805460ff19168315151790555b5b505050565b60096020526000908152604090205460ff1681565b600160a060020a03331660009081526001602052604090205460ff161515610f7257fe5b600160a060020a0382166000908152600d60205260409020620151808202420190555b5b5050565b600160a060020a038083166000908152600760209081526040808320938516835292905220545b92915050565b60005433600160a060020a03908116911614610fdf57fe5b610fe881611097565b600160a060020a038082166000908152600160208190526040808320805460ff199081169093179055339093168252919020805490911690555b5b50565b600a5481565b60016020526000908152604090205460ff1681565b60008183101561104d57fe5b508082035b92915050565b61106183610b90565b151561106c57600080fd5b610f338383836110f7565b5b505050565b60008282018381101561108c57fe5b8091505b5092915050565b60005433600160a060020a039081169116146110af57fe5b600054600160a060020a03828116911614156110ca57600080fd5b6000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0383161790555b5b50565b611102838383611192565b600160a060020a0382166000908152600b602052604090205460ff1615610f335781600160a060020a0316637d24a8a1848360405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401600060405180830381600087803b151561117757600080fd5b6102c65a03f1151561118857600080fd5b5050505b5b505050565b600854600160a060020a03161561121b57600854600160a060020a03166354fbed3784848460405160e060020a63ffffffff8616028152600160a060020a0393841660048201529190921660248201526044810191909152606401600060405180830381600087803b151561120657600080fd5b6102c65a03f1151561121757600080fd5b5050505b600160a060020a03831660009081526009602052604090205460ff161561124c57611248600a5482611041565b600a555b600160a060020a03821660009081526009602052604090205460ff161561127d57611279600a548261107d565b600a555b610f3383838361128e565b5b505050565b600160a060020a0383166000908152600660205260409020546112b19082611041565b600160a060020a0380851660009081526006602052604080822093909355908416815220546112e0908261107d565b600160a060020a0383166000908152600660205260409020555b5050505600a165627a7a72305820474149328d7edafd2632a1df93a432971d50262829325ad61f7e45b82a3cd3d1002900000000000000000000000000000000000000000000000000000000009896800000000000000000000000000000000000000000000000000000000000000012";

    protected BCS(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected BCS(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<BurnEventResponse> getBurnEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<BurnEventResponse> responses = new ArrayList<BurnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            BurnEventResponse typedResponse = new BurnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<BurnEventResponse> burnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Burn", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, BurnEventResponse>() {
            @Override
            public BurnEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                BurnEventResponse typedResponse = new BurnEventResponse();
                typedResponse.log = log;
                typedResponse.sender = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<ManagerSetEventResponse> getManagerSetEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("ManagerSet", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<ManagerSetEventResponse> responses = new ArrayList<ManagerSetEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ManagerSetEventResponse typedResponse = new ManagerSetEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.manager = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse.state = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ManagerSetEventResponse> managerSetEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("ManagerSet", 
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Bool>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, ManagerSetEventResponse>() {
            @Override
            public ManagerSetEventResponse call(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                ManagerSetEventResponse typedResponse = new ManagerSetEventResponse();
                typedResponse.log = log;
                typedResponse.manager = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse.state = (Boolean) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> name() {
        final Function function = new Function("name", 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                "approve", 
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> transferLocked() {
        final Function function = new Function("transferLocked",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<Boolean> transferAllowed(String param0) {
        final Function function = new Function("transferAllowed",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setValueAgent(String newAgent) {
        final Function function = new Function(
                "setValueAgent",
                Arrays.<Type>asList(new Address(newAgent)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                "transferFrom",
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _value) {
        final Function function = new Function(
                "burn",
                Arrays.<Type>asList(new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setReturnAgent(String agent) {
        final Function function = new Function(
                "setReturnAgent",
                Arrays.<Type>asList(new Address(agent)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> allowTransferFor(String holder, Boolean state) {
        final Function function = new Function(
                "allowTransferFor",
                Arrays.<Type>asList(new Address(holder),
                new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> transferLockUntil(String param0) {
        final Function function = new Function("transferLockUntil",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> getValuableTokenAmount() {
        final Function function = new Function("getValuableTokenAmount",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function("balanceOf",
                Arrays.<Type>asList(new Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> canTransfer(String holder) {
        final Function function = new Function("canTransfer",
                Arrays.<Type>asList(new Address(holder)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setLockedState(Boolean state) {
        final Function function = new Function(
                "setLockedState",
                Arrays.<Type>asList(new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function("owner",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function("symbol",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> removeReturnAgent(String agent) {
        final Function function = new Function(
                "removeReturnAgent",
                Arrays.<Type>asList(new Address(agent)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> returnAgents(String param0) {
        final Function function = new Function("returnAgents",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setManager(String manager, Boolean state) {
        final Function function = new Function(
                "setManager",
                Arrays.<Type>asList(new Address(manager),
                new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> getRealTokenAmount(BigInteger tokens) {
        final Function function = new Function("getRealTokenAmount",
                Arrays.<Type>asList(new Uint256(tokens)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> setReserved(String holder, Boolean state) {
        final Function function = new Function(
                "setReserved",
                Arrays.<Type>asList(new Address(holder),
                new Bool(state)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Boolean> reserved(String param0) {
        final Function function = new Function("reserved",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> lockTransferFor(String holder, BigInteger daysFromNow) {
        final Function function = new Function(
                "lockTransferFor",
                Arrays.<Type>asList(new Address(holder),
                new Uint256(daysFromNow)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function("allowance",
                Arrays.<Type>asList(new Address(_owner),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String _newOwner) {
        final Function function = new Function(
                "transferOwnership",
                Arrays.<Type>asList(new Address(_newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> reservedAmount() {
        final Function function = new Function("reservedAmount",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> managers(String param0) {
        final Function function = new Function("managers",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public static RemoteCall<BCS> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Uint8(_decimals)));
        return deployRemoteCall(BCS.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<BCS> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialSupply, BigInteger _decimals) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialSupply),
                new Uint8(_decimals)));
        return deployRemoteCall(BCS.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static BCS load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new BCS(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static BCS load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new BCS(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class BurnEventResponse {
        public Log log;

        public String sender;

        public BigInteger value;
    }

    public static class TransferEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String _owner;

        public String _spender;

        public BigInteger _value;
    }

    public static class ManagerSetEventResponse {
        public Log log;

        public String manager;

        public Boolean state;
    }
}
