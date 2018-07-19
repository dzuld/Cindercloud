package cloud.cinder.web.config;

import cloud.cinder.web.coinmarketcap.client.CoinMarketCapClient;
import cloud.cinder.web.cryptocompare.client.CryptoCompareClient;
import cloud.cinder.web.etherscan.EtherscanClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class FeignConfiguration {

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${cloud.cinder.etherscan-api}")
    private String etherscanApi;
    @Value("${cloud.cinder.etherscan-api-key}")
    private String etherscanApiKey;

    @Bean
    public CoinMarketCapClient provideCoinMarketcapClient(final Decoder decoder) {
        return Feign.builder()
                .decoder(decoder)
                .target(CoinMarketCapClient.class, "https://api.coinmarketcap.com/v1");
    }

    @Bean
    public CryptoCompareClient provideCryptoCompareClient(final Decoder decoder) {
        return Feign.builder()
                .decoder(decoder)
                .logger(new Logger.JavaLogger())
                .logLevel(Logger.Level.FULL)
                .target(CryptoCompareClient.class, "https://min-api.cryptocompare.com");
    }

    @Bean
    public EtherscanClient provideEtherscanClient(final Encoder encoder, final Decoder decoder) {
        return Feign.builder()
                .decoder(decoder)
                .encoder(encoder)
                .logLevel(Logger.Level.FULL)
                .logger(new Logger.JavaLogger())
                .requestInterceptor(template -> template.query("apikey", etherscanApiKey))
                .target(EtherscanClient.class, etherscanApi);
    }

}
