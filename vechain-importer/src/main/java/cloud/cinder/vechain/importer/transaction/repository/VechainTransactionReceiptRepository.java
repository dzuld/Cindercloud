package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransactionReceipt;

public interface VechainTransactionReceiptRepository extends JpaRepository<VechainTransactionReceipt, String> {
}
