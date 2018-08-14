package cloud.cinder.whitehat.token.repository;

import cloud.cinder.ethereum.token.domain.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
