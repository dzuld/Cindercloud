package cloud.cinder.core.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class Web3Authentication extends AbstractAuthenticationToken {

    private String address;
    final AuthenticationType type = AuthenticationType.WEB3;

    public Web3Authentication(final String address) {
        super(Collections.emptyList());
        this.address = address;
    }

    @Override
    public Object getCredentials() {
        return address;
    }

    @Override
    public Object getPrincipal() {
        return address;
    }

    public AuthenticationType getType() {
        return type;
    }
}
