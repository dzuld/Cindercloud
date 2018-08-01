package cloud.cinder.vechain.importer.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransactionTransfer;

public interface VechainTransactionTransferRepository extends JpaRepository<VechainTransactionTransfer, Long> {
}
