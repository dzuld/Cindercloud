package cloud.cinder.core.config;

import cloud.cinder.core.CindercloudCore;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@EnableFeignClients(basePackageClasses = CindercloudCore.class)
@Configuration
public class FeignConfig {
}
