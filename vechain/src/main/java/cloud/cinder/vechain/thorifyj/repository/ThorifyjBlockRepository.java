package cloud.cinder.vechain.thorifyj.repository;

import cloud.cinder.vechain.thorifyj.domain.ThorifyBlock;
import feign.Param;
import feign.RequestLine;

public interface ThorifyjBlockRepository {

    @RequestLine("GET /blocks/{identifier}")
    ThorifyBlock getBlock(@Param("identifier") final String identifier);
}
