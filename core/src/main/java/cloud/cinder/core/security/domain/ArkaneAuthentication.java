package cloud.cinder.core.security.domain;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.Collections;

public class ArkaneAuthentication extends AbstractAuthenticationToken {

    static final long serialVersionUID = 1L;

    private String address;
    private String bearerToken;
    private String walletId;

    final AuthenticationType type = AuthenticationType.ARKANE;

    public ArkaneAuthentication(final String address, final String bearerToken, final String walletId) {
        super(Collections.emptyList());
        this.address = address;
        this.bearerToken = bearerToken;
        this.walletId = walletId;
    }

    @Override
    public Object getCredentials() {
        return bearerToken;
    }

    @Override
    public Object getPrincipal() {
        return address;
    }

    public AuthenticationType getType() {
        return type;
    }

    public String getAddress() {
        return address;
    }

    public String getBearerToken() {
        return bearerToken;
    }

    public String getWalletId() {
        return walletId;
    }
}
