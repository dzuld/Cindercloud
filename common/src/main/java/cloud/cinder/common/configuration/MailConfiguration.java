package cloud.cinder.common.configuration;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty("cloud.cinder.mailgun.api-key, cloud.cinder.mailgun.domain")
public class MailConfiguration {

    @Value("${cloud.cinder.mailgun.api-key}")
    private String mailgunApiKey;
    @Value("${cloud.cinder.mailgun.domain}")
    private String mailgunDomain;

    @Bean
    public net.sargue.mailgun.Configuration provideMailConfiguration() {
        return new net.sargue.mailgun.Configuration()
                .domain(mailgunDomain)
                .apiKey(mailgunApiKey)
                .from("Cinder Cloud", "info@cinder.cloud");
    }
}
