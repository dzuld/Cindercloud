package cloud.cinder.web.whitehat;

import cloud.cinder.web.whitehat.tokens.Token;
import cloud.cinder.web.whitehat.tokens.TokenTransferResult;
import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.bouncycastle.util.encoders.Hex;
import org.junit.Before;
import org.junit.Test;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.*;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.*;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Numeric;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

public class PrivateTester {


    private Web3j web3j;
    private OkHttpClient httpClient;

    private List<Web3j> web3js = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(5, TimeUnit.MINUTES);
        builder.writeTimeout(5, TimeUnit.MINUTES);
        builder.readTimeout(5, TimeUnit.MINUTES);
        httpClient = builder.build();
        HttpService quicknodeService = new HttpService("https://freely-central-lark.quiknode.io/9fe4c4a0-2ea2-4ac1-ab64-f92990cd2914/118-xxADc8hKSSB9joCb-g==/", httpClient, false);
        HttpService fundRequestService = new HttpService("https://fullnode.fundrequest.io", httpClient, false);
        HttpService infuraService = new HttpService("https://mainnet.infura.io", httpClient, false);
        web3j = Web3j.build(fundRequestService, 5L, new ScheduledThreadPoolExecutor(5));

        web3js.add(Web3j.build(fundRequestService, 5L, new ScheduledThreadPoolExecutor(5)));
        web3js.add(Web3j.build(infuraService, 5L, new ScheduledThreadPoolExecutor(5)));
        web3js.add(Web3j.build(quicknodeService, 5L, new ScheduledThreadPoolExecutor(5)));
    }

    @Test
    public void tokenTransfers() throws IOException {
        final DefaultBlockParameter from = DefaultBlockParameterName.EARLIEST;
        final DefaultBlockParameter to = DefaultBlockParameterName.LATEST;
        final EthFilter ethFilter = new EthFilter(from, to, "0x4156d3342d5c385a87d264f90653733592000581");
        ethFilter.addSingleTopic(EventEncoder.encode(TRANSFER_EVENT));
        ethFilter.addOptionalTopics(prettifyAddress(TypeEncoder.encode(new Address("0xb7bd981cac9f087177fe90fc4d6439d3f2782061"))));
        EthLog send = web3j.ethGetLogs(ethFilter).send();
        send.getLogs().forEach(x -> {
            final Log log = (Log) x.get();
            System.out.println(log.getTransactionHash());
        });
    }

    private static final Event TRANSFER_EVENT = new Event("Transfer",
            Arrays.asList(
                    new TypeReference<Address>() {
                    },
                    new TypeReference<Address>() {
                    }
            ),
            Arrays.asList(
                    new TypeReference<Uint256>() {
                    })
    );

    @Test
    public void reindex() throws Exception {
        try (BufferedReader br = new BufferedReader(new FileReader(new File("./src/test/resources/whitehat/to-change.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    final String txId = line.replace("\uFEFF", "").trim();

                    Call call = httpClient.newCall(new Request.Builder().url(String.format("https://cinder.cloud/rest/tx/%s/reindex", txId)).build());
                    Response execute = call.execute();
                    System.out.println(execute.body().string());
                } catch (final Exception ex) {
                    System.out.println(ex.getMessage());
                }
            }
        }
    }

    @Test
    public void name() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //next: "0x434e9f6a1ac134fda3e7ceb3fd67c3d9b3518737"
        final String x = "0xC257274276a4E539741Ca11b590B9447B26A8051";
        //next:  0x957cd4ff9b3894fc78b5134a8dc72b032ffbc464
        Call call = httpClient.newCall(new Request.Builder().url(String.format("http://api.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=asc&apikey=HPK1JKX6586DVIIYGGAPRPNAD9I9Z3Q8PS", x)).build());
        Response execute = call.execute();
        String string = execute.body().string();
        CrowdsaleResult $ = objectMapper.readValue(string, CrowdsaleResult.class);

        $.getResult()
                .stream()
                .map(z -> {
                    if (z.getFrom().equals(x)) {
                        return z.getTo();
                    } else {
                        return z.getFrom();
                    }
                })
                .forEach(y -> {
                    try {
                        getFromsOrTos(objectMapper, y);
                    } catch (Exception e) {
                        System.out.println("error");
                    }
                });
    }

    private void getFromsOrTos(final ObjectMapper objectMapper, final String origin) throws IOException {
        try {
            final Call call = httpClient.newCall(new Request.Builder().url(String.format("http://api.etherscan.io/api?module=account&action=txlist&address=%s&startblock=0&endblock=99999999&sort=asc&apikey=HPK1JKX6586DVIIYGGAPRPNAD9I9Z3Q8PS", origin)).build());
            Response execute = call.execute();
            String string = execute.body().string();
            CrowdsaleResult crowdsaleResult = objectMapper.readValue(string, CrowdsaleResult.class);
            checkInputs(crowdsaleResult);
        } catch (Exception ex) {
            getFromsOrTos(objectMapper, origin);
        }
    }

    private void checkInputs(final CrowdsaleResult crowdsaleResult) {
        crowdsaleResult.getResult()
                .stream()
                .filter(r -> r.getInput() != null && r.getInput().length() == 66)
                .map(CrowdsaleResult.Transaction::getInput)
                .forEach(privatekey -> System.out.println(String.format("%s", privatekey)));
    }

    public void crowdsaleTester() {
        web3j.ethGetLogs(
                new EthFilter(DefaultBlockParameterName.EARLIEST, DefaultBlockParameterName.LATEST, "0xd0a6e6c54dbc68db5db3a091b171a77407ff7ccf")
        ).observable()
                .subscribe(x -> {
                    x.getLogs()
                            .forEach(y -> {
                                EthLog.LogObject logObject = (EthLog.LogObject) y;
                                String transactionHash = logObject.get().getTransactionHash();
                                System.out.println(transactionHash);
                            });
                });
    }

    @Test
    public void balances() throws Exception {
        final Set<String> keys = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("./src/test/resources/whitehat/privates_to_test.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                keys.add(line.replace("\uFEFF", "").trim());
            }
        }

        keys.parallelStream()
                .forEach(key -> {
                    try {
                        // process the line.
                        final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(key));
                        final String address = Keys.getAddress(keypair);
                        printTxCount(web3j.ethGetTransactionCount("0x" + address, DefaultBlockParameterName.LATEST).observable().toBlocking().first(), address, key);
                        printBalance(web3j.ethGetBalance("0x" + address, DefaultBlockParameterName.LATEST).observable().toBlocking().first(), address, key);
                    } catch (final Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                });
    }

    public static <T> Predicate<T> distinctByKey(java.util.function.Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

    private void printTokens(final ECKeyPair keypair, Stream<Token> tokenResult) {
        tokenResult
                .filter(distinctByKey(Token::getAddress))
                .forEach(x -> {
                    HumanStandardToken token = HumanStandardToken.load(x.getAddress(), web3j);

                    try {
                        BigInteger balance = token.balanceOf(Keys.getAddress(keypair)).send();
                        if (!balance.equals(BigInteger.ZERO)) {
                            System.out.println(balance.longValue() + " " + x.getSymbol() + " were found @ " + Keys.getAddress(keypair));
                        }
                    } catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                });

    }

    @Test
    public void tokens() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        final Set<String> keys = new HashSet<>();
        try (BufferedReader br = new BufferedReader(new FileReader(new File("./src/test/resources/whitehat/privates_to_test.txt")))) {
            String line;
            while ((line = br.readLine()) != null) {
                keys.add(line.replace("\uFEFF", "").trim());
            }
        }


        keys.parallelStream()
                .forEach(key -> {
                    try {
                        final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(key));
                        Call call = httpClient.newCall(new Request.Builder().url(String.format("http://api.etherscan.io/api?module=account&action=tokentx&address=%s&startblock=0&endblock=999999999&sort=asc&apikey=HPK1JKX6586DVIIYGGAPRPNAD9I9Z3Q8PS", prettify(Keys.getAddress(keypair)))).build());
                        Response execute = call.execute();
                        String string = execute.body().string();
                        TokenTransferResult $ = objectMapper.readValue(string, TokenTransferResult.class);
                        if ($.getResult() != null && $.getResult().size() > 0) {
                            System.out.println("private: " + key + ", address: " + prettify(Keys.getAddress(keypair)));
                        }
                    } catch (final Exception ex) {
                    }
                });
    }

    private void printBalance(final EthGetBalance x, final String address, final String line) {
        if (x.getBalance().longValue() != 0) {
            System.out.println("private: " + line + ", address: " + address + ", balance: " + x.getBalance().longValue());
        }
    }

    private void printTxCount(final EthGetTransactionCount x, final String address, final String line) {
        if (x.getTransactionCount().longValue() != 0) {
            System.out.println("private: " + line + ", address: " + address + ", txCount: " + x.getTransactionCount().longValue());
        }
    }


    @Test
    public void minereum() throws Exception {
        while (true) {
            String owner = "0xb7605ddc0327406a7ac225b9de87865e22ac5927";
            String privateKey = "0x049bb603adbd99a8a00aeef83d2ceacc3f2958cd927f7295188ae43cbeb09df9";
            String tokenAddress = "0x1a95b271b0535d15fa49932daba31ba612b52946";

            String to = "0xc0ffee145a59d2172971cde0f0b4959d51193a80";

            BigInteger gasLimit = BigInteger.valueOf(200000);
            BigInteger gasPrice = BigInteger.valueOf(0);


            ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.replace("\uFEFF", "")));
            BigInteger nonce = web3j.ethGetTransactionCount(owner, DefaultBlockParameterName.LATEST).send().getTransactionCount();

            MinereumToken token = MinereumToken.load(tokenAddress, web3j, Credentials.create(keypair), BigInteger.ZERO, gasLimit);

            BigInteger balance = token.availableBalanceOf(owner).send();
            if (balance.equals(BigInteger.ZERO)) {
                System.out.println("no tokens");
            } else {
                Function function = new Function(
                        "transfer",
                        asList(new org.web3j.abi.datatypes.Address(to),
                                new org.web3j.abi.datatypes.generated.Uint256(balance)),
                        emptyList());


                String encodedFunction = FunctionEncoder.encode(function);

                RawTransaction transaction = RawTransaction.createTransaction(nonce, gasPrice,
                        gasLimit,
                        tokenAddress, encodedFunction);

                final byte[] signedMessage = sign(keypair, transaction);
                final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                sendRawTransaction(signedMessageAsHex);
            }
        }

    }

    @Test
    public void signer() throws Exception {
        while (true) {
            String owner = "0x8311804426a24495bd4306daf5f595a443a52e32";
            String privateKey = "0x290b47e468dd582ebaecaf5af6f824ae82a4b082562a8a64b33859b11d227b7d";
            String tokenAddress = "0xef68e7c694f40c8202821edf525de3782458639f";

            String to = "0xc0ffee145a59d2172971cde0f0b4959d51193a80";

            BigInteger gasLimit = BigInteger.valueOf(200000);
            BigInteger gasPrice = BigInteger.valueOf(0);


            ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.replace("\uFEFF", "")));
            BigInteger nonce = web3j.ethGetTransactionCount(owner, DefaultBlockParameterName.LATEST).send().getTransactionCount();

            HumanStandardToken token = HumanStandardToken.load(tokenAddress, web3j);

            BigInteger balance = token.balanceOf(owner).send();
            if (balance.equals(BigInteger.ZERO)) {
                System.out.println("no tokens");
            } else {
                final Function function = new Function(
                        "transfer",
                        asList(new org.web3j.abi.datatypes.Address(to),
                                new org.web3j.abi.datatypes.generated.Uint256(balance)),
                        emptyList());


                String encodedFunction = FunctionEncoder.encode(function);

                RawTransaction transaction = RawTransaction.createTransaction(nonce, gasPrice,
                        gasLimit,
                        tokenAddress, encodedFunction);

                final byte[] signedMessage = sign(keypair, transaction);
                final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                sendRawTransaction(signedMessageAsHex);
            }
        }

    }


    public void sendRawTransaction(final String signedMessageAsHex) {
        for (Web3j _web3j : web3js) {
            try {
                EthSendTransaction send = _web3j.ethSendRawTransaction(signedMessageAsHex).send();
                if (send.getError() != null) {
                    System.out.println(send.getError().getMessage());
                } else {
                    System.out.println(send.getTransactionHash());
                }
            } catch (final Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }


    private byte[] sign(final ECKeyPair keyPair, final RawTransaction etherTransaction) {
        return TransactionEncoder.signMessage(etherTransaction, Credentials.create(keyPair));
    }

    private String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }
}