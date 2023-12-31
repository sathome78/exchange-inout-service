package com.exrates.inout.service.ethereum.ethTokensWrappers;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Bool;
import org.web3j.abi.datatypes.DynamicBytes;
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
 * <p>Generated with web3j version 3.3.1.
 */
public class NAC extends Contract implements ethTokenERC20 {
    private static final String BINARY = "60606040526040805190810160405280600881526020017f4e616d692049434f00000000000000000000000000000000000000000000000081525060009080519060200190620000519291906200021d565b506040805190810160405280600381526020017f4e41430000000000000000000000000000000000000000000000000000000000815250600190805190602001906200009f9291906200021d565b5060126002556000600360006101000a81548160ff02191690831515021790555060006004556000600560006101000a81548160ff02191690836004811115620000e557fe5b021790555060006006553415620000fb57600080fd5b6040516060806200312e8339810160405280805190602001909190805190602001909190805190602001909190505060008273ffffffffffffffffffffffffffffffffffffffff16141515156200015157600080fd5b82600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555081600860006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555080600960006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff160217905550505050620002cc565b828054600181600116156101000203166002900490600052602060002090601f016020900481019282601f106200026057805160ff191683800117855562000291565b8280016001018555821562000291579182015b828111156200029057825182559160200191906001019062000273565b5b509050620002a09190620002a4565b5090565b620002c991905b80821115620002c5576000816000905550600101620002ab565b5090565b90565b612e5280620002dc6000396000f3006060604052600436106101b7576000357c0100000000000000000000000000000000000000000000000000000000900463ffffffff1680630321f836146101c2578063055ad42e1461021757806306fdde031461024e578063095ea7b3146102dc57806318160ddd146103365780631bfd68141461035f5780631ca2e94a1461038c57806323b872dd146103b2578063292005a21461042b578063313ce56714610454578063341176d61461047d5780633bed33ce146104d25780634defd1bf146104f55780635058c4601461052e57806370a082311461057057806378044ba5146105bd5780638d70c0ce1461061e57806390a9cc021461066957806395d89b41146106be57806398d5fdca1461074c578063a76044a414610775578063a9059cbb1461079e578063a99d8d48146107e0578063abc4cbd3146107f5578063b237f7d414610837578063cae9ca5114610870578063ce6d35d11461090d578063d579f9e814610965578063dcfcda2b14610988578063dd62ed3e146109c1578063dff2db7114610a2d578063e0c6d1ed14610a82578063e2fdcc1714610a97578063f088d54714610aec578063f5d9778914610b1a575b6101c033610b53565b005b34156101cd57600080fd5b6101d5610d95565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b341561022257600080fd5b61022a610dbb565b6040518082600481111561023a57fe5b60ff16815260200191505060405180910390f35b341561025957600080fd5b610261610dce565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156102a1578082015181840152602081019050610286565b50505050905090810190601f1680156102ce5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b34156102e757600080fd5b61031c600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050610e6c565b604051808215151515815260200191505060405180910390f35b341561034157600080fd5b610349610f14565b6040518082815260200191505060405180910390f35b341561036a57600080fd5b610372610f1a565b604051808215151515815260200191505060405180910390f35b341561039757600080fd5b6103b0600480803560ff16906020019091905050610f2d565b005b34156103bd57600080fd5b610411600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190505061121a565b604051808215151515815260200191505060405180910390f35b341561043657600080fd5b61043e611362565b6040518082815260200191505060405180910390f35b341561045f57600080fd5b610467611372565b6040518082815260200191505060405180910390f35b341561048857600080fd5b610490611378565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156104dd57600080fd5b6104f3600480803590602001909190505061139e565b005b341561050057600080fd5b61052c600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506114c8565b005b341561053957600080fd5b61056e600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190505061159d565b005b341561057b57600080fd5b6105a7600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050611729565b6040518082815260200191505060405180910390f35b34156105c857600080fd5b61061c600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050611741565b005b341561062957600080fd5b610667600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091908035906020019091905050611a62565b005b341561067457600080fd5b61067c611d48565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b34156106c957600080fd5b6106d1611d6e565b6040518080602001828103825283818151815260200191508051906020019080838360005b838110156107115780820151818401526020810190506106f6565b50505050905090810190601f16801561073e5780820380516001836020036101000a031916815260200191505b509250505060405180910390f35b341561075757600080fd5b61075f611e0c565b6040518082815260200191505060405180910390f35b341561078057600080fd5b610788611f5d565b6040518082815260200191505060405180910390f35b34156107a957600080fd5b6107de600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050611f63565b005b34156107eb57600080fd5b6107f3611f8d565b005b341561080057600080fd5b610835600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091908035906020019091905050612015565b005b341561084257600080fd5b61086e600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050612080565b005b341561087b57600080fd5b6108f3600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803590602001909190803590602001908201803590602001908080601f01602080910402602001604051908101604052809392919081815260200183838082843782019150505050505091905050612308565b604051808215151515815260200191505060405180910390f35b341561091857600080fd5b610963600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506124a1565b005b341561097057600080fd5b610986600480803590602001909190505061250b565b005b341561099357600080fd5b6109bf600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050612571565b005b34156109cc57600080fd5b610a17600480803573ffffffffffffffffffffffffffffffffffffffff1690602001909190803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050612637565b6040518082815260200191505060405180910390f35b3415610a3857600080fd5b610a4061265c565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b3415610a8d57600080fd5b610a95612682565b005b3415610aa257600080fd5b610aaa61268e565b604051808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200191505060405180910390f35b610b18600480803573ffffffffffffffffffffffffffffffffffffffff16906020019091905050610b53565b005b3415610b2557600080fd5b610b51600480803573ffffffffffffffffffffffffffffffffffffffff169060200190919050506126b4565b005b600060016004811115610b6257fe5b600560009054906101000a900460ff166004811115610b7d57fe5b141515610b8957600080fd5b635abc2c8042111580610be95750600b60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16145b1515610bf457600080fd5b60003414151515610c0457600080fd5b610c0c611e0c565b340290506b033b2e3c9fd0803ce80000008160065401101515610c2e57600080fd5b610c8081600c60008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461277a90919063ffffffff16565b600c60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550610cd88160065461277a90919063ffffffff16565b6006819055508173ffffffffffffffffffffffffffffffffffffffff167f4f79409f494e81c38036d80aa8a6507c2cb08d90bfb2fead5519447646b3497e826040518082815260200191505060405180910390a28173ffffffffffffffffffffffffffffffffffffffff163073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a35050565b600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600560009054906101000a900460ff1681565b60008054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015610e645780601f10610e3957610100808354040283529160200191610e64565b820191906000526020600020905b815481529060010190602001808311610e4757829003601f168201915b505050505081565b6000600360009054906101000a900460ff161515610e8957600080fd5b81600d60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055506001905092915050565b60065481565b600360009054906101000a900460ff1681565b6000600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515610f8b57600080fd5b60006004811115610f9857fe5b600560009054906101000a900460ff166004811115610fb357fe5b148015610fd6575060016004811115610fc857fe5b826004811115610fd457fe5b145b80611028575060016004811115610fe957fe5b600560009054906101000a900460ff16600481111561100457fe5b14801561102757506002600481111561101957fe5b82600481111561102557fe5b145b5b806110ef57506001600481111561103b57fe5b600560009054906101000a900460ff16600481111561105657fe5b148061108757506002600481111561106a57fe5b600560009054906101000a900460ff16600481111561108557fe5b145b80156110a957506003600481111561109b57fe5b8260048111156110a757fe5b145b80156110ee57506000600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1614155b5b8061114157506002600481111561110257fe5b600560009054906101000a900460ff16600481111561111d57fe5b14801561114057506001600481111561113257fe5b82600481111561113e57fe5b145b5b806111a057506003600481111561115457fe5b600560009054906101000a900460ff16600481111561116f57fe5b148015611191575060048081111561118357fe5b82600481111561118f57fe5b145b801561119f57506000600654145b5b90508015156111ae57600080fd5b81600560006101000a81548160ff021916908360048111156111cc57fe5b02179055507f8d9efa3fab1bd6476defa44f520afbf9337886a4947021fd7f2775e0efaf4571826040518082600481111561120357fe5b60ff16815260200191505060405180910390a15050565b6000600360009054906101000a900460ff16151561123757600080fd5b600d60008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482111515156112c257600080fd5b81600d60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002060008282540392505081905550611357848484612798565b600190509392505050565b6b033b2e3c9fd0803ce800000081565b60025481565b600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156113fa57600080fd5b6000600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff161415151561144257600080fd5b60003073ffffffffffffffffffffffffffffffffffffffff163111156114c557600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff166108fc829081150290604051600060405180830381858888f1935050505015156114c457600080fd5b5b50565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561152457600080fd5b6003600481111561153157fe5b600560009054906101000a900460ff16600481111561154c57fe5b1415151561155957600080fd5b80600a60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b6000600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156115fb57600080fd5b600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1690508073ffffffffffffffffffffffffffffffffffffffff16632f54bf6e846000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b15156116c357600080fd5b6102c65a03f115156116d457600080fd5b5050506040518051905015611724578273ffffffffffffffffffffffffffffffffffffffff166108fc839081150290604051600060405180830381858888f19350505050151561172357600080fd5b5b505050565b600c6020528060005260406000206000915090505481565b600080843b915061179a84600c60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054612aae90919063ffffffff16565b600c60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000208190555061182f84600c60008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461277a90919063ffffffff16565b600c60008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508473ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef866040518082815260200191505060405180910390a36000821115611a5b578490508073ffffffffffffffffffffffffffffffffffffffff166309c716903386866000604051602001526040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018381526020018273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019350505050602060405180830381600087803b15156119c257600080fd5b6102c65a03f115156119d357600080fd5b50505060405180519050508273ffffffffffffffffffffffffffffffffffffffff168573ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167f7051b075ffda80300623a0c664d9583af6ff4153a784b041e17c2505eb758e25876040518082815260200191505060405180910390a45b5050505050565b600080843b9150611abb84600c60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054612aae90919063ffffffff16565b600c60003373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550611b5084600c60008873ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461277a90919063ffffffff16565b600c60008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020819055508473ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef866040518082815260200191505060405180910390a36000821115611d41578490508073ffffffffffffffffffffffffffffffffffffffff1663cd8d8da03386866000604051602001526040518463ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018381526020018281526020019350505050602060405180830381600087803b1515611cb757600080fd5b6102c65a03f11515611cc857600080fd5b50505060405180519050508473ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff167fb52303b2c118351a837187237ba9792c0733fe98fe5697c787d0f07116c2d8d58686604051808381526020018281526020019250505060405180910390a35b5050505050565b600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b60018054600181600116156101000203166002900480601f016020809104026020016040519081016040528092919081815260200182805460018160011615610100020316600290048015611e045780601f10611dd957610100808354040283529160200191611e04565b820191906000526020600020905b815481529060010190602001808311611de757829003601f168201915b505050505081565b6000635a725880421015611e2457610d7a9050611f5a565b42635a725880108015611e3b5750635a7b93004211155b15611e4a576109609050611f5a565b42635a7b9300108015611e615750635a84cd804211155b15611e70576108fc9050611f5a565b42635a84cd80108015611e875750635a8e08004211155b15611e96576108989050611f5a565b42635a8e0800108015611ead5750635a9742804211155b15611ebc576108349050611f5a565b42635a974280108015611ed35750635aa07d004211155b15611ee2576107d09050611f5a565b42635aa07d00108015611ef95750635aa9b7804211155b15611f085761076c9050611f5a565b42635aa9b780108015611f1f5750635ab2f2004211155b15611f2e576107089050611f5a565b42635ab2f200108015611f455750635abc2c804211155b15611f54576106a49050611f5a565b60045490505b90565b60045481565b600360009054906101000a900460ff161515611f7e57600080fd5b611f89338383612798565b5050565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff16141515611fe957600080fd5b600360009054906101000a900460ff1615600360006101000a81548160ff021916908315150217905550565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561207157600080fd5b61207c338383612798565b5050565b6000600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156120de57600080fd5b600360048111156120eb57fe5b600560009054906101000a900460ff16600481111561210657fe5b14151561211257600080fd5b600c60008373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205490506000811415151561216457600080fd5b6000600c60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550806006600082825403925050819055508173ffffffffffffffffffffffffffffffffffffffff167f38d762ef507761291a578e921acfe29c1af31a7331ea03e391cf16cfc4d4f581826040518082815260200191505060405180910390a2600a60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168273ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a360006006541415612304576004600560006101000a81548160ff021916908360048111156122b857fe5b02179055507f8d9efa3fab1bd6476defa44f520afbf9337886a4947021fd7f2775e0efaf45716004604051808260048111156122f057fe5b60ff16815260200191505060405180910390a15b5050565b600080600360009054906101000a900460ff16151561232657600080fd5b8490506123338585610e6c565b15612498578073ffffffffffffffffffffffffffffffffffffffff16638f4ffcb1338630876040518563ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018481526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200180602001828103825283818151815260200191508051906020019080838360005b8381101561242d578082015181840152602081019050612412565b50505050905090810190601f16801561245a5780820380516001836020036101000a031916815260200191505b5095505050505050600060405180830381600087803b151561247b57600080fd5b6102c65a03f1151561248c57600080fd5b50505060019150612499565b5b509392505050565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156124fd57600080fd5b6125078282612ac7565b5050565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561256757600080fd5b8060048190555050565b600860009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff161415156125cd57600080fd5b60008173ffffffffffffffffffffffffffffffffffffffff16141515156125f357600080fd5b80600760006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600d602052816000526040600020602052806000526040600020600091509150505481565b600b60009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b61268c3333612ac7565b565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1681565b600760009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff163373ffffffffffffffffffffffffffffffffffffffff1614151561271057600080fd5b60008173ffffffffffffffffffffffffffffffffffffffff161415151561273657600080fd5b80600b60006101000a81548173ffffffffffffffffffffffffffffffffffffffff021916908373ffffffffffffffffffffffffffffffffffffffff16021790555050565b600080828401905083811015151561278e57fe5b8091505092915050565b6000808373ffffffffffffffffffffffffffffffffffffffff16141515156127bf57600080fd5b81600c60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020541015151561280d57600080fd5b600c60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205482600c60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020540111151561289b57600080fd5b600c60008473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600c60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401905081600c60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000206000828254039250508190555081600c60008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001908152602001600020600082825401925050819055508273ffffffffffffffffffffffffffffffffffffffff168473ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef846040518082815260200191505060405180910390a380600c60008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002054600c60008773ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205401141515612aa857fe5b50505050565b6000828211151515612abc57fe5b818303905092915050565b600080600960009054906101000a900473ffffffffffffffffffffffffffffffffffffffff1691508173ffffffffffffffffffffffffffffffffffffffff166370a08231856000604051602001526040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050602060405180830381600087803b1515612b9257600080fd5b6102c65a03f11515612ba357600080fd5b505050604051805190509050600081111515612bbe57600080fd5b8173ffffffffffffffffffffffffffffffffffffffff1663b237f7d4856040518263ffffffff167c0100000000000000000000000000000000000000000000000000000000028152600401808273ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001915050600060405180830381600087803b1515612c5857600080fd5b6102c65a03f11515612c6957600080fd5b505050612cbe81600c60008673ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020019081526020016000205461277a90919063ffffffff16565b600c60008573ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff16815260200190815260200160002081905550612d168160065461277a90919063ffffffff16565b6006819055507ff0fee1f70845d356d6a3e0baa0944ce846437b6469ea89416dad2cd7067919a4848483604051808473ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff1681526020018373ffffffffffffffffffffffffffffffffffffffff1673ffffffffffffffffffffffffffffffffffffffff168152602001828152602001935050505060405180910390a18273ffffffffffffffffffffffffffffffffffffffff163073ffffffffffffffffffffffffffffffffffffffff167fddf252ad1be2c89b69c2b068fc378daa952ba7f163c4a11628f55a4df523b3ef836040518082815260200191505060405180910390a3505050505600a165627a7a723058204de28bdd2bb77832c4628d306defc38b5d2b0b6d80372994619d3a2112e9b3a30029000000000000000000000000ca293800147768bece42669bb29995f1b238d4560000000000000000000000004e237f139582708a592a14034b3c1a5b38da45a60000000000000000000000008f2ecccc42ed88348ad39a1985188dc57d75bdf0";

    protected NAC(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected NAC(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public List<LogBuyEventResponse> getLogBuyEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LogBuy", 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LogBuyEventResponse> responses = new ArrayList<LogBuyEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogBuyEventResponse typedResponse = new LogBuyEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogBuyEventResponse> logBuyEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LogBuy",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LogBuyEventResponse>() {
            @Override
            public LogBuyEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LogBuyEventResponse typedResponse = new LogBuyEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<LogBurnEventResponse> getLogBurnEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LogBurn",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LogBurnEventResponse> responses = new ArrayList<LogBurnEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogBurnEventResponse typedResponse = new LogBurnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogBurnEventResponse> logBurnEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LogBurn",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LogBurnEventResponse>() {
            @Override
            public LogBurnEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LogBurnEventResponse typedResponse = new LogBurnEventResponse();
                typedResponse.log = log;
                typedResponse.owner = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<LogPhaseSwitchEventResponse> getLogPhaseSwitchEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LogPhaseSwitch",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LogPhaseSwitchEventResponse> responses = new ArrayList<LogPhaseSwitchEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogPhaseSwitchEventResponse typedResponse = new LogPhaseSwitchEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse.newPhase = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogPhaseSwitchEventResponse> logPhaseSwitchEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LogPhaseSwitch",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LogPhaseSwitchEventResponse>() {
            @Override
            public LogPhaseSwitchEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LogPhaseSwitchEventResponse typedResponse = new LogPhaseSwitchEventResponse();
                typedResponse.log = log;
                typedResponse.newPhase = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<LogMigrateEventResponse> getLogMigrateEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("LogMigrate",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<LogMigrateEventResponse> responses = new ArrayList<LogMigrateEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            LogMigrateEventResponse typedResponse = new LogMigrateEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getNonIndexedValues().get(1).getValue();
            typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<LogMigrateEventResponse> logMigrateEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("LogMigrate",
                Arrays.<TypeReference<?>>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, LogMigrateEventResponse>() {
            @Override
            public LogMigrateEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                LogMigrateEventResponse typedResponse = new LogMigrateEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getNonIndexedValues().get(1).getValue();
                typedResponse.amount = (BigInteger) eventValues.getNonIndexedValues().get(2).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
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

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferEventResponse>() {
            @Override
            public TransferEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferEventResponse typedResponse = new TransferEventResponse();
                typedResponse.log = log;
                typedResponse.from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse.to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse.value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferToBuyerEventResponse> getTransferToBuyerEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TransferToBuyer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferToBuyerEventResponse> responses = new ArrayList<TransferToBuyerEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferToBuyerEventResponse typedResponse = new TransferToBuyerEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._seller = (String) eventValues.getIndexedValues().get(2).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferToBuyerEventResponse> transferToBuyerEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TransferToBuyer",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferToBuyerEventResponse>() {
            @Override
            public TransferToBuyerEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferToBuyerEventResponse typedResponse = new TransferToBuyerEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._seller = (String) eventValues.getIndexedValues().get(2).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                return typedResponse;
            }
        });
    }

    public List<TransferToExchangeEventResponse> getTransferToExchangeEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("TransferToExchange",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        List<EventValuesWithLog> valueList = extractEventParametersWithLog(event, transactionReceipt);
        ArrayList<TransferToExchangeEventResponse> responses = new ArrayList<TransferToExchangeEventResponse>(valueList.size());
        for (EventValuesWithLog eventValues : valueList) {
            TransferToExchangeEventResponse typedResponse = new TransferToExchangeEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
            typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
            typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
            typedResponse._price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
            responses.add(typedResponse);
        }
        return responses;
    }

    public Observable<TransferToExchangeEventResponse> transferToExchangeEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("TransferToExchange",
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Address>() {}),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}, new TypeReference<Uint256>() {}));
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(new Func1<Log, TransferToExchangeEventResponse>() {
            @Override
            public TransferToExchangeEventResponse call(Log log) {
                EventValuesWithLog eventValues = extractEventParametersWithLog(event, log);
                TransferToExchangeEventResponse typedResponse = new TransferToExchangeEventResponse();
                typedResponse.log = log;
                typedResponse._from = (String) eventValues.getIndexedValues().get(0).getValue();
                typedResponse._to = (String) eventValues.getIndexedValues().get(1).getValue();
                typedResponse._value = (BigInteger) eventValues.getNonIndexedValues().get(0).getValue();
                typedResponse._price = (BigInteger) eventValues.getNonIndexedValues().get(1).getValue();
                return typedResponse;
            }
        });
    }

    public RemoteCall<String> namiMultiSigWallet() {
        final Function function = new Function("namiMultiSigWallet",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> currentPhase() {
        final Function function = new Function("currentPhase",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint8>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
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

    public RemoteCall<BigInteger> totalSupply() {
        final Function function = new Function("totalSupply",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<Boolean> TRANSFERABLE() {
        final Function function = new Function("TRANSFERABLE",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Bool>() {}));
        return executeRemoteCallSingleValueReturn(function, Boolean.class);
    }

    public RemoteCall<TransactionReceipt> setPresalePhase(BigInteger _nextPhase) {
        final Function function = new Function(
                "setPresalePhase",
                Arrays.<Type>asList(new Uint8(_nextPhase)),
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

    public RemoteCall<BigInteger> TOKEN_SUPPLY_LIMIT() {
        final Function function = new Function("TOKEN_SUPPLY_LIMIT",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> decimals() {
        final Function function = new Function("decimals",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> crowdsaleManager() {
        final Function function = new Function("crowdsaleManager",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> withdrawEther(BigInteger _amount) {
        final Function function = new Function(
                "withdrawEther",
                Arrays.<Type>asList(new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> setCrowdsaleManager(String _mgr) {
        final Function function = new Function(
                "setCrowdsaleManager",
                Arrays.<Type>asList(new Address(_mgr)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> safeWithdraw(String _withdraw, BigInteger _amount) {
        final Function function = new Function(
                "safeWithdraw",
                Arrays.<Type>asList(new Address(_withdraw),
                new Uint256(_amount)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> balanceOf(String param0) {
        final Function function = new Function("balanceOf",
                Arrays.<Type>asList(new Address(param0)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferToBuyer(String _to, BigInteger _value, String _buyer) {
        final Function function = new Function(
                "transferToBuyer",
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value),
                new Address(_buyer)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferToExchange(String _to, BigInteger _value, BigInteger _price) {
        final Function function = new Function(
                "transferToExchange",
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value),
                new Uint256(_price)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> namiPresale() {
        final Function function = new Function("namiPresale",
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

    public RemoteCall<BigInteger> getPrice() {
        final Function function = new Function("getPrice",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<BigInteger> binary() {
        final Function function = new Function("binary",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        final Function function = new Function(
                "transfer",
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeTransferable() {
        final Function function = new Function(
                "changeTransferable",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> transferForTeam(String _to, BigInteger _value) {
        final Function function = new Function(
                "transferForTeam",
                Arrays.<Type>asList(new Address(_to),
                new Uint256(_value)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> burnTokens(String _owner) {
        final Function function = new Function(
                "burnTokens",
                Arrays.<Type>asList(new Address(_owner)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        final Function function = new Function(
                "approveAndCall",
                Arrays.<Type>asList(new Address(_spender),
                new Uint256(_value),
                new DynamicBytes(_extraData)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> migrateToken(String _from, String _to) {
        final Function function = new Function(
                "migrateToken",
                Arrays.<Type>asList(new Address(_from),
                new Address(_to)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeBinary(BigInteger _binary) {
        final Function function = new Function(
                "changeBinary",
                Arrays.<Type>asList(new Uint256(_binary)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> changeEscrow(String _escrow) {
        final Function function = new Function(
                "changeEscrow",
                Arrays.<Type>asList(new Address(_escrow)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String param0, String param1) {
        final Function function = new Function("allowance",
                Arrays.<Type>asList(new Address(param0),
                new Address(param1)),
                Arrays.<TypeReference<?>>asList(new TypeReference<Uint256>() {}));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> binaryAddress() {
        final Function function = new Function("binaryAddress",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> migrateForInvestor() {
        final Function function = new Function(
                "migrateForInvestor",
                Arrays.<Type>asList(),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<String> escrow() {
        final Function function = new Function("escrow",
                Arrays.<Type>asList(),
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> buy(String _buyer, BigInteger weiValue) {
        final Function function = new Function(
                "buy",
                Arrays.<Type>asList(new Address(_buyer)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function, weiValue);
    }

    public RemoteCall<TransactionReceipt> changeBinaryAddress(String _binaryAddress) {
        final Function function = new Function(
                "changeBinaryAddress",
                Arrays.<Type>asList(new Address(_binaryAddress)),
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public static RemoteCall<NAC> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, String _escrow, String _namiMultiSigWallet, String _namiPresale) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_escrow),
                new Address(_namiMultiSigWallet),
                new Address(_namiPresale)));
        return deployRemoteCall(NAC.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static RemoteCall<NAC> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, String _escrow, String _namiMultiSigWallet, String _namiPresale) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(new Address(_escrow),
                new Address(_namiMultiSigWallet),
                new Address(_namiPresale)));
        return deployRemoteCall(NAC.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static NAC load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new NAC(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public static NAC load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new NAC(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static class LogBuyEventResponse {
        public Log log;

        public String owner;

        public BigInteger value;
    }

    public static class LogBurnEventResponse {
        public Log log;

        public String owner;

        public BigInteger value;
    }

    public static class LogPhaseSwitchEventResponse {
        public Log log;

        public BigInteger newPhase;
    }

    public static class LogMigrateEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger amount;
    }

    public static class TransferEventResponse {
        public Log log;

        public String from;

        public String to;

        public BigInteger value;
    }

    public static class TransferToBuyerEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public String _seller;

        public BigInteger _value;
    }

    public static class TransferToExchangeEventResponse {
        public Log log;

        public String _from;

        public String _to;

        public BigInteger _value;

        public BigInteger _price;
    }
}
