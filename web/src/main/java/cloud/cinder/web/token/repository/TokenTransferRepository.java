package cloud.cinder.web.token.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.token.domain.TokenTransfer;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TokenTransferRepository extends JpaRepository<TokenTransfer, Long> {

    @Query("select transfer from TokenTransfer transfer where fromAddress LIKE :address or toAddress LIKE :address order by blockTimestamp DESC")
    List<TokenTransfer> findByFromOrTo(@Param("address") final String address, final Pageable pageable);

    @Query("select transfer from TokenTransfer transfer where tokenAddress LIKE :tokenAddress order by blockTimestamp DESC")
    List<TokenTransfer> findByTokenAddress(@Param("tokenAddress") final String tokenAddress, final Pageable pageable);

    @Query("select count(tt) > 0 from TokenTransfer tt where fromAddress LIKE :address or toAddress LIKE :address")
    boolean existsByFromOrTo(@Param("address") final String address);
}
