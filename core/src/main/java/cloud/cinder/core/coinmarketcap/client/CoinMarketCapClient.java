package cloud.cinder.core.coinmarketcap.client;

import cloud.cinder.core.coinmarketcap.dto.TickerResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(url = "https://api.coinmarketcap.com/v1", value = "coinmarketcap")
public interface CoinMarketCapClient {

    @GetMapping("/ticker/{id}?convert=EUR")
    List<TickerResult> getTickerById(@PathVariable("id") final String id);
}
