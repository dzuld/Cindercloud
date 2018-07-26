package cloud.cinder.whitehat.sweeping.continuous;

import cloud.cinder.whitehat.credentials.service.CredentialService;
import cloud.cinder.whitehat.sweeping.EthereumSweeper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Slf4j
public class EthereumSweeperService {

    @Autowired
    private EthereumSweeper ethereumSweeper;
    @Autowired
    private CredentialService leakedCredentialRepository;

    private Map<String, Date> shortTermSweeping = new HashMap<>();

    @Scheduled(fixedDelay = 3_600_000 /* one hour */)
    public void sweepKnownAddresses() {
        leakedCredentialRepository.streamAll()
                .forEach(x -> ethereumSweeper.sweep(x.getPrivateKey()));
    }

    @Async
    public void sweepEthereum(final String address) {
        leakedCredentialRepository.findByAddress(address).ifPresent(credential -> {
            log.debug("[SweepAfterTransaction] Looks like someone sent some eth to {}", address);
            shortTermSweeping.put(credential.getPrivateKey(), Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC)));
            ethereumSweeper.sweep(credential.getPrivateKey());
        });
    }

    //all transactions that were found
    @Scheduled(fixedDelay = 2000)
    public void sweepShortTermFindings() {
        final HashMap<String, Date> hashmapCopy = new HashMap<>(shortTermSweeping);
        hashmapCopy.forEach((privateKey, date) -> {
            ethereumSweeper.sweep(privateKey);
        });
    }

    @Scheduled(fixedDelay = 10000)
    public void removeAgedFindings() {
        final HashMap<String, Date> hashmapCopy = new HashMap<>(shortTermSweeping);
        hashmapCopy.forEach((privateKey, date) -> {
            final Date fiveMInutesAgo = Date.from(LocalDateTime.now().toInstant(ZoneOffset.UTC).minus(5, ChronoUnit.MINUTES));
            if (date.before(fiveMInutesAgo)) {
                log.debug("Removing aged finding: {}", privateKey);
                shortTermSweeping.remove(privateKey);
            }
        });
    }
}
