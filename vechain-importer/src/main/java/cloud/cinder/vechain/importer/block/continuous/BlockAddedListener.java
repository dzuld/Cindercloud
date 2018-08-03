package cloud.cinder.vechain.importer.block.continuous;

import cloud.cinder.vechain.importer.transaction.TransactionReceiptProcessor;
import cloud.cinder.vechain.importer.transaction.service.VechainTransactionService;
import cloud.cinder.vechain.thorifyj.ThorifyjGateway;
import cloud.cinder.vechain.thorifyj.block.domain.ThorifyBlock;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransaction;
import cloud.cinder.vechain.transaction.VechainTransaction;
import cloud.cinder.vechain.transaction.VechainTransactionClause;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.vechain.queue-block-added-transaction-import", havingValue = "true")
public class BlockAddedListener {

    private ObjectMapper objectMapper;
    private VechainTransactionService vechainTransactionService;
    private ThorifyjGateway thorifyjGateway;
    private TransactionReceiptProcessor transactionReceiptProcessor;

    public BlockAddedListener(final ObjectMapper objectMapper,
                              final VechainTransactionService vechainTransactionService,
                              final ThorifyjGateway thorifyjGateway,
                              final TransactionReceiptProcessor transactionReceiptProcessor) {
        this.objectMapper = objectMapper;
        this.vechainTransactionService = vechainTransactionService;
        this.thorifyjGateway = thorifyjGateway;
        this.transactionReceiptProcessor = transactionReceiptProcessor;
    }

    public void receiveMessage(final String blockAsString) {
        try {
            log.debug("Fetched from queue: {}", blockAsString);
            final ThorifyBlock convertedBlock = objectMapper.readValue(blockAsString, ThorifyBlock.class);
            convertedBlock.getTransactions()
                    .forEach(this::importTransaction);
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        }
    }

    private void importTransaction(final String transactionhash) {
        final Optional<ThorifyTransaction> transaction = getTransaction(transactionhash);
        transaction
                .filter(x -> !vechainTransactionService.exists(x.getId()))
                .ifPresent(tx -> {

                    final VechainTransaction savedTransaction = vechainTransactionService.save(VechainTransaction.of(tx));
                    if (tx.getClauses() != null && tx.getClauses().size() > 0) {
                        vechainTransactionService.save(tx.getClauses()
                                .stream()
                                .map(x -> VechainTransactionClause.of(x, savedTransaction.getId()))
                                .collect(Collectors.toList()));
                    }
                    importReceipt(transactionhash);
                });

    }

    private void importReceipt(final String transactionhash) {
        transactionReceiptProcessor.importTransactionReceipt(transactionhash);
    }

    private Optional<ThorifyTransaction> getTransaction(final String transactionhash) {
        try {
            return Optional.ofNullable(thorifyjGateway.thorify().transactions().get(transactionhash));
        } catch (final Exception exc) {
            return Optional.empty();
        }
    }
}
