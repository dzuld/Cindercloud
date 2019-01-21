package cloud.cinder.whitehat.sweeping;

import cloud.cinder.common.mail.MailService;
import cloud.cinder.ethereum.util.EthUtil;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import io.reactivex.functions.Consumer;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.exceptions.MessageDecodingException;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Objects;
import java.util.Optional;

@Component
@Slf4j
public class EthereumSweeper {

    private static final BigInteger ETHER_TRANSACTION_GAS_LIMIT = BigInteger.valueOf(21000L);

    @Autowired
    private Web3jGateway web3j;
    @Autowired
    private MailService mailService;

    @Value("${cloud.cinder.whitehat.address}")
    private String whitehatAddress;

    @Value("${cloud.cinder.whitehat.tiny-gas-price}")
    private Long tinyGasPrice;
    @Value("${cloud.cinder.whitehat.low-gas-price}")
    private Long lowGasPrice;
    @Value("${cloud.cinder.whitehat.medium-gas-price}")
    private Long mediumGasPrice;
    @Value("${cloud.cinder.whitehat.high-gas-price}")
    private Long highGasPrice;

    private BigInteger TINY_GAS_PRICE;
    private BigInteger LOW_GAS_PRICE;
    private BigInteger MEDIUM_GAS_PRICE;
    private BigInteger HIGH_GAS_PRICE;


    @PostConstruct
    public void init() {
        this.TINY_GAS_PRICE = BigInteger.valueOf(tinyGasPrice);
        this.LOW_GAS_PRICE = BigInteger.valueOf(lowGasPrice);
        this.MEDIUM_GAS_PRICE = BigInteger.valueOf(mediumGasPrice);
        this.HIGH_GAS_PRICE = BigInteger.valueOf(highGasPrice);
    }

    public void sweep(final String privateKey) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.trim()));
            final String address = Keys.getAddress(keypair);

            web3j.websocket().ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).flowable()
                    .filter(Objects::nonNull)
                    .subscribe(balanceFetched(keypair, Optional.of(BigInteger.ONE)), error -> log.error("Error occurred: {}", error.getMessage()));
        } catch (final Exception ex) {
            log.error("something went wrong while trying sweep {}: {}", privateKey, ex.getMessage());
            if (ex.getMessage().contains("timeout")) {
                sweep(privateKey);
            }
        }
    }

    private void sweepWithHigherGasPrice(final BigInteger privateKey, final Optional<BigInteger> multiplier) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(privateKey);
            final String address = Keys.getAddress(keypair);
            log.debug("going to sweep with higher gasPrice: " + multiplier.orElse(BigInteger.ONE).multiply(BigInteger.valueOf(2)));
            web3j.websocket().ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).flowable()
                    .onErrorReturn(error -> {
                        log.error("Unable to fetch balance for {}, {}", address, error.getMessage());
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .subscribe(balanceFetched(keypair, multiplier.map(x -> x.multiply(BigInteger.valueOf(2)))), error -> {
                        log.error("Error occurred while fetching balance: {}", error.getMessage());
                    });
        } catch (final Exception ex) {
            log.error("something went wrong while trying to resubmit with higher gas price {}: {}", privateKey, ex.getMessage());
        }
    }

    private BigInteger calculateOptimalGasPrice(final BigInteger balance) {
        if (balance.compareTo(HIGH_GAS_PRICE.multiply(ETHER_TRANSACTION_GAS_LIMIT)) >= 0) {
            return HIGH_GAS_PRICE;
        }
        if (balance.compareTo(MEDIUM_GAS_PRICE.multiply(ETHER_TRANSACTION_GAS_LIMIT)) >= 0) {
            return MEDIUM_GAS_PRICE;
        }
        if (balance.compareTo(LOW_GAS_PRICE.multiply(ETHER_TRANSACTION_GAS_LIMIT)) >= 0) {
            return LOW_GAS_PRICE;
        }
        if (balance.compareTo(TINY_GAS_PRICE.multiply(ETHER_TRANSACTION_GAS_LIMIT)) >= 0) {
            return TINY_GAS_PRICE;
        } else {
            return BigInteger.ZERO;
        }
    }


    private Consumer<EthGetBalance> balanceFetched(final ECKeyPair keyPair, final Optional<BigInteger> multiplier) {
        return balance -> {
            try {

                if (balance.getBalance().longValue() != 0L) {

                    final BigInteger gasPrice = calculateOptimalGasPrice(balance.getBalance()).multiply(multiplier.orElse(BigInteger.ONE));
                    if (!gasPrice.equals(BigInteger.ZERO)) {

                        log.debug("[Sweeper] {} has a balance of about {}", Keys.getAddress(keyPair), EthUtil.format(balance.getBalance()));

                        final EthGetTransactionCount transactionCount = calculateNonce(keyPair);

                        if (transactionCount != null) {
                            final RawTransaction etherTransaction = generateTransaction(balance, transactionCount, gasPrice);

                            final byte[] signedMessage = sign(keyPair, etherTransaction);
                            final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                            try {
                                handleTransactionhash(keyPair, balance, web3j.cindercloud().ethSendRawTransaction(signedMessageAsHex).sendAsync().get(), multiplier);
                            } catch (final Exception ex) {
                                log.error("Error sending transaction {}", ex.getMessage());
                            }
                            try {
                                handleTransactionhash(keyPair, balance, web3j.websocket().ethSendRawTransaction(signedMessageAsHex).sendAsync().get(), multiplier);
                            } catch (final Exception ex) {
                                log.error("Error sending transaction {}", ex.getMessage());
                            }
                        } else {
                            log.error("Noncecount returned an error for {}", Keys.getAddress(keyPair));
                        }
                    }
                }
            } catch (final MessageDecodingException ex) {
                log.error("Unable to decode {}", balance.getResult());
            }
        };
    }

    private void handleTransactionhash(final ECKeyPair keyPair, final EthGetBalance balance, final EthSendTransaction send, final Optional<BigInteger> multiplier) {
        if (send.getTransactionHash() != null) {
            log.info("txHash: {}", send.getTransactionHash());
            final String usedAddress = prettify(Keys.getAddress(keyPair));
            mailService.send("Saved funds from compromised wallet!", "Hi Admin,\nWe just saved " + EthUtil.format(balance.getBalance()).toString() + " from a compromised wallet[" + usedAddress + "]\n\n.\nKind regards,\nCindercloud");
        } else if (send.getError() != null && send.getError().getMessage() != null && (send.getError().getMessage().contains("already imported") || send.getError().getMessage().contains("known transaction"))) {
            log.debug("already imported");
        } else if (send.getError() != null && send.getError().getMessage() != null && send.getError().getMessage().contains("with same nonce")) {
            log.debug("same nonce already available");
        } else if (send.getError() != null && send.getError().getMessage() != null && send.getError().getMessage().contains("transaction underpriced")) {
            sweepWithHigherGasPrice(keyPair.getPrivateKey(), multiplier);
        } else {
            if (send.getError() != null) {
                log.debug("Unable to send: {}", send.getError().getMessage());
            }
        }
    }

    public BigInteger calculatePriority(final BigInteger balance, final BigInteger gasCost) {
        BigInteger priority = balance.divide(gasCost).divide(BigInteger.valueOf(25));
        if (priority.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        } else {
            return priority;
        }
    }

    private RawTransaction generateTransaction(final EthGetBalance balance, final EthGetTransactionCount transactionCount, final BigInteger gasPrice) {
        return RawTransaction.createEtherTransaction(
                transactionCount.getTransactionCount(),
                gasPrice,
                ETHER_TRANSACTION_GAS_LIMIT,
                whitehatAddress,
                balance.getBalance().subtract(gasPrice.multiply(ETHER_TRANSACTION_GAS_LIMIT))
        );
    }

    private byte[] sign(final ECKeyPair keyPair, final RawTransaction etherTransaction) {
        return TransactionEncoder.signMessage(etherTransaction, Credentials.create(keyPair));
    }

    private EthGetTransactionCount calculateNonce(final ECKeyPair keyPair) {
        return web3j.websocket().ethGetTransactionCount(
                prettify(Keys.getAddress(keyPair)),
                DefaultBlockParameterName.LATEST
        ).flowable().blockingFirst();
    }

    private String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }

}
