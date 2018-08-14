package cloud.cinder.core.vechain.block.service;

import cloud.cinder.core.vechain.block.repository.VechainTransactionReceiptRepository;
import cloud.cinder.core.vechain.block.repository.VechainTransactionRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;
import cloud.cinder.vechain.transaction.VechainTransactionReceipt;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class VechainTransactionService {

    private VechainTransactionRepository vechainTransactionRepository;
    private VechainTransactionReceiptRepository vechainTransactionReceiptRepository;

    public VechainTransactionService(final VechainTransactionRepository vechainTransactionRepository,
                                     final VechainTransactionReceiptRepository vechainTransactionReceiptRepository) {
        this.vechainTransactionRepository = vechainTransactionRepository;
        this.vechainTransactionReceiptRepository = vechainTransactionReceiptRepository;
    }

    @Transactional(readOnly = true)
    public Slice<VechainTransaction> getTransactionsForBlock(final String id, final Pageable pageable) {
        return vechainTransactionRepository.findAllByBlockIdOrderByBlockNumberDesc(id, pageable);
    }


    @Transactional(readOnly = true)
    public Slice<VechainTransaction> getLastTransactions(Pageable pageable) {
        return vechainTransactionRepository.findAllOrderByBlockTimestamp(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<VechainTransaction> getTransaction(final String hash) {
        return vechainTransactionRepository.findById(hash);
    }

    @Transactional
    public Optional<VechainTransactionReceipt> getTransactionReceipt(final String hash) {
        return vechainTransactionReceiptRepository.findById(hash);
    }
}
