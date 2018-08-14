package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.vechain.transaction.VechainTransactionReceipt;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainTransactionReceiptRepository extends JpaRepository<VechainTransactionReceipt, String> {
}
