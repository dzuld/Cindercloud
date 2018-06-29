package cloud.cinder.whitehat;

import cloud.cinder.common.CindercloudCommon;
import cloud.cinder.ethereum.CindercloudEthereum;
import cloud.cinder.web.infrastructure.IgnoreDuringComponentScan;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.EndpointAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.HealthIndicatorAutoConfiguration;
import org.springframework.boot.actuate.autoconfigure.InfoContributorAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.core.env.Environment;

@SpringBootApplication
@ComponentScan(
        basePackageClasses = {
                CindercloudCommon.class,
                CindercloudEthereum.class
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
@EnableAutoConfiguration(exclude = {
        EndpointAutoConfiguration.class,
        HealthIndicatorAutoConfiguration.class,
        InfoContributorAutoConfiguration.class
})
@Slf4j
public class CindercloudWhitehat {

    public static void main(String[] args) {
        final SpringApplication app = new SpringApplication(CindercloudWhitehat.class);
        final Environment env = app.run(args).getEnvironment();
        log.info("\n----------------------------------------------------------\n\t"
                        + "Application '{}' is running! Access URLs:\n\t"
                        + "Profile(s): \t{}\n----------------------------------------------------------",
                env.getProperty("spring.application.name"),
                env.getActiveProfiles());
    }
}
