package cloud.cinder.whitehat.sweeping;

import cloud.cinder.common.mail.MailService;
import cloud.cinder.ethereum.util.EthUtil;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.crypto.*;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.utils.Numeric;
import rx.functions.Action1;

import javax.annotation.PostConstruct;
import java.math.BigInteger;
import java.util.Objects;

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

    @Value("${cloud.cinder.whitehat.gasPrice}")
    private Long gasPrice;

    private BigInteger GAS_PRICE;


    @PostConstruct
    public void init() {
        this.GAS_PRICE = BigInteger.valueOf(gasPrice);
    }


    public void sweep(final String privateKey) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.trim()));
            final String address = Keys.getAddress(keypair);

            web3j.web3j().ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).observable()
                    .filter(Objects::nonNull)
                    .subscribe(balanceFetched(keypair, GAS_PRICE));
        } catch (final Exception ex) {
            log.error("something went wrong while trying sweep {}: {}", privateKey, ex.getMessage());
            if (ex.getMessage().contains("timeout")) {
                sweep(privateKey);
            }
        }
    }

    private void sweepWithHigherGasPrice(final BigInteger privateKey, final BigInteger newGasPrice) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(privateKey);
            final String address = Keys.getAddress(keypair);

            web3j.web3j().ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).observable()
                    .filter(Objects::nonNull)
                    .subscribe(balanceFetched(keypair, newGasPrice));
        } catch (final Exception ex) {
            log.error("something went wrong while trying to resubmit with higher gas price {}: {}", privateKey, ex.getMessage());
        }
    }

    private Action1<EthGetBalance> balanceFetched(final ECKeyPair keyPair, final BigInteger gasPrice) {
        return balance -> {
            if (balance.getBalance().longValue() != 0L) {


                //if balance is more than gasCost
                final BigInteger gasCost = gasPrice.multiply(ETHER_TRANSACTION_GAS_LIMIT);
                if (balance.getBalance().compareTo(gasCost) >= 0) {

                    final BigInteger priority = calculatePriority(balance.getBalance(), gasCost);

                    final BigInteger actualGasPrice = priority.multiply(gasPrice);

                    log.trace("[Sweeper] {} has a balance of about {}", Keys.getAddress(keyPair), EthUtil.format(balance.getBalance()));

                    final EthGetTransactionCount transactionCount = calculateNonce(keyPair);

                    if (transactionCount != null) {
                        final RawTransaction etherTransaction = generateTransaction(balance, transactionCount, actualGasPrice);

                        final byte[] signedMessage = sign(keyPair, etherTransaction);
                        final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                        try {
                            final EthSendTransaction send = web3j.web3j().ethSendRawTransaction(signedMessageAsHex).sendAsync().get();
                            if (send.getTransactionHash() != null) {
                                log.info("txHash: {}", send.getTransactionHash());
                                mailService.send("Saved funds from compromised wallet!", "Hi Admin,\nWe just saved " + EthUtil.format(balance.getBalance()).toString() + " from a compromised wallet[" + prettify(Keys.getAddress(keyPair) + "].\nKind regards,\nCindercloud"));
                            } else if (send.getError() != null && send.getError().getMessage() != null && send.getError().getMessage().contains("already imported")) {
                                sweepWithHigherGasPrice(keyPair.getPrivateKey(), actualGasPrice.multiply(BigInteger.valueOf(2)));
                            } else if (send.getError() != null && send.getError().getMessage() != null && send.getError().getMessage().contains("with same nonce")) {
                                sweepWithHigherGasPrice(keyPair.getPrivateKey(), actualGasPrice.multiply(BigInteger.valueOf(12).divide(BigInteger.TEN)));
                            } else {
                                if (send.getError() != null) {
                                    log.debug("Unable to send: {}", send.getError().getMessage());
                                }
                            }
                        } catch (final Exception ex) {
                            log.error("Error sending transaction (io)");
                        }
                    } else {
                        log.error("Noncecount returned an error for {}", Keys.getAddress(keyPair));
                    }
                }
            }
        };
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
        return web3j.web3j().ethGetTransactionCount(
                prettify(Keys.getAddress(keyPair)),
                DefaultBlockParameterName.LATEST
        ).observable().toBlocking().first();
    }

    private String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
    }

}
