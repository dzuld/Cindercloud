package cloud.cinder.whitehat.sweeping.continuous;

import cloud.cinder.whitehat.credentials.service.CredentialService;
import cloud.cinder.whitehat.sweeping.TokenSweeper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TokenSweeperService {

    @Autowired
    private TokenSweeper tokenSweeper;
    @Autowired
    private CredentialService credentialService;

    @Scheduled(fixedDelay = 300_000)
    public void sweepKnownAddresses() {
        credentialService.streamAll()
                .forEach(x -> tokenSweeper.sweep(x.getPrivateKey()));
    }
}
