package com.exrates.inout.service.ethereum.ethTokensWrappers;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
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
 * <p>Generated with web3j version 3.4.0.
 */
public class uDOO extends Contract implements ethTokenNotERC20{
    private static final String BINARY = "0x6080604052600436106101955763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde03811461019a5780630764ebd914610224578063095ea7b31461024c57806316ada5471461028457806318160ddd146102ab57806323b872dd146102c0578063313ce567146102ea57806335b7588f1461031557806340c10f191461032a5780635a3b7e421461034e578063661884631461036357806369bb4dc21461038757806370a082311461039c578063757f7302146103bd57806378c37a45146103e357806379ba5097146103f85780637e5cd5c11461040d5780638da5cb5b1461042257806395d89b41146104535780639c7beb8a14610468578063a9059cbb14610489578063abe2a18d146104ad578063b22a7bfa146104ce578063cd8f8b3c14610531578063cf011b2614610557578063d4ee1d9014610578578063d5abeb011461058d578063d73dd623146105a2578063dace4557146105c6578063dd62ed3e146105de578063f2fde38b14610605578063f7aad9ed14610626575b600080fd5b3480156101a657600080fd5b506101af61067b565b6040805160208082528351818301528351919283929083019185019080838360005b838110156101e95781810151838201526020016101d1565b50505050905090810190601f1680156102165780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34801561023057600080fd5b5061024a600160a060020a03600435166024351515610709565b005b34801561025857600080fd5b50610270600160a060020a036004351660243561074b565b604080519115158252519081900360200190f35b34801561029057600080fd5b506102996107b1565b60408051918252519081900360200190f35b3480156102b757600080fd5b506102996107b7565b3480156102cc57600080fd5b50610270600160a060020a03600435811690602435166044356107bd565b3480156102f657600080fd5b506102ff6107d2565b6040805160ff9092168252519081900360200190f35b34801561032157600080fd5b506102706107f3565b34801561033657600080fd5b5061024a600160a060020a03600435166024356107fc565b34801561035a57600080fd5b506101af6108f2565b34801561036f57600080fd5b50610270600160a060020a036004351660243561094d565b34801561039357600080fd5b50610299610a3d565b3480156103a857600080fd5b50610299600160a060020a0360043516610a5b565b3480156103c957600080fd5b5061024a600160a060020a03600435166024351515610a76565b3480156103ef57600080fd5b50610299610ab8565b34801561040457600080fd5b5061024a610abe565b34801561041957600080fd5b5061024a610b03565b34801561042e57600080fd5b50610437610b2d565b60408051600160a060020a039092168252519081900360200190f35b34801561045f57600080fd5b506101af610b3c565b34801561047457600080fd5b50610270600160a060020a0360043516610b97565b34801561049557600080fd5b50610270600160a060020a0360043516602435610bac565b3480156104b957600080fd5b50610270600160a060020a0360043516610bbf565b3480156104da57600080fd5b5060408051602060046024803582810135848102808701860190975280865261024a968435600160a060020a031696369660449591949091019291829185019084908082843750949750610bd49650505050505050565b34801561053d57600080fd5b5061024a600160a060020a03600435166024351515610ce7565b34801561056357600080fd5b50610270600160a060020a0360043516610d29565b34801561058457600080fd5b50610437610d3e565b34801561059957600080fd5b50610299610d4d565b3480156105ae57600080fd5b50610270600160a060020a0360043516602435610d53565b3480156105d257600080fd5b5061024a600435610dec565b3480156105ea57600080fd5b50610299600160a060020a0360043581169060243516610e0f565b34801561061157600080fd5b5061024a600160a060020a0360043516610e3a565b34801561063257600080fd5b506040805160206004803580820135838102808601850190965280855261024a95369593946024949385019291829185019084908082843750949750610e959650505050505050565b6007805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107015780601f106106d657610100808354040283529160200191610701565b820191906000526020600020905b8154815290600101906020018083116106e457829003601f168201915b505050505081565b600554600160a060020a0316331461072057600080fd5b600160a060020a03919091166000908152600160205260409020805460ff1916911515919091179055565b336000818152600460209081526040808320600160a060020a038716808552908352818420869055815186815291519394909390927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925928290030190a350600192915050565b60005481565b60035490565b60006107ca848484610ff2565b949350505050565b60065474010000000000000000000000000000000000000000900460ff1681565b600b5460ff1681565b336000908152600c602052604090205460ff16151561081a57600080fd5b600b5460ff16151560011480156108455750600a54600354610842908363ffffffff61104d16565b11155b151561085057600080fd5b600354610863908263ffffffff61104d16565b60035561087f8161087384610a5b565b9063ffffffff61104d16565b600160a060020a038316600090815260026020526040902055600a5460035414156108af57600b805460ff191690555b604080518281529051600160a060020a038416917f0f6798a560793a54c3bcfe86a93cde1e73087d944c0ea20544137d4121396885919081900360200190a25050565b6009805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107015780601f106106d657610100808354040283529160200191610701565b336000908152600460209081526040808320600160a060020a0386168452909152812054808311156109a257336000908152600460209081526040808320600160a060020a03881684529091528120556109d7565b6109b2818463ffffffff61106016565b336000908152600460209081526040808320600160a060020a03891684529091529020555b336000818152600460209081526040808320600160a060020a0389168085529083529281902054815190815290519293927f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929181900390910190a35060019392505050565b6000610a56600354600a5461106090919063ffffffff16565b905090565b600160a060020a031660009081526002602052604090205490565b600554600160a060020a03163314610a8d57600080fd5b600160a060020a03919091166000908152600d60205260409020805460ff1916911515919091179055565b600e5481565b600654600160a060020a0316331415610b01576006546005805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039092169190911790555b565b336000908152600d602052604090205460ff161515610b2157600080fd5b600b805460ff19169055565b600554600160a060020a031681565b6008805460408051602060026001851615610100026000190190941693909304601f810184900484028201840190925281815292918301828280156107015780601f106106d657610100808354040283529160200191610701565b600c6020526000908152604090205460ff1681565b6000610bb88383611072565b9392505050565b600d6020526000908152604090205460ff1681565b6005546000908190600160a060020a03163314610bf057600080fd5b5060005b8251811015610ce15783600160a060020a03166370a082318483815181101515610c1a57fe5b906020019060200201516040518263ffffffff167c01000000000000000000000000000000000000000000000000000000000281526004018082600160a060020a0316600160a060020a03168152602001915050602060405180830381600087803b158015610c8857600080fd5b505af1158015610c9c573d6000803e3d6000fd5b505050506040513d6020811015610cb257600080fd5b50518351909250610cd990849083908110610cc957fe5b90602001906020020151836107fc565b600101610bf4565b50505050565b600554600160a060020a03163314610cfe57600080fd5b600160a060020a03919091166000908152600c60205260409020805460ff1916911515919091179055565b60016020526000908152604090205460ff1681565b600654600160a060020a031681565b600a5481565b336000908152600460209081526040808320600160a060020a0386168452909152812054610d87908363ffffffff61104d16565b336000818152600460209081526040808320600160a060020a0389168085529083529281902085905580519485525191937f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b925929081900390910190a350600192915050565b336000908152600d602052604090205460ff161515610e0a57600080fd5b600055565b600160a060020a03918216600090815260046020908152604080832093909416825291909152205490565b600554600160a060020a03163314610e5157600080fd5b600160a060020a0381161515610e6657600080fd5b6006805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b6005546000908190600160a060020a03163314610eb157600080fd5b50693ff7845a0ea77c7aa5b4905060005b8251811015610fed57600e54600010610eda57600080fd5b600e8054600019019055600354610ef7908363ffffffff61106016565b600381905550610f4282600260008685815181101515610f1357fe5b6020908102909101810151600160a060020a03168252810191909152604001600020549063ffffffff61106016565b600260008584815181101515610f5457fe5b90602001906020020151600160a060020a0316600160a060020a03168152602001908152602001600020819055506000600160a060020a03168382815181101515610f9b57fe5b90602001906020020151600160a060020a03167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a3600101610ec2565b505050565b60008054600160a060020a03851682526001602081905260408320548692849242919091109160ff161515148061102c5750811515811515145b151561103757600080fd5b6110428787876110c5565b979650505050505050565b8181018281101561105a57fe5b92915050565b60008282111561106c57fe5b50900390565b60008054338083526001602081905260408420549192849242919091109160ff909116151514806110a65750811515811515145b15156110b157600080fd5b6110bb868661123e565b9695505050505050565b6000600160a060020a03831615156110dc57600080fd5b600160a060020a03841660009081526002602052604090205482111561110157600080fd5b600160a060020a038416600090815260046020908152604080832033845290915290205482111561113157600080fd5b600160a060020a03841660009081526002602052604090205461115a908363ffffffff61106016565b600160a060020a03808616600090815260026020526040808220939093559085168152205461118f908363ffffffff61104d16565b600160a060020a0380851660009081526002602090815260408083209490945591871681526004825282812033825290915220546111d3908363ffffffff61106016565b600160a060020a03808616600081815260046020908152604080832033845282529182902094909455805186815290519287169391927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef929181900390910190a35060019392505050565b6000600160a060020a038316151561125557600080fd5b3360009081526002602052604090205482111561127157600080fd5b33600090815260026020526040902054611291908363ffffffff61106016565b3360009081526002602052604080822092909255600160a060020a038516815220546112c3908363ffffffff61104d16565b600160a060020a0384166000818152600260209081526040918290209390935580518581529051919233927fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9281900390910190a3506001929150505600a165627a7a72305820ba97c2db0ecb8c88f4f6bac1192a16775b5a85477fb40e08ece88bce8c16c1910029\n";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_APPROVEANDCALL = "approveAndCall";

    public static final String FUNC_VERSION = "version";

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    protected uDOO(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected uDOO(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF, 
                Arrays.<Type>asList(new Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new Address(_owner),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferEventResponse> transferEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(TRANSFER_EVENT, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        return transferEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, ApprovalEventResponse>() {
            @Override
            public ApprovalEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(APPROVAL_EVENT, log);
                ApprovalEventResponse typedResponse = new ApprovalEventResponse();
                typedResponse.log = log;
                typedResponse._owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public static RemoteCall<uDOO> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialAmount),
                new Utf8String(_tokenName),
                new Uint8(_decimalUnits),
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(uDOO.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<uDOO> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, BigInteger _initialAmount, String _tokenName, BigInteger _decimalUnits, String _tokenSymbol) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Uint256(_initialAmount),
                new Utf8String(_tokenName),
                new Uint8(_decimalUnits),
                new Utf8String(_tokenSymbol)));
        return deployRemoteCall(uDOO.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final Function function = new Function(
                FUNC_APPROVEANDCALL,
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value),
                new org.web3j.abi.datatypes.DynamicBytes(_extraData)), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> version() {
        final Function function = new Function(FUNC_VERSION, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public static uDOO load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new uDOO(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static uDOO load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new uDOO(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
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
}
