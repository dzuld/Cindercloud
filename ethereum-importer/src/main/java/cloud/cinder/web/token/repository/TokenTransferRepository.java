package cloud.cinder.web.token.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.token.domain.TokenTransfer;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenTransferRepository extends JpaRepository<TokenTransfer, Long> {

    @Query("select f from TokenTransfer f where transactionHash LIKE :transactionHash and logIndex = :logIndex")
    Optional<TokenTransfer> findByTransactionHashAndLogIndex(@Param("transactionHash") final String transactionHash, final @Param("logIndex") String logIndex);

}
