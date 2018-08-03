package cloud.cinder.core.vechain.block.service;

import cloud.cinder.core.vechain.block.repository.VechainTransactionRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;
import com.sun.mail.imap.protocol.IMAPProtocol;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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


    @Transactional(readOnly = true)
    public Slice<VechainTransaction> getLastTransactions(Pageable pageable) {
        return vechainTransactionRepository.findAllOrderByBlockTimestamp(pageable);
    }

    public Optional<VechainTransaction> getTransaction(final String hash) {
        return null;
    }
}
