package cloud.cinder.core.token.repository;

import cloud.cinder.ethereum.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("select t from Token t where lower(address) LIKE lower(:address)")
    Optional<Token> findByAddressLike(@Param("address") final String address);

}
