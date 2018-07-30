package cloud.cinder.wallet;

import cloud.cinder.common.CindercloudCommon;
import cloud.cinder.common.infrastructure.IgnoreDuringComponentScan;
import cloud.cinder.core.CindercloudCore;
import cloud.cinder.ethereum.CindercloudEthereum;
import cloud.cinder.vechain.CindercloudVechain;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.InfoContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration;
import org.springframework.cloud.netflix.archaius.ArchaiusAutoConfiguration;
import org.springframework.cloud.netflix.feign.FeignAutoConfiguration;
import org.springframework.cloud.netflix.feign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.cloud.netflix.metrics.servo.ServoMetricsAutoConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EntityScan(basePackageClasses = {
        CindercloudCommon.class,
        CindercloudEthereum.class,
        CindercloudVechain.class,
        CindercloudCore.class

})
@EnableJpaRepositories(basePackageClasses = {
        CindercloudCommon.class,
        CindercloudEthereum.class,
        CindercloudVechain.class,
        CindercloudCore.class
})
@ComponentScan(
        basePackageClasses = {
                CindercloudCommon.class,
                CindercloudEthereum.class,
                CindercloudVechain.class,
                CindercloudCore.class,
                CindercloudWallet.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
@EnableAutoConfiguration(exclude = {
        ArchaiusAutoConfiguration.class,
        EndpointAutoConfiguration.class,
        FeignAutoConfiguration.class,
        FeignRibbonClientAutoConfiguration.class,
        HealthIndicatorAutoConfiguration.class,
        InfoContributorAutoConfiguration.class,
        LoadBalancerAutoConfiguration.class,
        RibbonAutoConfiguration.class,
        ServoMetricsAutoConfiguration.class
})
@Slf4j
@EnableRabbit
public class CindercloudWallet {

    public static void main(String[] args) throws UnknownHostException {
        final SpringApplication app = new SpringApplication(CindercloudWallet.class);
        final Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Local: \t\thttp://localhost:{}\n\t"
                        + "External: \thttp://{}:{}\n\t" +
                        "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getProperty("server.port"),
                InetAddress.getLocalHost().getHostAddress(),
                env.getProperty("server.port"),
                env.getActiveProfiles());
    }
}
