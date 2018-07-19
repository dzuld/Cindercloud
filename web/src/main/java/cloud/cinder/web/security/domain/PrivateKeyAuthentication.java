package cloud.cinder.web.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.web3j.crypto.ECKeyPair;

import static java.util.Collections.emptyList;

public class PrivateKeyAuthentication extends AbstractAuthenticationToken {

    final ECKeyPair keyPair;
    final String address;
    final AuthenticationType type = AuthenticationType.CINDERCLOUD;

    public PrivateKeyAuthentication(final ECKeyPair keyPair, final String address) {
        super(emptyList());
        this.keyPair = keyPair;
        this.address = address;
    }

    @Override
    public Object getCredentials() {
        return keyPair;
    }

    @Override
    public Object getPrincipal() {
        return address;
    }

    public AuthenticationType getType() {
        return type;
    }
}
