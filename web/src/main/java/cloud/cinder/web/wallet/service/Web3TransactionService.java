package cloud.cinder.web.wallet.service;

import cloud.cinder.web.wallet.controller.command.confirm.ConfirmEtherTransactionCommand;
import cloud.cinder.web.wallet.controller.command.confirm.ConfirmTokenTransactionCommand;
import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.datatypes.Function;
import org.web3j.crypto.RawTransaction;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;

import javax.validation.constraints.NotNull;
import java.math.BigInteger;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;

@Service
@Slf4j
public class Web3TransactionService {

    private AuthenticationService authenticationService;
    private Web3jGateway web3jGateway;

    public Web3TransactionService(final AuthenticationService authenticationService,
                                  final Web3jGateway web3j,
                                  final ApplicationEventPublisher applicationEventPublisher) {
        this.authenticationService = authenticationService;
        this.web3jGateway = web3j;
    }

    public String submitEtherTransaction(final ConfirmEtherTransactionCommand etherTransactionCommand) {
        final String address = authenticationService.getAddress();
        final BigInteger balance = web3jGateway.web3j().ethGetBalance(address, DefaultBlockParameterName.LATEST)
                .observable()
                .toBlocking()
                .first()
                .getBalance();
        if (hasEnoughBalance(balance, etherTransactionCommand)) {
            final EthGetTransactionCount ethGetTransactionCount = calculateNonce(address);
            if (ethGetTransactionCount != null) {
                return generateAndSendEther(etherTransactionCommand, ethGetTransactionCount);
            } else {
                throw new IllegalArgumentException("Error trying to submit a transaction, please try again later");
            }
        } else {
            throw new IllegalArgumentException("Your balance isn't high enough to submit this transaction.");
        }
    }

    public String submitTokenTransaction(final ConfirmTokenTransactionCommand command) {
        final String owner = authenticationService.getAddress();
        final BigInteger balance = web3jGateway.web3j().ethGetBalance(owner, DefaultBlockParameterName.LATEST)
                .observable()
                .toBlocking()
                .first()
                .getBalance();
        if (hasEnoughBalance(balance, command)) {
            final EthGetTransactionCount ethGetTransactionCount = calculateNonce(owner);
            if (ethGetTransactionCount != null) {
                return generateAndSendToken(command, ethGetTransactionCount, owner);
            } else {
                throw new IllegalArgumentException("Error trying to submit a transaction, please try again later");
            }
        } else {
            throw new IllegalArgumentException("Your balance isn't high enough to submit this transaction.");
        }
    }

    private String generateAndSendEther(final ConfirmEtherTransactionCommand etherTransactionCommand, final EthGetTransactionCount ethGetTransactionCount) {
        final RawTransaction etherTransaction = generateEtherTransaction(ethGetTransactionCount, etherTransactionCommand);

        final byte[] signedMessage = sign(etherTransaction);
        final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
        try {
            final EthSendTransaction send = web3jGateway.web3j().ethSendRawTransaction(signedMessageAsHex).sendAsync().get();
            log.debug("txHash: {}", send.getTransactionHash());
            if (send.getTransactionHash() != null) {
                return send.getTransactionHash();
            } else {
                log.error(send.getError().getMessage());
                throw new IllegalArgumentException(send.getError().getMessage());
            }
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Error trying to submit a transaction, please try again later.");
        }
    }

    private String generateAndSendToken(final ConfirmTokenTransactionCommand command,
                                        final EthGetTransactionCount ethGetTransactionCount,
                                        final String owner) {
        try {
            final HumanStandardToken token = HumanStandardToken.load(command.getTokenAddress(), web3jGateway.web3j());
            final BigInteger balance = token.balanceOf(owner).send();
            if (!hasEnoughTokens(balance, command)) {
                log.debug("User did not have any tokens");
                throw new IllegalArgumentException("You do not have enough tokens to send.");
            } else {

                final RawTransaction transaction = generateTokenTransaction(command, ethGetTransactionCount);

                final byte[] signedMessage = sign(transaction);
                final String signedMessageAsHex = prettify(Hex.toHexString(signedMessage));
                final EthSendTransaction send = web3jGateway.web3j().ethSendRawTransaction(signedMessageAsHex).send();
                log.debug("txHash: {}", send.getTransactionHash());
                if (send.getTransactionHash() != null) {
                    return send.getTransactionHash();
                } else {
                    log.error(send.getError().getMessage());
                    throw new IllegalArgumentException(send.getError().getMessage());
                }
            }
        } catch (final Exception ex) {
            log.error("unable to send tokens", ex);
            throw new IllegalArgumentException(ex.getMessage());
        }
    }

    @NotNull
    private RawTransaction generateTokenTransaction(final ConfirmTokenTransactionCommand command, final EthGetTransactionCount ethGetTransactionCount) {
        final Function function = new Function(
                "transfer",
                asList(new org.web3j.abi.datatypes.Address(command.getTo()),
                        new org.web3j.abi.datatypes.generated.Uint256(command.getAmountInWei())),
                emptyList());
        final String encodedFunction = FunctionEncoder.encode(function);
        return RawTransaction.createTransaction(
                ethGetTransactionCount.getTransactionCount(),
                command.getGasPriceInWei(),
                command.getGasLimit(),
                command.getTokenAddress(), encodedFunction);
    }

    private byte[] sign(final RawTransaction etherTransaction) {
        return authenticationService.sign(etherTransaction);
    }

    private RawTransaction generateEtherTransaction(final EthGetTransactionCount transactionCount, final ConfirmEtherTransactionCommand etherTransactionCommand) {
        return RawTransaction.createEtherTransaction(
                transactionCount.getTransactionCount(),
                etherTransactionCommand.getGasPriceInWei(),
                etherTransactionCommand.getGasLimit(),
                etherTransactionCommand.getTo(),
                etherTransactionCommand.getAmountInWei()
        );
    }

    private boolean hasEnoughBalance(final BigInteger balance, final ConfirmEtherTransactionCommand etherTransactionCommand) {
        final BigInteger totalGas = etherTransactionCommand.getGasPriceInWei().multiply(etherTransactionCommand.getGasLimit());
        final BigInteger totalPrice = etherTransactionCommand.getAmountInWei().add(totalGas);
        return (balance.compareTo(totalPrice) >= 0);
    }


    private boolean hasEnoughBalance(final BigInteger balance, final ConfirmTokenTransactionCommand etherTransactionCommand) {
        final BigInteger totalGas = etherTransactionCommand.getGasPriceInWei().multiply(etherTransactionCommand.getGasLimit());
        return (balance.compareTo(totalGas) >= 0);
    }

    private boolean hasEnoughTokens(final BigInteger balance, final ConfirmTokenTransactionCommand etherTransactionCommand) {
        return (balance.compareTo(etherTransactionCommand.getAmountInWei()) >= 0);
    }


    private EthGetTransactionCount calculateNonce(final String address) {
        return web3jGateway.web3j().ethGetTransactionCount(
                prettify(address),
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
