package cloud.cinder.core.cryptocompare.service;

import cloud.cinder.core.cryptocompare.client.CryptoCompareClient;
import cloud.cinder.core.cryptocompare.dto.PriceResultDto;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class CryptoCompareGateway {

    private final CryptoCompareClient client;

    public CryptoCompareGateway(final CryptoCompareClient client) {
        this.client = client;
    }

    @Cacheable(value = "token_price", key = "#symbol")
    public PriceResultDto getPrice(final String symbol) {
        return client.getPrice(symbol);
    }
}
