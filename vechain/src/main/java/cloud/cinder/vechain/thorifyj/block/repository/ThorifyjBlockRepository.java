package cloud.cinder.vechain.thorifyj.block.repository;

import cloud.cinder.vechain.thorifyj.block.domain.ThorifyBlock;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://api.vechain.tools", value = "thorify-blocks")
public interface ThorifyjBlockRepository {

    @GetMapping("/blocks/{identifier}")
    ThorifyBlock getBlock(@Param("identifier") final String identifier);
}
