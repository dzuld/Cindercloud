package cloud.cinder.vechain.config;

import cloud.cinder.vechain.thorifyj.repository.ThorifyTransactionRepository;
import cloud.cinder.vechain.thorifyj.repository.ThorifyjBlockRepository;
import feign.Feign;
import feign.codec.Decoder;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(FeignClientsConfiguration.class)
public class ThorifyFeignConfiguration {

    @Bean
    public ThorifyjBlockRepository provideBlockRepository(final Decoder decoder) {
        return Feign.builder()
                .decoder(decoder)
                .target(ThorifyjBlockRepository.class, "https://api.vechain.tools");
    }

    @Bean
    public ThorifyTransactionRepository provideTransactionRepository(final Decoder decoder) {
        return Feign.builder()
                .decoder(decoder)
                .target(ThorifyTransactionRepository.class, "https://api.vechain.tools");
    }
}
