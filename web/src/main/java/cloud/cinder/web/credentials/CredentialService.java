package cloud.cinder.web.credentials;

import cloud.cinder.common.credential.domain.LeakedCredential;
import cloud.cinder.web.credentials.repository.LeakedCredentialRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

import java.util.Date;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Component
@Slf4j
public class CredentialService {

    @Autowired
    private LeakedCredentialRepository leakedCredentialRepository;

    @Transactional
    public void saveLeakedCredential(final String privateKey) {
        try {
            final ECKeyPair keypair = ECKeyPair.create(Numeric.decodeQuantity(privateKey.trim()));
            final String address = prettifyAddress(Keys.getAddress(keypair));
            if (!leakedCredentialRepository.exists(address)) {
                leakedCredentialRepository.save(LeakedCredential.builder()
                        .dateAdded(new Date())
                        .address(address)
                        .privateKey(privateKey)
                        .build());
            }
        } catch (final Exception exc) {
            log.error("unable to save {}", privateKey);
        }
    }
}
