package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransactionEvent;

public interface VechainTransactionEventRepository extends JpaRepository<VechainTransactionEvent, Long> {
}
