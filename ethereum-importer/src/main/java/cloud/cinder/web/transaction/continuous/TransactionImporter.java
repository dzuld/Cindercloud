package cloud.cinder.web.transaction.continuous;

import cloud.cinder.web.transaction.service.TransactionService;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.ethereum.transaction.TransactionStatusService;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.web3j.protocol.core.methods.response.EthBlock;
import rx.functions.Action1;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.Objects;

@Component
@Slf4j
public class TransactionImporter {

    private Web3jGateway web3j;
    private TransactionService transactionService;
    private ObjectMapper objectMapper;
    private TransactionStatusService transactionStatusService;

    public TransactionImporter(final Web3jGateway web3j,
                               final TransactionService transactionService,
                               final ObjectMapper objectMapper,
                               final TransactionStatusService transactionStatusService) {
        this.web3j = web3j;
        this.transactionService = transactionService;
        this.objectMapper = objectMapper;
        this.transactionStatusService = transactionStatusService;
    }

    @PostConstruct
    public void init() {
        objectMapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);
    }

    public void importTransactions(final Block convertedBlock) {
        try {
            web3j.web3j().ethGetBlockByHash(convertedBlock.getHash(), true)
                    .observable()
                    .filter(bk -> bk.getBlock() != null)
                    .flatMapIterable(bk -> bk.getBlock().getTransactions())
                    .filter(tx -> tx.get() != null && tx.get() instanceof EthBlock.TransactionObject && ((EthBlock.TransactionObject) tx.get()).get() != null)
                    .map(tx -> ((EthBlock.TransactionObject) tx.get()))
                    .map(tx -> {
                        log.trace("importing transaction {}", tx.getHash());
                        return Transaction.builder()
                                .blockHash(tx.getBlockHash())
                                .blockHeight(convertedBlock.getHeight())
                                .fromAddress(tx.getFrom())
                                .gas(tx.getGas())
                                .hash(tx.getHash())
                                .input(tx.getInput())
                                .toAddress(tx.getTo())
                                .value(tx.getValue())
                                .blockTimestamp(Date.from(LocalDateTime.ofEpochSecond(convertedBlock.getTimestamp().longValue(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                                .gasPrice(tx.getGasPrice())
                                .creates(tx.getCreates())
                                .s(tx.getS())
                                .r(tx.getR())
                                .v(tx.getV())
                                .status(transactionStatusService.getTransactionStatus(tx))
                                .nonce(tx.getNonce())
                                .transactionIndex(tx.getTransactionIndex())
                                .build();
                    })
                    .filter(Objects::nonNull)
                    .forEach(save());
        } catch (final Exception e) {
            log.error("Error trying to import transactions from block", e);
        }
    }

    private Action1<Transaction> save() {
        return (e) -> {
            try {
                transactionService.save(e);
            } catch (final Exception ex) {
                log.debug("Couldn't save, tx already in db: {}", e.getHash());
            }
        };
    }
}