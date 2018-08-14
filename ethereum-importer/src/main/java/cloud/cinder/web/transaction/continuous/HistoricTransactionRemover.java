package cloud.cinder.web.transaction.continuous;

import cloud.cinder.ethereum.transaction.domain.Transaction;
import cloud.cinder.web.transaction.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Slice;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
@Slf4j
@ConditionalOnProperty(name = "cloud.cinder.transactions.historic-remover", havingValue = "true")
public class HistoricTransactionRemover {

    @Autowired
    private TransactionRepository transactionRepository;

    @Scheduled(fixedDelay = 60000)
    public void deleteTransactions() {
        final Slice<Transaction> agedTransactions = transactionRepository.findTransactionsBefore(Date.from(LocalDateTime.now().minus(6, ChronoUnit.MONTHS).atZone(ZoneId.systemDefault()).toInstant()));
        if (agedTransactions.hasContent()) {
            agedTransactions.getContent()
                    .forEach(x -> {
                        transactionRepository.deleteById(x.getHash());
                    });
        }
    }
}