package cloud.cinder.common.mail;

import lombok.extern.slf4j.Slf4j;
import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MailService {

    private Configuration configuration;

    public MailService(final Configuration configuration) {
        this.configuration = configuration;
    }

    @Value("${cloud.cinder.mailgun.default-to:info@cinder.cloud}")
    private String defaultTo;

    public void send(final String title, final String content, final String to) {
        try {
            log.debug("sending mail to {}", to);
            Mail.using(configuration)
                    .to(to)
                    .subject(title)
                    .text(content)
                    .build()
                    .send();
        } catch (final Exception exception) {
            log.error("Unable to send mail.", exception);
        }
    }

    public void send(final String title, final String content) {
        send(title, content, defaultTo);
    }
}
