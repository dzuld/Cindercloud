package cloud.cinder.vechain.thorifyj.transaction.repository;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransaction;
import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(url = "https://api.vechain.tools", value = "thorify-transactions")
public interface ThorifyTransactionRepository {

    @GetMapping("/transactions/{identifier}")
    ThorifyTransaction get(final @Param("identifier") String identifier);

    @GetMapping("/transactions/{id}/receipt")
    ThorifyTransactionReceipt getReceipt(final @Param("id") String id);
}
