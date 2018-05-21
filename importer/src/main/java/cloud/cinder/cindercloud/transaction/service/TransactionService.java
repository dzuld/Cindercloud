package cloud.cinder.cindercloud.transaction.service;

import cloud.cinder.cindercloud.transaction.repository.TransactionRepository;
import cloud.cinder.ethereum.transaction.Transaction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {

    private TransactionRepository transactionRepository;

    public TransactionService(final TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void save(final Transaction transaction) {
        if (!transactionRepository.exists(transaction.getHash())) {
            transactionRepository.save(transaction);
        }
    }
}
