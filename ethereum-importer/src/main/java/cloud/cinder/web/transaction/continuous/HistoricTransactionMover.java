package cloud.cinder.web.transaction.continuous;

import cloud.cinder.web.transaction.repository.HistoricTransactionRepository;
import cloud.cinder.web.transaction.repository.TransactionRepository;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.transactions.historic-mover", havingValue = "true")
public class HistoricTransactionMover {

    @Value("${cloud.cinder.transactions.historic-threshold}")
    private int historicTransactionThreshold;

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private HistoricTransactionRepository historicTransactionRepository;

    @Scheduled(fixedDelay = 60000)
    public void moveTransactions() {
        if (transactionRepository.count() > historicTransactionThreshold) {
            final Slice<Transaction> transactionsOrderedByHeight = transactionRepository.getTransactionsOrderedByHeight(new PageRequest(historicTransactionThreshold, 1));
            if (transactionsOrderedByHeight.hasContent()) {
                final Optional<Transaction> transaction = transactionsOrderedByHeight.getContent().stream().findFirst();
                transaction.ifPresent(moveOlderThanTransaction(transaction.get()));
            }
        }
    }

    private Consumer<Transaction> moveOlderThanTransaction(final Transaction transaction) {
        return tx -> {
            log.info("there are more than {} transactions, trying to move oldes now.", historicTransactionThreshold);
            final Stream<Transaction> allOldTransactions = transactionRepository.findAllTransactionsBefore(transaction.getBlockHeight());
            allOldTransactions
                    .forEach(move());
        };
    }

    private Consumer<Transaction> move() {
        return x -> {
            log.debug("moving tx to historic_tx: {}", x.getHash());
            historicTransactionRepository.save(x.toHistoricTransaction());
            transactionRepository.delete(x);
        };
    }

}
