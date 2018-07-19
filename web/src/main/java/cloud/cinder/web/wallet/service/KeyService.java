package cloud.cinder.web.wallet.service;

import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;
import org.web3j.utils.Numeric;

@Service
public class KeyService {

    public String fromPrivateKey(final String privateKey) {
        requireLength(privateKey);
        require0x(privateKey);
        return Keys.getAddress(ECKeyPair.create(Numeric.decodeQuantity(privateKey)));
    }

    private void require0x(final String privateKey) {
        if (!privateKey.startsWith("0x")) {
            throw new IllegalArgumentException("Private key should start with 0x");
        }
    }

    private void requireLength(final String privateKey) {
        if (privateKey == null || privateKey.length() != 66) {
            throw new IllegalArgumentException("Incorrect size of private key");
        }
    }

}
