package cloud.cinder.whitehat.credentials.repository;


import cloud.cinder.common.credential.domain.LeakedCredential;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface LeakedCredentialRepository extends JpaRepository<LeakedCredential, String> {

    @Query("select lc from LeakedCredential lc")
    Stream<LeakedCredential> streamAll();

    Optional<LeakedCredential> findByAddressLike(String address);
}
