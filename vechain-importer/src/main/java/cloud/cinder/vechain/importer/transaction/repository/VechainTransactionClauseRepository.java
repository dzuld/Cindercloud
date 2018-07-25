package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransactionClause;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VechainTransactionClauseRepository extends JpaRepository<VechainTransactionClause, Long> {

    List<VechainTransactionClause> findAllByTransactionId(@Param("transactionId") final String transactionId);

}
