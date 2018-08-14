package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.vechain.transaction.VechainTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VechainTransactionRepository extends JpaRepository<VechainTransaction, String> {
}
