package cloud.cinder.web.transaction.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.Query;

import java.math.BigInteger;
import java.util.stream.Stream;

public interface TransactionRepository extends JpaRepository<Transaction, String> {

    @Query("select transaction from Transaction transaction order by blockHeight desc")
    Slice<Transaction> getTransactionsOrderedByHeight(final Pageable pageable);

    @Query("select transaction from Transaction transaction where blockHeight < :blockHeight")
    Stream<Transaction> findAllTransactionsBefore(final BigInteger blockHeight);
}
