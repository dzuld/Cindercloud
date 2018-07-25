package cloud.cinder.web.token.repository;

import cloud.cinder.common.infrastructure.repository.JpaRepository;
import cloud.cinder.ethereum.token.domain.Token;

public interface TokenRepository extends JpaRepository<Token, Long> {
}
