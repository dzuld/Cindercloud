package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.vechain.transaction.VechainTransactionClause;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainTransactionClauseRepository extends JpaRepository<VechainTransactionClause, Long> {

}
