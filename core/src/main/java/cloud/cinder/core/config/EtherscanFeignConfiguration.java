package cloud.cinder.core.config;

import cloud.cinder.common.infrastructure.IgnoreDuringComponentScan;
import feign.RequestInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@IgnoreDuringComponentScan
public class EtherscanFeignConfiguration {

    @Value("${cloud.cinder.etherscan-api-key}")
    private String etherscanApiKey;

    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return template -> template.query("apikey", etherscanApiKey);
    }
}
