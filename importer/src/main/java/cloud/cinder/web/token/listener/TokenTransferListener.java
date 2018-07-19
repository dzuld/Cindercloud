package cloud.cinder.web.token.listener;

import cloud.cinder.web.token.listener.model.TokenEvent;
import cloud.cinder.web.token.service.TokenService;
import cloud.cinder.web.token.service.TokenTransferService;
import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.ethereum.token.domain.TokenTransfer;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.EventValues;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.EthBlock;
import org.web3j.protocol.core.methods.response.Log;
import rx.Observable;
import rx.Subscription;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
@ConditionalOnProperty(name = "cloud.cinder.ethereum.token-transfer-import", havingValue = "true")
@EnableScheduling
public class TokenTransferListener {

    private static final org.apache.commons.logging.Log LOG = LogFactory.getLog(TokenTransferListener.class);

    static final Event TRANSFER_EVENT = new Event("Transfer",
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

    private final Web3jGateway web3jGateway;
    private final TokenTransferService tokenTransferService;
    private final TokenService tokenService;

    private Subscription historicSubscription;

    public TokenTransferListener(final Web3jGateway web3jGateway, final TokenTransferService tokenTransferService, final TokenService tokenService) {
        this.web3jGateway = web3jGateway;
        this.tokenTransferService = tokenTransferService;
        this.tokenService = tokenService;
    }


    @Scheduled(fixedRate = (60000 * 20))
    private void subscribeToLive() {
        if (this.historicSubscription == null) {
            this.historicSubscription = doLiveSubscription(tokenService.findAll());
        } else {
            this.historicSubscription.unsubscribe();
            this.historicSubscription = doLiveSubscription(tokenService.findAll());
        }
    }

    private Subscription doLiveSubscription(final List<Token> tokens) {
        return live(tokens).subscribe((log) -> {
            try {
                tokenTransferService.getEventParameters(TRANSFER_EVENT, log)
                        .ifPresent(submitTokenTransfer(log));
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }, (err) -> {
            LOG.error("something went wrong while trying to listen to live token events", err);
        });
    }

    Consumer<TokenEvent> submitTokenTransfer(final Log log) {
        return tokenEvent -> {
            boolean succeeded = false;
            int attempts = 0;
            while (!succeeded && attempts < 5) {
                try {
                    final long timestamp = getTimestamp(log.getBlockHash());
                    final EventValues eventValues = tokenEvent.getEventValues();
                    final TokenTransfer tokenTransfer = TokenTransfer.builder()
                            .blockHeight(log.getBlockNumber())
                            .logIndex(log.getLogIndex().toString())
                            .blockTimestamp(Date.from(LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                            .blockHeight(log.getBlockNumber())
                            .tokenAddress(log.getAddress())
                            .removed(log.isRemoved())
                            .amount(new BigInteger(eventValues.getNonIndexedValues().get(0).getValue().toString()))
                            .fromAddress(eventValues.getIndexedValues().get(0).toString())
                            .toAddress(eventValues.getIndexedValues().get(1).getValue().toString())
                            .transactionHash(log.getTransactionHash())
                            .build();
                    tokenTransferService.saveTokenTransfer(tokenTransfer);
                    succeeded = true;
                } catch (final Exception ex) {
                    LOG.error("something went wrong while trying to save the transfer event");
                } finally {
                    attempts++;
                }
            }
        };
    }

    private long getTimestamp(final String blockHash) throws java.io.IOException {
        final EthBlock send = web3jGateway.web3j().ethGetBlockByHash(blockHash, false).send();
        if (send.getBlock() != null) {
            return send.getBlock().getTimestamp().longValue();
        } else {
            return 0;
        }
    }

    private EthFilter contractEventsFilter(final List<Token> tokens) {
        EthFilter ethFilter = new EthFilter(DefaultBlockParameterName.LATEST,
                DefaultBlockParameterName.LATEST, tokens.stream().map(Token::getAddress).collect(Collectors.toList()));
        ethFilter.addOptionalTopics(EventEncoder.encode(TRANSFER_EVENT));
        return ethFilter;
    }

    private Observable<Log> live(final List<Token> tokens) {
        return web3jGateway.web3j().ethLogObservable(contractEventsFilter(tokens));
    }
}
