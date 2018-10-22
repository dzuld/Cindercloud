package cloud.cinder.core.vechain.block.repository;

import cloud.cinder.vechain.transaction.VechainTransaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface VechainTransactionRepository extends JpaRepository<VechainTransaction, String> {


    Slice<VechainTransaction> findAllByBlockIdOrderByBlockNumberDesc(final @Param("blockId") String blockId, Pageable pageable);

    @Query("select transaction from VechainTransaction transaction order by blockNumber desc")
    Slice<VechainTransaction> findAllOrderByBlockTimestamp(Pageable pageable);
}
