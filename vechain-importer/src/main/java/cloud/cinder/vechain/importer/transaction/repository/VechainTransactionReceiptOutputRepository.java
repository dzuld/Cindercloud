package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.vechain.transaction.VechainTransactionReceiptOutput;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainTransactionReceiptOutputRepository extends JpaRepository<VechainTransactionReceiptOutput, Long> {


}
