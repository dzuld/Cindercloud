package cloud.cinder.vechain.importer.transaction.service;

import cloud.cinder.vechain.importer.transaction.repository.*;
import cloud.cinder.vechain.transaction.*;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class VechainTransactionService {

    private VechainTransactionClauseRepository vechainTransactionClauseRepository;
    private VechainTransactionRepository vechainTransactionRepository;
    private VechainTransactionReceiptRepository vechainTransactionReceiptRepository;
    private VechainTransactionTransferRepository vechainTransactionTransferRepository;
    private VechainTransactionReceiptOutputRepository vechainTransactionReceiptOutputRepository;
    private VechainTransactionEventRepository vechainTransactionEventRepository;

    public VechainTransactionService(final VechainTransactionClauseRepository vechainTransactionClauseRepository,
                                     final VechainTransactionRepository vechainTransactionRepository, final VechainTransactionReceiptRepository vechainTransactionReceiptRepository, final VechainTransactionTransferRepository vechainTransactionTransferRepository, final VechainTransactionReceiptOutputRepository vechainTransactionReceiptOutputRepository, final VechainTransactionEventRepository vechainTransactionEventRepository) {
        this.vechainTransactionClauseRepository = vechainTransactionClauseRepository;
        this.vechainTransactionRepository = vechainTransactionRepository;
        this.vechainTransactionReceiptRepository = vechainTransactionReceiptRepository;
        this.vechainTransactionTransferRepository = vechainTransactionTransferRepository;
        this.vechainTransactionReceiptOutputRepository = vechainTransactionReceiptOutputRepository;
        this.vechainTransactionEventRepository = vechainTransactionEventRepository;
    }

    @Transactional(readOnly = true)
    public boolean exists(final String transactionId) {
        return vechainTransactionRepository.existsById(transactionId);
    }

    @Transactional
    public VechainTransaction save(VechainTransaction vechainTransaction) {
        return vechainTransactionRepository.save(vechainTransaction);
    }

    @Transactional
    public void save(final List<VechainTransactionClause> vechainTransactionClauses) {
        vechainTransactionClauseRepository.saveAll(vechainTransactionClauses);
    }

    @Transactional(readOnly = true)
    public boolean receiptImported(final String id) {
        return vechainTransactionReceiptRepository.existsById(id);
    }

    @Transactional
    public VechainTransactionReceipt save(final VechainTransactionReceipt vechainTransactionReceipt) {
        return vechainTransactionReceiptRepository.save(vechainTransactionReceipt);
    }

    @Transactional
    public VechainTransactionReceiptOutput save(final VechainTransactionReceiptOutput vechainTransactionReceiptOutput) {
        return vechainTransactionReceiptOutputRepository.save(vechainTransactionReceiptOutput);
    }

    @Transactional
    public VechainTransactionTransfer save(final VechainTransactionTransfer transfer) {
        return vechainTransactionTransferRepository.save(transfer);
    }

    @Transactional
    public VechainTransactionEvent save(final VechainTransactionEvent event) {
        return vechainTransactionEventRepository.save(event);
    }
}
