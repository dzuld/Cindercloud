package cloud.cinder.web.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class ClientSideAuthentication extends AbstractAuthenticationToken {

    private String address;
    final AuthenticationType type = AuthenticationType.WEB3;

    public ClientSideAuthentication(final String address) {
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
