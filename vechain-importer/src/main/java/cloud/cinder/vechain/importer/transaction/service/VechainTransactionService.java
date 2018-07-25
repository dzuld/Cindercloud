package cloud.cinder.vechain.importer.transaction.service;

import cloud.cinder.vechain.importer.transaction.repository.VechainTransactionClauseRepository;
import cloud.cinder.vechain.importer.transaction.repository.VechainTransactionRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;
import cloud.cinder.vechain.transaction.VechainTransactionClause;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class VechainTransactionService {

    private VechainTransactionClauseRepository vechainTransactionClauseRepository;
    private VechainTransactionRepository vechainTransactionRepository;

    public VechainTransactionService(final VechainTransactionClauseRepository vechainTransactionClauseRepository,
                                     final VechainTransactionRepository vechainTransactionRepository) {
        this.vechainTransactionClauseRepository = vechainTransactionClauseRepository;
        this.vechainTransactionRepository = vechainTransactionRepository;
    }

    @Transactional(readOnly = true)
    public boolean exists(final String transactionId) {
        return vechainTransactionRepository.exists(transactionId);
    }

    @Transactional
    public VechainTransaction save(VechainTransaction vechainTransaction) {
        return vechainTransactionRepository.save(vechainTransaction);
    }

    @Transactional
    public void save(final List<VechainTransactionClause> vechainTransactionClauses) {
        vechainTransactionClauseRepository.save(vechainTransactionClauses);
    }
}
