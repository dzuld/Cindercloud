package cloud.cinder.vechain.config;

import cloud.cinder.vechain.CindercloudVechain;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = CindercloudVechain.class)
public class VechainFeignConfig {

}
