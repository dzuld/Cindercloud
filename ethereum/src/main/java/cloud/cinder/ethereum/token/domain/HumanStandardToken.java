package cloud.cinder.ethereum.token.domain;

import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.generated.Uint8;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import rx.Observable;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;

public final class HumanStandardToken extends Contract {
    private static final Credentials DUMMY = Credentials.create("0x0");

    private HumanStandardToken(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super("", contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    public List<TransferEventResponse> getTransferEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Transfer",
                asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                singletonList(new TypeReference<Uint256>() {
                }));
        final List<EventValues> valueList = extractEventParameters(event, transactionReceipt);
        return valueList.stream()
                .map(eventValues -> new TransferEventResponse(
                        (String) eventValues.getIndexedValues().get(0).getValue(),
                        (String) eventValues.getIndexedValues().get(1).getValue(),
                        (BigInteger) eventValues.getNonIndexedValues().get(0).getValue()
                )).collect(Collectors.toList());
    }

    public Observable<TransferEventResponse> transferEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Transfer",
                asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                singletonList(new TypeReference<Uint256>() {
                }));
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(log -> {
            final EventValues eventValues = extractEventParameters(event, log);
            return new TransferEventResponse(
                    (String) eventValues.getIndexedValues().get(0).getValue(),
                    (String) eventValues.getIndexedValues().get(1).getValue(),
                    (BigInteger) eventValues.getNonIndexedValues().get(0).getValue()
            );
        });
    }

    public List<ApprovalEventResponse> getApprovalEvents(TransactionReceipt transactionReceipt) {
        final Event event = new Event("Approval",
                asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                singletonList(new TypeReference<Uint256>() {
                }));
        return extractEventParameters(event, transactionReceipt).stream()
                .map(eventValues -> new ApprovalEventResponse(
                        (String) eventValues.getIndexedValues().get(0).getValue(),
                        (String) eventValues.getIndexedValues().get(1).getValue(),
                        (BigInteger) eventValues.getNonIndexedValues().get(0).getValue()
                )).collect(Collectors.toList());
    }

    public Observable<ApprovalEventResponse> approvalEventObservable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        final Event event = new Event("Approval",
                asList(new TypeReference<Address>() {
                }, new TypeReference<Address>() {
                }),
                singletonList(new TypeReference<Uint256>() {
                }));
        final EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(event));
        return web3j.ethLogObservable(filter).map(log -> {
            final EventValues eventValues = extractEventParameters(event, log);
            return new ApprovalEventResponse(
                    (String) eventValues.getIndexedValues().get(0).getValue(),
                    (String) eventValues.getIndexedValues().get(1).getValue(),
                    (BigInteger) eventValues.getNonIndexedValues().get(0).getValue()
            );
        });
    }

    public RemoteCall<String> name() {
        Function function = new Function("name",
                emptyList(),
                singletonList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> approve(String _spender, BigInteger _value) {
        Function function = new Function(
                "approve",
                asList(new org.web3j.abi.datatypes.Address(_spender),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> totalSupply() {
        Function function = new Function("totalSupply",
                emptyList(),
                singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<TransactionReceipt> transferFrom(String _from, String _to, BigInteger _value) {
        Function function = new Function(
                "transferFrom",
                asList(new org.web3j.abi.datatypes.Address(_from),
                        new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> decimals() {
        Function function = new Function("decimals",
                emptyList(),
                singletonList(new TypeReference<Uint8>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> version() {
        Function function = new Function("version",
                emptyList(),
                singletonList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<BigInteger> balanceOf(String _owner) {
        Function function = new Function("balanceOf",
                singletonList(new Address(_owner)),
                singletonList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public RemoteCall<String> symbol() {
        Function function = new Function("symbol",
                emptyList(),
                singletonList(new TypeReference<Utf8String>() {
                }));
        return executeRemoteCallSingleValueReturn(function, String.class);
    }

    public RemoteCall<TransactionReceipt> transfer(String _to, BigInteger _value) {
        Function function = new Function(
                "transfer",
                asList(new org.web3j.abi.datatypes.Address(_to),
                        new org.web3j.abi.datatypes.generated.Uint256(_value)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<TransactionReceipt> approveAndCall(String _spender, BigInteger _value, byte[] _extraData) {
        Function function = new Function(
                "approveAndCall",
                asList(new org.web3j.abi.datatypes.Address(_spender),
                        new org.web3j.abi.datatypes.generated.Uint256(_value),
                        new org.web3j.abi.datatypes.DynamicBytes(_extraData)),
                emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<BigInteger> allowance(String _owner, String _spender) {
        Function function = new Function("allowance",
                asList(new org.web3j.abi.datatypes.Address(_owner),
                        new org.web3j.abi.datatypes.Address(_spender)),
                asList(new TypeReference<Uint256>() {
                }));
        return executeRemoteCallSingleValueReturn(function, BigInteger.class);
    }

    public static HumanStandardToken load(final String contractAddress, final Web3j web3j) {
        return new HumanStandardToken(prettifyAddress(contractAddress), web3j, DUMMY, BigInteger.ZERO, BigInteger.ZERO);
    }

}