package cloud.cinder.web.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select transaction from Transaction transaction where fromAddress like :address or toAddress like :address order by blockHeight desc")
    Slice<Transaction> findByAddressFromOrTo(@Param("address") final String address, final Pageable pageable);

    Slice<Transaction> findAllByBlockHashOrderByBlockHeightDesc(@Param("blockHash") final String blockHash, final Pageable pageable);

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    Slice<Transaction> findAllOrOrderByBlockTimestampAsPage(Pageable pageable);

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    List<Transaction> findAllOrOrderByBlockTimestampAsList(Pageable pageable);

    @Query("select transaction from Transaction transaction where blockHash LIKE %:block% order by blockHeight desc")
    Slice<Transaction> findByBlockHash(@Param("block") final String block, final Pageable pageable);

    @Query("select transaction from Transaction transaction where (fromAddress like :address or toAddress like :address) and blockHash like :blockHash order by blockHeight desc")
    Slice<Transaction> findByAddressFromOrToAndBlockHash(@Param("blockHash") final String blockHash, final @Param("address") String address, final Pageable pageable);

    @Query("select transaction from Transaction transaction where blockTimestamp >= :from order by blockHeight")
    Stream<Transaction> findAllTransactionsSince(@Param("from") final Date from);
}
