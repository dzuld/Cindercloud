package cloud.cinder.whitehat.sweeping;

import cloud.cinder.ethereum.erc20.service.ERC20Service;
import cloud.cinder.common.mail.MailService;
import cloud.cinder.web.token.service.TokenService;
import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
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
import java.util.Optional;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Slf4j
@Component
public class TokenSweeper {

    private static final BigInteger CONTRACT_TRANSACTION_GAS_LIMIT = BigInteger.valueOf(120000);

    private Web3jGateway web3j;
    private TokenService tokenService;
    private ERC20Service erc20Service;
    private MailService mailService;

    public TokenSweeper(final Web3jGateway web3j,
                        final TokenService tokenService,
                        final ERC20Service erc20Service,
                        final MailService mailService,
                        @Value("${cloud.cinder.whitehat.address}") final String whitehatAddress,
                        @Value("${cloud.cinder.whitehat.gasPrice}") final Long gasPrice,
                        @Value("${cloud.cinder.whitehat.tokensweeper.enabled:false}") final boolean tokenSweeperEnabled) {
        this.web3j = web3j;
        this.tokenService = tokenService;
        this.erc20Service = erc20Service;
        this.mailService = mailService;
        this.whitehatAddress = whitehatAddress;
        this.gasPrice = gasPrice;
        this.tokenSweeperEnabled = tokenSweeperEnabled;
    }


    private final String whitehatAddress;
    private final Long gasPrice;
    private final boolean tokenSweeperEnabled;

    private BigInteger GAS_PRICE;
    private BigInteger GAS_COST;

    @PostConstruct
    public void init() {
        this.GAS_PRICE = BigInteger.valueOf(gasPrice);
        this.GAS_COST = this.GAS_PRICE.multiply(CONTRACT_TRANSACTION_GAS_LIMIT);
    }

    public void sweep(final String privateKey) {
        if (tokenSweeperEnabled) {
            try {
                final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.trim()));
                final String address = Keys.getAddress(keypair);

                tokenService.findAll()
                        .forEach(token -> {
                            final BigInteger tokenBalance = erc20Service.rawBalanceOf(address, token.getAddress());
                            if (!tokenBalance.equals(BigInteger.ZERO)) {
                                web3j.web3j().ethGetBalance(prettify(address), DefaultBlockParameterName.LATEST).observable()
                                        .filter(Objects::nonNull)
                                        .subscribe(onBalanceFetched(keypair, token, tokenBalance));
                            }
                        });
            } catch (final Exception ex) {
                log.error("something went wrong while trying sweep {}: {}", privateKey, ex.getMessage());
            }
        }
    }


    private Action1<EthGetBalance> onBalanceFetched(final ECKeyPair keyPair, final Token token, final BigInteger tokenBalance) {
        return ethBalance -> {
            if (!ethBalance.getBalance().equals(BigInteger.ZERO)) {
                if (ethBalance.getBalance().compareTo(GAS_COST) >= 0) {
                    sendTokensToWhitehat(keyPair, token, tokenBalance, Optional.empty());
                } else {
                    sendTokensToWhitehat(keyPair, token, tokenBalance, Optional.of(BigInteger.ZERO));
                    log.info("[TOKENSWEEP] It would appear about " + tokenBalance + " " + token.getSymbol() + "  from a compromised wallet[" + prettify(Keys.getAddress(keyPair) + "] are at risk but autosweeping was not possible due to gas constraints."));
                }
            }
        };
    }

    private String constructTransferFunction(final BigInteger tokenBalance) {
        final Function function = new Function(
                "transfer",
                asList(new org.web3j.abi.datatypes.Address(whitehatAddress),
                        new org.web3j.abi.datatypes.generated.Uint256(tokenBalance)),
                emptyList());
        return FunctionEncoder.encode(function);
    }

    private void sendTokensToWhitehat(final ECKeyPair keyPair, Token token, final BigInteger tokenBalance, final Optional<BigInteger> gasPrice) {

        final EthGetTransactionCount transactionCount = calculateNonce(keyPair);

        if (transactionCount != null) {

            final String encodedFunction = constructTransferFunction(tokenBalance);

            final RawTransaction transaction = RawTransaction.createTransaction(transactionCount.getTransactionCount(),
                    gasPrice.orElse(GAS_PRICE),
                    CONTRACT_TRANSACTION_GAS_LIMIT,
                    token.getAddress(), encodedFunction);

            final byte[] signedMessage = sign(keyPair, transaction);
            final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
            try {
                final EthSendTransaction send = web3j.web3j().ethSendRawTransaction(signedMessageAsHex).sendAsync().get();
                log.debug("txHash: {}", send.getTransactionHash());
                if (send.getTransactionHash() != null) {
                    mailService.send("Saved Tokens (" + token.getSymbol() + ") from compromised wallet!", "Hi Admin,\nWe just saved " + tokenBalance + " from a compromised wallet[" + prettify(Keys.getAddress(keyPair) + "].\nKind regards,\nCindercloud"));
                }
            } catch (final Exception ex) {
                log.error("Error sending transaction (io) {}", ex.getMessage());
            }
        } else {
            log.error("Noncecount returned an error for {}", Keys.getAddress(keyPair));
        }
    }

    private String prettify(final String address) {
        if (!address.startsWith("0x")) {
            return String.format("0x%s", address);
        } else {
            return address;
        }
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
}
