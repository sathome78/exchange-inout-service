package com.exrates.inout.service.ethereum.ethTokensWrappers;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.StaticArray6;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tuples.generated.Tuple7;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import rx.Observable;
import rx.functions.Func1;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 3.4.0.
 */
public class SPD extends Contract implements ethTokenNotERC20 {
    private static final String BINARY = "60606040526006604051805910620000145750595b908082528060200260200182016040525060019080516200003a929160200190620002ca565b5034156200004757600080fd5b60008054600160a060020a03191633600160a060020a031617815560018054635af62e80929081106200007657fe5b90600052602060002090600491828204019190066008026101000a8154816001604060020a0302191690836001604060020a03160217905550635af62e8062278d0001600180815481101515620000c957fe5b90600052602060002090600491828204019190066008026101000a8154816001604060020a0302191690836001604060020a03160217905550635af62e80624f1a0001600160028154811015156200011d57fe5b90600052602060002090600491828204019190066008026101000a8154816001604060020a0302191690836001604060020a03160217905550635af62e806276a70001600160038154811015156200017157fe5b90600052602060002090600491828204019190066008026101000a8154816001604060020a0302191690836001604060020a03160217905550635af62e8062ed4e000160016004815481101515620001c557fe5b90600052602060002090600491828204019190066008026101000a8154816001604060020a0302191690836001604060020a03160217905550635af62e80630163f50001600160058154811015156200021a57fe5b600091825260208083206004830401805460039093166008026101000a6001604060020a0381810219909416959093169290920293909317905533600160a060020a031681526002909152604081206b204fce5e3e25026110000000910155600160a060020a03331660007fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef6b204fce5e3e2502611000000060405190815260200160405180910390a3620003b4565b82805482825590600052602060002090600301600490048101928215620003775791602002820160005b838211156200034057835183826101000a8154816001604060020a0302191690836001604060020a031602179055509260200192600801602081600701049283019260010302620002f4565b8015620003755782816101000a8154906001604060020a03021916905560080160208160070104928301926001030262000340565b505b506200038592915062000389565b5090565b620003b191905b808211156200038557805467ffffffffffffffff1916815560010162000390565b90565b61139180620003c46000396000f3006060604052600436106100cf5763ffffffff7c010000000000000000000000000000000000000000000000000000000060003504166306fdde0381146100d4578063095ea7b31461015e5780630e64780d1461019457806318160ddd146101c55780631f470b14146101ea57806323b872dd14610241578063313ce5671461026957806370a082311461029257806370d25a9f146102b15780638da5cb5b1461056e57806395d89b411461059d578063a9059cbb146105b0578063dd62ed3e146105d2578063f2fde38b146105f7575b600080fd5b34156100df57600080fd5b6100e7610618565b60405160208082528190810183818151815260200191508051906020019080838360005b8381101561012357808201518382015260200161010b565b50505050905090810190601f1680156101505780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561016957600080fd5b610180600160a060020a036004351660243561064f565b604051901515815260200160405180910390f35b341561019f57600080fd5b610180600160a060020a036004351660243560443560643560843560a43560c4356106bb565b34156101d057600080fd5b6101d861091b565b60405190815260200160405180910390f35b34156101f557600080fd5b610209600160a060020a036004351661092b565b604051808260c080838360005b8381101561022e578082015183820152602001610216565b5050505090500191505060405180910390f35b341561024c57600080fd5b610180600160a060020a0360043581169060243516604435610a6c565b341561027457600080fd5b61027c610bc2565b60405160ff909116815260200160405180910390f35b341561029d57600080fd5b6101d8600160a060020a0360043516610bc7565b34156102bc57600080fd5b6102d0600160a060020a0360043516610be5565b6040518088600160a060020a0316600160a060020a0316815260200180602001806020018060200180602001806020018060200187810387528d818151815260200191508051906020019080838360005b83811015610339578082015183820152602001610321565b50505050905090810190601f1680156103665780820380516001836020036101000a031916815260200191505b5087810386528c818151815260200191508051906020019080838360005b8381101561039c578082015183820152602001610384565b50505050905090810190601f1680156103c95780820380516001836020036101000a031916815260200191505b5087810385528b818151815260200191508051906020019080838360005b838110156103ff5780820151838201526020016103e7565b50505050905090810190601f16801561042c5780820380516001836020036101000a031916815260200191505b5087810384528a818151815260200191508051906020019080838360005b8381101561046257808201518382015260200161044a565b50505050905090810190601f16801561048f5780820380516001836020036101000a031916815260200191505b50878103835289818151815260200191508051906020019080838360005b838110156104c55780820151838201526020016104ad565b50505050905090810190601f1680156104f25780820380516001836020036101000a031916815260200191505b50878103825288818151815260200191508051906020019080838360005b83811015610528578082015183820152602001610510565b50505050905090810190601f1680156105555780820380516001836020036101000a031916815260200191505b509d505050505050505050505050505060405180910390f35b341561057957600080fd5b610581610ca7565b604051600160a060020a03909116815260200160405180910390f35b34156105a857600080fd5b6100e7610cb6565b34156105bb57600080fd5b610180600160a060020a0360043516602435610ced565b34156105dd57600080fd5b6101d8600160a060020a0360043581169060243516610de9565b341561060257600080fd5b610616600160a060020a0360043516610e14565b005b60408051908101604052600781527f5350494e444c4500000000000000000000000000000000000000000000000000602082015281565b600160a060020a03338116600081815260036020908152604080832094871680845294909152808220859055909291907f8c5be1e5ebec7d5bd14f71427d1e84f3dd0314c0f7b2291e5b200ac8c7c3b9259085905190815260200160405180910390a350600192915050565b60008054819033600160a060020a039081169116146106d957600080fd5b600160a060020a03891615156106ee57600080fd5b6106f733610eaf565b50600061070a818963ffffffff610f1e16565b905061071c818863ffffffff610f1e16565b905061072e818763ffffffff610f1e16565b9050610740818663ffffffff610f1e16565b9050610752818563ffffffff610f1e16565b9050610764818463ffffffff610f1e16565b600160a060020a03331660009081526002602052604081209192506107949183915b01549063ffffffff610f3716565b600160a060020a0333811660009081526002602052604080822093909355908b1681529081206107d0918a91905b01549063ffffffff610f1e16565b600160a060020a038a1660009081526002602052604090209081556107f890889060016107c2565b600160a060020a038a16600090815260026020819052604090912060018101929092556108269188916107c2565b600160a060020a038a1660009081526002602081905260409091209081019190915561085590869060036107c2565b600160a060020a038a166000908152600260205260409020600381019190915561088290859060046107c2565b600160a060020a038a16600090815260026020526040902060048101919091556108af90849060056107c2565b600160a060020a038a16600090815260026020526040902060050155600160a060020a03808a169033167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8360405190815260200160405180910390a350600198975050505050505050565b6b204fce5e3e2502611000000090565b61093361132c565b61093b61132c565b600160a060020a038316600090815260026020526040902054815260015b600154811015610a6557600160a060020a0384166000908152600260205260409020816006811061098657fe5b015482826006811061099457fe5b602002015260018054829081106109a757fe5b6000918252602090912060048204015460039091166008026101000a900467ffffffffffffffff164210610a2657600160a060020a0384166000908152600260205260409020610a0c9082600681106109fc57fe5b015483519063ffffffff610f1e16565b82526000828260068110610a1c57fe5b6020020152610a5d565b600160a060020a03841660009081526002602052604090208160068110610a4957fe5b0154828260068110610a5757fe5b60200201525b600101610959565b5092915050565b6000600160a060020a0383161515610a8357600080fd5b30600160a060020a031683600160a060020a031614151515610aa457600080fd5b610aad84610eaf565b600160a060020a0384166000908152600260205260408120610ad191849190610786565b600160a060020a03858116600090815260026020526040808220939093559085168152908120610b03918491906107c2565b600160a060020a0384166000908152600260205260408120900155600160a060020a0380851660009081526003602090815260408083203390941683529290522054610b55908363ffffffff610f3716565b600160a060020a03808616600081815260036020908152604080832033861684529091529081902093909355908516917fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef9085905190815260200160405180910390a35060019392505050565b601281565b6000610bd161132c565b610bda8361092b565b905080519392505050565b6000610bef611353565b610bf7611353565b610bff611353565b610c07611353565b610c0f611353565b610c17611353565b6000610c2161132c565b899150600160a060020a0382161515610c38573391505b610c418261092b565b905081610c558260005b6020020151610f4c565b610c60836001610c4b565b610c6b846002610c4b565b610c76856003610c4b565b610c81866004610c4b565b610c8c876005610c4b565b98509850985098509850985098505050919395979092949650565b600054600160a060020a031681565b60408051908101604052600381527f5350440000000000000000000000000000000000000000000000000000000000602082015281565b6000600160a060020a0383161515610d0457600080fd5b30600160a060020a031683600160a060020a031614151515610d2557600080fd5b610d2e33610eaf565b600160a060020a0333166000908152600260205260408120610d5291849190610786565b600160a060020a03338116600090815260026020526040808220939093559085168152908120610d84918491906107c2565b600160a060020a0384166000908152600260205260408120900155600160a060020a038084169033167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef8460405190815260200160405180910390a350600192915050565b600160a060020a03918216600090815260036020908152604080832093909416825291909152205490565b60005433600160a060020a03908116911614610e2f57600080fd5b600160a060020a0381161515610e4457600080fd5b600054600160a060020a0380831691167f8be0079c531659141344cd1fd0a4f28419497f9722a3daafe3b4186f6b6457e060405160405180910390a36000805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a0392909216919091179055565b610eb761132c565b6000610ec28361092b565b9150600090505b60068160ff161015610f19578160ff821660068110610ee457fe5b6020020151600160a060020a038416600090815260026020526040902060ff831660068110610f0f57fe5b0155600101610ec9565b505050565b600082820183811015610f3057600080fd5b9392505050565b600082821115610f4657600080fd5b50900390565b610f54611353565b600080600080610f62611353565b610f6a611353565b601e604051805910610f795750595b818152601f19601f830116810160200160405290509150601d95505b8560ff16600b1415610fee577f2e000000000000000000000000000000000000000000000000000000000000008260ff881681518110610fd157fe5b906020010190600160f860020a031916908160001a905350611044565b600a880660300160f860020a02828760ff168151811061100a57fe5b906020010190600160f860020a031916908160001a90535061103388600a63ffffffff61131516565b975060ff8616151561104457611050565b60001990950194610f95565b600093505b600a8460ff1610156110c4577f30000000000000000000000000000000000000000000000000000000000000008260ff86168151811061109157fe5b016020015160f860020a900460f860020a02600160f860020a0319161415156110b9576110c4565b600190930192611055565b601d92505b600c8360ff161115611139577f30000000000000000000000000000000000000000000000000000000000000008260ff85168151811061110557fe5b016020015160f860020a900460f860020a02600160f860020a03191614151561112d57611139565b600019909201916110c9565b60ff6005858503011660405180591061114f5750595b818152601f19601f830116810160200160405290509050600094508395505b60ff808416908716116111d257818660ff168151811061118a57fe5b016020015160f860020a900460f860020a02818660ff16815181106111ab57fe5b906020010190600160f860020a031916908160001a9053506001958601959094019361116e565b60018501947f200000000000000000000000000000000000000000000000000000000000000090829060ff168151811061120857fe5b906020010190600160f860020a031916908160001a90535060018501947f530000000000000000000000000000000000000000000000000000000000000090829060ff168151811061125657fe5b906020010190600160f860020a031916908160001a90535060018501947f500000000000000000000000000000000000000000000000000000000000000090829060ff16815181106112a457fe5b906020010190600160f860020a031916908160001a90535060018501947f440000000000000000000000000000000000000000000000000000000000000090829060ff16815181106112f257fe5b906020010190600160f860020a031916908160001a905350979650505050505050565b600080828481151561132357fe5b04949350505050565b60c06040519081016040526006815b600081526020019060019003908161133b5790505090565b602060405190810160405260008152905600a165627a7a723058208718140c084bead5dcdfca3173be3182266b3f3582cdcafdc38b7e043b9f81030029";

    public static final String FUNC_NAME = "name";

    public static final String FUNC_APPROVE = "approve";

    public static final String FUNC_DISTRIBUTE = "distribute";

    public static final String FUNC_TOTALSUPPLY = "totalSupply";

    public static final String FUNC_LOCKUPAMOUNTOF = "lockUpAmountOf";

    public static final String FUNC_TRANSFERFROM = "transferFrom";

    public static final String FUNC_DECIMALS = "decimals";

    public static final String FUNC_BALANCEOF = "balanceOf";

    public static final String FUNC_LOCKUPAMOUNTSTROF = "lockUpAmountStrOf";

    public static final String FUNC_OWNER = "owner";

    public static final String FUNC_SYMBOL = "symbol";

    public static final String FUNC_TRANSFER = "transfer";

    public static final String FUNC_ALLOWANCE = "allowance";

    public static final String FUNC_TRANSFEROWNERSHIP = "transferOwnership";

    public static final Event OWNERSHIPTRANSFERRED_EVENT = new Event("OwnershipTransferred", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList());
    ;

    public static final Event APPROVAL_EVENT = new Event("Approval", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    public static final Event TRANSFER_EVENT = new Event("Transfer", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
            Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
    ;

    protected SPD(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected SPD(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public RemoteCall<String> name() {
        final Function function = new Function(FUNC_NAME, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Utf8String>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        final Function function = new Function(
                FUNC_APPROVE, 
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> distribute(String _to, BigInteger _a, BigInteger _b, BigInteger _c, BigInteger _d, BigInteger _e, BigInteger _f) {
        final Function function = new Function(
                FUNC_DISTRIBUTE,
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_a),
                new Uint256(_b),
                new Uint256(_c),
                new Uint256(_d),
                new Uint256(_e),
                new Uint256(_f)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function(FUNC_TOTALSUPPLY,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<List> lockUpAmountOf(String _holder) {
        final Function function = new Function(FUNC_LOCKUPAMOUNTOF,
                Arrays.<Type>asList(new Address(_holder)),
                Arrays.<TypeReference<?>>asList(new TypeReference<StaticArray6<Uint256>>() {}));
        return new RemoteCall<List>(
                new Callable<List>() {
                    @Override
                    @SuppressWarnings("unchecked")
                    public List call() throws Exception {
                        List<Type> result = (List<Type>) executeCallSingleValueReturn(function, List.class);
                        return convertToNative(result);
                    }
                });
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

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function(FUNC_DECIMALS,
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _holder) {
        final Function function = new Function(FUNC_BALANCEOF,
                Arrays.<Type>asList(new Address(_holder)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Tuple7<String, String, String, String, String, String, String>> lockUpAmountStrOf(String _address) {
        final Function function = new Function(FUNC_LOCKUPAMOUNTSTROF,
                Arrays.<Type>asList(new Address(_address)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Utf8String>() {}));
        return new RemoteCall<Tuple7<String, String, String, String, String, String, String>>(
                new Callable<Tuple7<String, String, String, String, String, String, String>>() {
                    @Override
                    public Tuple7<String, String, String, String, String, String, String> call() throws Exception {
                        List<Type> results = executeCallMultipleValueReturn(function);
                        return new Tuple7<String, String, String, String, String, String, String>(
                                (String) results.get(0).getValue(),
                                (String) results.get(1).getValue(),
                                (String) results.get(2).getValue(),
                                (String) results.get(3).getValue(),
                                (String) results.get(4).getValue(),
                                (String) results.get(5).getValue(),
                                (String) results.get(6).getValue());
                    }
                });
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

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                FUNC_TRANSFER,
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _holder, String _spender) {
        final Function function = new Function(FUNC_ALLOWANCE,
                Arrays.<Type>asList(new Address(_holder),
                new Address(_spender)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferOwnership(String newOwner) {
        final Function function = new Function(
                FUNC_TRANSFEROWNERSHIP,
                Arrays.<Type>asList(new Address(newOwner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<SPD> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SPD.class, web3j, credentials, gasPrice, gasLimit, BINARY, "");
    }

    public static RemoteCall<SPD> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return deployRemoteCall(SPD.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, "");
    }

    public List<OwnershipTransferredEventResponse> getOwnershipTransferredEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, transactionReceipt);
        ArrayList<OwnershipTransferredEventResponse> responses = new ArrayList<OwnershipTransferredEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(EthFilter filter) {
        return web3j.ethLogObservable(filter).map(new Func1<Log, OwnershipTransferredEventResponse>() {
            @Override
            public OwnershipTransferredEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(OWNERSHIPTRANSFERRED_EVENT, log);
                OwnershipTransferredEventResponse typedResponse = new OwnershipTransferredEventResponse();
                typedResponse.log = log;
                typedResponse.previousOwner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.newOwner = (String) eventValues.getIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public Observable<OwnershipTransferredEventResponse> ownershipTransferredEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWNERSHIPTRANSFERRED_EVENT));
        return ownershipTransferredEventObservable(filter);
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(APPROVAL_EVENT, transactionReceipt);
        ArrayList<ApprovalEventResponse> responses = new ArrayList<ApprovalEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            ApprovalEventResponse typedResponse = new ApprovalEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.holder = (String) eventValues.getIndexedValues().get(0).getValue();
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
                typedResponse.holder = (String) eventValues.getIndexedValues().get(0).getValue();
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

    public static SPD load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new SPD(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static SPD load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new SPD(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class OwnershipTransferredEventResponse {
        public Log log;

        public String previousOwner;

        public String newOwner;
    }

    public static class ApprovalEventResponse {
        public Log log;

        public String holder;

        public String spender;

        public BigInteger value;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }
}
