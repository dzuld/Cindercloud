package cloud.cinder.vechain.thorifyj.transaction.repository;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransaction;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "https://api.vechain.tools", value = "thorify-transactions")
public interface ThorifyTransactionRepository {

    @RequestLine("GET /transactions/{identifier}")
    ThorifyTransaction get(final @Param("identifier") String identifier);

    @RequestLine("GET /transactions/{id}/receipt")
    ThorifyTransactionReceipt getReceipt(final @Param("id") String id);
}
