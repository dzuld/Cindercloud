package cloud.cinder.web.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public abstract class HardwareWalletAuthentication extends AbstractAuthenticationToken {

    public HardwareWalletAuthentication() {
        super(Collections.emptyList());
    }

    public abstract AuthenticationType getType();


}
