package cloud.cinder.vechain.thorifyj.repository;

import cloud.cinder.vechain.thorifyj.domain.ThorifyTransaction;
import feign.Param;
import feign.RequestLine;

public interface ThorifyTransactionRepository {

    @RequestLine("GET /transactions/{identifier}")
    ThorifyTransaction get(final @Param("identifier") String identifier);

}
