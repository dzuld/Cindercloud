package cloud.cinder.web.transaction.repository;

import cloud.cinder.ethereum.transaction.domain.HistoricTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoricTransactionRepository extends JpaRepository<HistoricTransaction, String> {
}
