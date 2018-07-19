package cloud.cinder.whitehat.credentials.service;

import cloud.cinder.common.credential.domain.LeakedCredential;
import cloud.cinder.whitehat.credentials.repository.LeakedCredentialRepository;
import cloud.cinder.whitehat.sweeping.EthereumSweeper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Stream;

@Service
@Slf4j
public class CredentialService {

    private LeakedCredentialRepository leakedCredentialRepository;
    private EthereumSweeper ethereumSweeper;

    public CredentialService(final LeakedCredentialRepository leakedCredentialRepository, final EthereumSweeper ethereumSweeper) {
        this.leakedCredentialRepository = leakedCredentialRepository;
        this.ethereumSweeper = ethereumSweeper;
    }

    @Transactional
    public void saveLeakedCredential(final LeakedCredential leakedCredential) {
        if (!leakedCredentialRepository.exists(leakedCredential.getAddress())) {
            leakedCredentialRepository.save(leakedCredential);
            sweep(leakedCredential);
        }
    }

    @Transactional(readOnly = true)
    public Stream<LeakedCredential> streamAll() {
        return leakedCredentialRepository.findAll().stream();
    }

    @Transactional(readOnly = true)
    public Optional<LeakedCredential> findByAddress(final String address) {
        return leakedCredentialRepository.findByAddressLike(address);
    }

    private void sweep(final LeakedCredential leakedCredential) {
        log.debug("going to try to sweep {}", leakedCredential.getAddress());
        ethereumSweeper.sweep(leakedCredential.getPrivateKey());
    }

}
