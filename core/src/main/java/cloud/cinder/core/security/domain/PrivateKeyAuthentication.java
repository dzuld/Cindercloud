package cloud.cinder.core.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.web3j.crypto.ECKeyPair;

import java.math.BigInteger;

import static java.util.Collections.emptyList;

public class PrivateKeyAuthentication extends AbstractAuthenticationToken {

    final BigInteger privateKey;
    final String address;
    final AuthenticationType type = AuthenticationType.CINDERCLOUD;

    public PrivateKeyAuthentication(final ECKeyPair keyPair, final String address) {
        super(emptyList());
        this.privateKey = keyPair.getPrivateKey();
        this.address = address;
    }

    @Override
    public Object getCredentials() {
        return ECKeyPair.create(privateKey);
    }

    @Override
    public Object getPrincipal() {
        return address;
    }

    public AuthenticationType getType() {
        return type;
    }
}
