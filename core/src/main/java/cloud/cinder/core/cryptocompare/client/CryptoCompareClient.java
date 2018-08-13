package cloud.cinder.core.cryptocompare.client;

import cloud.cinder.core.cryptocompare.dto.PriceResultDto;
import feign.Param;
import feign.RequestLine;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(url = "https://min-api.cryptocompare.com", value = "cryptocompare")
public interface CryptoCompareClient {

    @RequestLine("GET /data/price?fsym={symbol}&tsyms=EUR,USD,BTC,ETH")
    PriceResultDto getPrice(final @Param("symbol") String symbol);

}
