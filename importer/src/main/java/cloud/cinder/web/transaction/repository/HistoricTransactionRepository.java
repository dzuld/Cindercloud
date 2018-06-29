package cloud.cinder.web.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.transaction.domain.HistoricTransaction;

public interface HistoricTransactionRepository extends JpaRepository<HistoricTransaction, String> {
}
