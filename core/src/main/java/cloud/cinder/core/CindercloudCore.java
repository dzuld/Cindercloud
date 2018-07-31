package cloud.cinder.core;

import cloud.cinder.common.CindercloudCommon;
import cloud.cinder.common.infrastructure.IgnoreDuringComponentScan;
import cloud.cinder.ethereum.CindercloudEthereum;
import cloud.cinder.vechain.CindercloudVechain;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan(basePackageClasses = {
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
        },
        excludeFilters = {
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = TypeExcludeFilter.class),
                @ComponentScan.Filter(type = FilterType.CUSTOM, classes = AutoConfigurationExcludeFilter.class),
                @ComponentScan.Filter(IgnoreDuringComponentScan.class)})
public class CindercloudCore {
}
