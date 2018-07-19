package cloud.cinder.web.config;

import com.codahale.metrics.MetricRegistry;
import com.zaxxer.hikari.HikariDataSource;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.dropwizard.DropwizardExports;
import io.prometheus.client.hotspot.MemoryPoolsExports;
import io.prometheus.client.hotspot.StandardExports;
import io.prometheus.client.spring.boot.EnablePrometheusEndpoint;
import io.prometheus.client.spring.boot.EnableSpringBootMetricsCollector;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@EnablePrometheusEndpoint
@EnableSpringBootMetricsCollector
@Slf4j
public class MetricConfiguration {

    private MetricRegistry dropwizardMetricRegistry;

    @Autowired
    private HikariDataSource hikariDataSource;

    @Autowired
    public MetricConfiguration(MetricRegistry dropwizardMetricRegistry) {
        this.dropwizardMetricRegistry = dropwizardMetricRegistry;
    }

    @PostConstruct
    public void init() {
        if (hikariDataSource != null) {
            log.debug("Monitoring the datasource");
            hikariDataSource.setMetricRegistry(dropwizardMetricRegistry);
        }
    }

    @PostConstruct
    public void registerPrometheusCollectors() {
        CollectorRegistry.defaultRegistry.clear();
        new StandardExports().register();
        new MemoryPoolsExports().register();
        new DropwizardExports(dropwizardMetricRegistry).register();
    }

}
