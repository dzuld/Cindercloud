package cloud.cinder.core.config;

import cloud.cinder.common.infrastructure.IgnoreDuringComponentScan;
import cloud.cinder.core.etherscan.EtherscanClient;
import feign.Logger;
import feign.RequestInterceptor;
import feign.slf4j.Slf4jLogger;
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

    @Bean
    Logger.Level feignLoggerLevel() {
        return Logger.Level.BASIC;
    }

    @Bean
    public Logger logger() {
        return new Slf4jLogger(EtherscanClient.class);
    }
}
