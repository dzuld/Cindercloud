package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;

public interface VechainTransactionRepository extends JpaRepository<VechainTransaction, String> {
}
