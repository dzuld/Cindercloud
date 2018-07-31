package cloud.cinder.core.vechain.block.service;

import cloud.cinder.core.vechain.block.repository.VechainTransactionRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class VechainTransactionService {

    private VechainTransactionRepository vechainTransactionRepository;

    public VechainTransactionService(final VechainTransactionRepository vechainTransactionRepository) {
        this.vechainTransactionRepository = vechainTransactionRepository;
    }

    @Transactional(readOnly = true)
    public Slice<VechainTransaction> getTransactionsForBlock(final String id, final Pageable pageable) {
        return vechainTransactionRepository.findAllByBlockIdOrderByBlockNumberDesc(id, pageable);
    }
}
