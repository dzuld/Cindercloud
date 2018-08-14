package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.vechain.transaction.VechainTransactionEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainTransactionEventRepository extends JpaRepository<VechainTransactionEvent, Long> {
}
