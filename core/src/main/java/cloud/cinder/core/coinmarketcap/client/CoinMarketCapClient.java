package cloud.cinder.core.coinmarketcap.client;

import cloud.cinder.core.coinmarketcap.dto.TickerResult;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

import java.util.List;

@FeignClient(url = "https://api.coinmarketcap.com/v1", value="coinmarketcap")
public interface CoinMarketCapClient {

    @RequestLine("GET /ticker/{id}?convert=EUR")
    List<TickerResult> getTickerById(@Param("id") final String id);
}
