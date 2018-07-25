package cloud.cinder.web.token.listener;

import cloud.cinder.web.token.listener.model.UserTokenRequest;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@ConditionalOnProperty(name = "token-transfer-queue-import", havingValue = "true")
@Slf4j
public class UserTokenImportListener {

    private ObjectMapper objectMapper;
    private TokenTransferHistoricImporter tokenTransferHistoricImporter;
    private final Meter blockImportMeter;

    private Map<String, Date> alreadySweeped = new HashMap<>();


    public UserTokenImportListener(final ObjectMapper objectMapper,
                                   final MetricRegistry metricRegistry,
                                   final TokenTransferHistoricImporter tokenTransferHistoricImporter) {
        this.objectMapper = objectMapper;
        this.tokenTransferHistoricImporter = tokenTransferHistoricImporter;

        this.blockImportMeter = metricRegistry.meter("user_token_imports");
    }

    public void receiveMessage(final String userTokenImportRequestAsString) {
        try {
            log.debug("Fetched from queue: {}", userTokenImportRequestAsString);
            final UserTokenRequest userTokenRequest = objectMapper.readValue(userTokenImportRequestAsString, UserTokenRequest.class);
            alreadySweeped.computeIfAbsent(userTokenRequest.getAddress(), s -> {
                tokenTransferHistoricImporter.importForUserTokens(userTokenRequest);
                return new Date();
            });
        } catch (final Exception ex) {
            log.error("Error trying to receive message", ex);
            throw new IllegalArgumentException("Unable to process");
        } finally {
            blockImportMeter.mark();
        }
    }

    @Scheduled(fixedDelay = 10000)
    public void removeAgedAlreadySweeped() {
        final HashMap<String, Date> hashmapCopy = new HashMap<>(alreadySweeped);
        hashmapCopy.forEach((address, date) -> {
            final Date oneDayAgo = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC).minus(1, ChronoUnit.DAYS));
            if (date.before(oneDayAgo)) {
                log.debug("Removing aged finding: {}", address);
                alreadySweeped.remove(address);
            }
        });
    }
}
