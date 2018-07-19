package cloud.cinder.web.credentials.repository;

import cloud.cinder.common.credential.domain.LeakedCredential;
import cloud.cinder.common.infrastructure.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeakedCredentialRepository extends JpaRepository<LeakedCredential, String> {

    @Query("select lc from LeakedCredential lc where address LIKE %:query% or privateKey LIKE %:query%")
    List<LeakedCredential> search(final @Param("query") String query);

}
