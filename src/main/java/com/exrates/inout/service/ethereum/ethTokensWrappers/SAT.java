package com.exrates.inout.service.ethereum.ethTokensWrappers;

import org.web3j.abi.EventEncoder;
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
public class SAT extends Contract implements ethTokenERC20 {
    public static final String FUNC_END_ICO = "end_ICO";
    public static final String FUNC_NAME = "name";
    public static final String FUNC_APPROVE = "approve";
    public static final String FUNC_RELEASEICO = "releaseICO";
    public static final String FUNC_TOTALSUPPLY = "totalSupply";
    public static final String FUNC_TRANSFERFROM = "transferFrom";
    public static final String FUNC_DECIMALS = "decimals";
    public static final String FUNC_STOPICO = "StopICO";
    public static final String FUNC_BURN = "burn";
    public static final String FUNC_BALANCEOF = "balanceOf";
    public static final String FUNC_START_ICO = "start_ICO";
    public static final String FUNC_OWNER = "owner";
    public static final String FUNC_SYMBOL = "symbol";
    public static final String FUNC_SET_CENTRALACCOUNT = "set_centralAccount";
    public static final String FUNC_DRAIN = "drain";
    public static final String FUNC__TOTALSUPPLY = "_totalsupply";
    public static final String FUNC_TRANSFER = "transfer";
    public static final String FUNC_STAGE = "stage";
    public static final String FUNC_STARTDATE = "startdate";
    public static final String FUNC_ALLOWANCE = "allowance";
    public static final String FUNC_TRANSFERBY = "transferby";
    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";
    public static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    public static final Event APPROVAL_EVENT = new Event("Approval",
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;
    private static final String BINARY = "606060405260008054600160a060020a031916738055d0504666e2b6942beb8d6014c964658ca59117905567016345785d8a00006001556002805460a060020a60ff0219169055341561005157600080fd5b60028054600160a060020a03191633600160a060020a03908116919091178083558116600090815260096020526040808220667c5850872380009055308316825280822066e6ed27d66680009055600b805460ff1916905592549091168082528282205490926000805160206113ca83398151915291905190815260200160405180910390a3600160a060020a033016600081815260096020526040808220546000805160206113ca833981519152915190815260200160405180910390a36112ab8061011f6000396000f30060606040526004361061010e5763ffffffff60e060020a60003504166302c3d7f681146105c657806306fdde03146105db578063095ea7b314610665578063124d33961461069b57806318160ddd146106ae57806323b872dd146106d3578063313ce567146106fb5780633dbedbd41461072457806342966c681461073757806370a082311461074d578063807d2da31461076c5780638da5cb5b1461077f57806395d89b41146107ae57806397668720146107c15780639890220b146107e0578063a393dc44146107f3578063a9059cbb14610806578063c040e6b814610828578063cde9f2ea1461085f578063dd62ed3e14610872578063eb9763ed14610897578063f2fde38b146108bf575b600080808080600180600b5460ff16600381111561012857fe5b1461013257600080fd5b66038d7ea4c6800034101561014657600080fd5b60025474010000000000000000000000000000000000000000900460ff16158015610180575060025433600160a060020a03908116911614155b151561018b57600080fd5b60008054600160a060020a031690630c560c6490806040516020015260405160e060020a63ffffffff84160281526004810191909152602401602060405180830381600087803b15156101dd57600080fd5b6102c65a03f115156101ee57600080fd5b5050506040518051965061020b905086600e63ffffffff6108de16565b9450610221856305f5e10063ffffffff61091416565b9450610233348663ffffffff61091416565b93506000925060045442101561030457642e90edd000841080159061025e57506501d1a3543f008411155b1561028c57610285606461027986603263ffffffff6108de16565b9063ffffffff61091416565b92506102ff565b6501d1a3543f00841180156102a75750650da46fb60f008411155b156102c257610285606461027986603763ffffffff6108de16565b650da46fb60f008411156102e657610285606461027986603c63ffffffff6108de16565b6102fc606461027986602d63ffffffff6108de16565b92505b610541565b6004544210158015610317575060055442105b156103c257642e90edd000841080159061033757506501d1a3543f008411155b1561035257610285606461027986602863ffffffff6108de16565b6501d1a3543f008411801561036d5750650da46fb60f008411155b1561038857610285606461027986602d63ffffffff6108de16565b650da46fb60f008411156103ac57610285606461027986603263ffffffff6108de16565b6102fc606461027986602363ffffffff6108de16565b60055442101580156103d5575060065442105b1561048057642e90edd00084108015906103f557506501d1a3543f008411155b1561041057610285606461027986601e63ffffffff6108de16565b6501d1a3543f008411801561042b5750650da46fb60f008411155b1561044657610285606461027986602363ffffffff6108de16565b650da46fb60f0084111561046a57610285606461027986602863ffffffff6108de16565b6102fc606461027986601963ffffffff6108de16565b6006544210158015610493575060075442105b1561054157642e90edd00084108015906104b357506501d1a3543f008411155b156104ce576102fc606461027986601463ffffffff6108de16565b6501d1a3543f00841180156104e95750650da46fb60f008411155b15610504576102fc606461027986601963ffffffff6108de16565b650da46fb60f00841115610528576102fc606461027986601e63ffffffff6108de16565b61053e606461027986600f63ffffffff6108de16565b92505b828401915030600160a060020a031663a9059cbb338460006040516020015260405160e060020a63ffffffff8516028152600160a060020a0390921660048301526024820152604401602060405180830381600087803b15156105a357600080fd5b6102c65a03f115156105b457600080fd5b50505060405180515050505050505050005b34156105d157600080fd5b6105d961092b565b005b34156105e657600080fd5b6105ee61098a565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561062a578082015183820152602001610612565b50505050905090810190601f1680156106575780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561067057600080fd5b610687600160a060020a03600435166024356109c1565b604051901515815260200160405180910390f35b34156106a657600080fd5b6105d9610a7c565b34156106b957600080fd5b6106c1610ae6565b60405190815260200160405180910390f35b34156106de57600080fd5b610687600160a060020a0360043581169060243516604435610aec565b341561070657600080fd5b61070e610c04565b60405160ff909116815260200160405180910390f35b341561072f57600080fd5b6105d9610c09565b341561074257600080fd5b6105d9600435610c87565b341561075857600080fd5b6106c1600160a060020a0360043516610db3565b341561077757600080fd5b6105d9610dce565b341561078a57600080fd5b610792610e5e565b604051600160a060020a03909116815260200160405180910390f35b34156107b957600080fd5b6105ee610e6d565b34156107cc57600080fd5b6105d9600160a060020a0360043516610ea4565b34156107eb57600080fd5b6105d9610eee565b34156107fe57600080fd5b6106c1610f44565b341561081157600080fd5b610687600160a060020a0360043516602435610f4a565b341561083357600080fd5b61083b61100e565b6040518082600381111561084b57fe5b60ff16815260200191505060405180910390f35b341561086a57600080fd5b6106c1611017565b341561087d57600080fd5b6106c1600160a060020a036004358116906024351661101d565b34156108a257600080fd5b610687600160a060020a0360043581169060243516604435611076565b34156108ca57600080fd5b6105d9600160a060020a03600435166111a2565b6000808315156108f1576000915061090d565b5082820282848281151561090157fe5b041461090957fe5b8091505b5092915050565b600080828481151561092257fe5b04949350505050565b60025433600160a060020a0390811691161461094657600080fd5b600180600b5460ff16600381111561095a57fe5b1461096457600080fd5b600754421161097257600080fd5b600b80546003919060ff19166001835b021790555050565b60408051908101604052601581527f536f6369616c20416374697669747920546f6b656e0000000000000000000000602082015281565b60008115806109f35750600160a060020a033381166000908152600a6020908152604080832093871683529290522054155b15156109fe57600080fd5b600160a060020a0383161515610a1357600080fd5b600160a060020a033381166000818152600a6020908152604080832094881680845294909152908190208590557f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a350600192915050565b60025433600160a060020a03908116911614610a9757600080fd5b600280600b5460ff166003811115610aab57fe5b14610ab557600080fd5b6002805474ff000000000000000000000000000000000000000019169055600b80546001919060ff19168280610982565b60015490565b6000600160a060020a0383161515610b0357600080fd5b600160a060020a038416600090815260096020526040902054610b2c908363ffffffff61123e16565b600160a060020a03808616600090815260096020908152604080832094909455600a8152838220339093168252919091522054610b6f908363ffffffff61123e16565b600160a060020a038086166000908152600a602090815260408083203385168452825280832094909455918616815260099091522054610bb5908363ffffffff61125016565b600160a060020a03808516600081815260096020526040908190209390935591908616906000805160206112608339815191529085905190815260200160405180910390a35060019392505050565b600881565b60025433600160a060020a03908116911614610c2457600080fd5b600180600b5460ff166003811115610c3857fe5b14610c4257600080fd5b6002805474ff0000000000000000000000000000000000000000191674010000000000000000000000000000000000000000178155600b805460ff1916600183610982565b60025433600160a060020a03908116911614610ca257600080fd5b600160a060020a033016600090815260096020526040902054811115610cc757600080fd5b600154610cda908263ffffffff61123e16565b600155600160a060020a033016600090815260096020526040902054610d06908263ffffffff61123e16565b600160a060020a03301660009081526009602052604081209190915580527fec8156718a8372b1db44bb411437d0870f3e3790d4a08526d024ce1b0b668f6b54610d56908263ffffffff61125016565b600080805260096020527fec8156718a8372b1db44bb411437d0870f3e3790d4a08526d024ce1b0b668f6b9190915530600160a060020a03166000805160206112608339815191528360405190815260200160405180910390a350565b600160a060020a031660009081526009602052604090205490565b60025433600160a060020a03908116911614610de957600080fd5b600080600b5460ff166003811115610dfd57fe5b14610e0757600080fd5b50600b805460ff191660011790556002805474ff0000000000000000000000000000000000000000191690554260038190556212750081016004556224ea00810160055562375f0081016006556249d40001600755565b600254600160a060020a031681565b60408051908101604052600381527f5341540000000000000000000000000000000000000000000000000000000000602082015281565b60025433600160a060020a03908116911614610ebf57600080fd5b6008805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b60025433600160a060020a03908116911614610f0957600080fd5b600254600160a060020a039081169030163180156108fc0290604051600060405180830381858888f193505050501515610f4257600080fd5b565b60015481565b6000600160a060020a0383161515610f6157600080fd5b600160a060020a033316600090815260096020526040902054610f8a908363ffffffff61123e16565b600160a060020a033381166000908152600960205260408082209390935590851681522054610fbf908363ffffffff61125016565b600160a060020a0380851660008181526009602052604090819020939093559133909116906000805160206112608339815191529085905190815260200160405180910390a350600192915050565b600b5460ff1681565b60035481565b6000600160a060020a0383161580159061103f5750600160a060020a03821615155b151561104a57600080fd5b50600160a060020a039182166000908152600a6020908152604080832093909416825291909152205490565b60085460009033600160a060020a0390811691161461109457600080fd5b600160a060020a03831615156110a957600080fd5b30600160a060020a031684600160a060020a03161415156110c957600080fd5b600160a060020a0384166000908152600960205260409020546110f2908363ffffffff61123e16565b600160a060020a038086166000908152600960205260408082209390935590851681522054611127908363ffffffff61125016565b600160a060020a03808516600090815260096020526040902091909155841615156111635760015461115f908363ffffffff61125016565b6001555b82600160a060020a031684600160a060020a03166000805160206112608339815191528460405190815260200160405180910390a35060019392505050565b60025433600160a060020a039081169116146111bd57600080fd5b600254600160a060020a039081166000908152600960205260408082205492841682529020546111f29163ffffffff61125016565b600160a060020a039182166000818152600960205260408082209390935560028054909416815291822091909155815473ffffffffffffffffffffffffffffffffffffffff1916179055565b60008282111561124a57fe5b50900390565b60008282018381101561090957fe00ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3efa165627a7a72305820a1c42cdc6fc9eb09efc77750532b747c24197c4fe0cb0693534aa5cdeaba5ec20029ddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef\n"
            + "\n";
    ;

    protected SAT(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SAT(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static RemoteCall<SAT> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SAT.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SAT> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SAT.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public static SAT load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SAT(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SAT load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SAT(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<TransactionReceipt> end_ICO() {
        final Function function = new Function(
                FUNC_END_ICO,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _amount) {
        final Function function = new Function(
                FUNC_APPROVE,
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> releaseICO() {
        final Function function = new Function(
                FUNC_RELEASEICO,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFERFROM,
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> StopICO() {
        final Function function = new Function(
                FUNC_STOPICO,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> burn(BigInteger _amount) {
        final Function function = new Function(
                FUNC_BURN,
                Arrays.<Type>asList(new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new Address(_owner)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> start_ICO() {
        final Function function = new Function(
                FUNC_START_ICO,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> owner() {
        final Function function = new Function(FUNC_OWNER,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<String> symbol() {
        final Function function = new Function(FUNC_SYMBOL,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> set_centralAccount(String central_Acccount) {
        final Function function = new Function(
                FUNC_SET_CENTRALACCOUNT,
                Arrays.<Type>asList(new Address(central_Acccount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> drain() {
        final Function function = new Function(
                FUNC_DRAIN,
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> _totalsupply() {
        final Function function = new Function(FUNC__TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> stage() {
        final Function function = new Function(FUNC_STAGE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> startdate() {
        final Function function = new Function(FUNC_STARTDATE,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new Address(_owner),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferby(String _from, String _to, BigInteger _amount) {
        final Function function = new Function(
                FUNC_TRANSFERBY,
                Arrays.<Type>asList(new Address(_from),
                new Address(_to),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(TRANSFER_EVENT, transactionReceipt);
        ArrayList<TransferEventResponse> responses = new ArrayList<TransferEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferEventResponse typedResponse = new TransferEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
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
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.spender = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(APPROVAL_EVENT));
        return approvalEventObservable(filter);
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String owner;

        public String spender;

        public BigInteger value;
    }
}
