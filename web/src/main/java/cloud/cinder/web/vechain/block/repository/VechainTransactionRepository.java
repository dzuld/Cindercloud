package cloud.cinder.web.vechain.block.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.vechain.transaction.VechainTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.query.Param;

public interface VechainTransactionRepository extends JpaRepository<VechainTransaction, String> {


    Slice<VechainTransaction> findAllByBlockIdOrderByBlockNumberDesc(final @Param("blockId") String blockId, Pageable pageable);
}
