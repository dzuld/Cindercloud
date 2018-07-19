package cloud.cinder.web.security.domain;

import lombok.Getter;

@Getter
public class TrezorAuthentication extends HardwareWalletAuthentication {

    private String xpubkey;
    private String publicKey;
    private String chainCode;
    private String address;
    private String path;

    final AuthenticationType type = AuthenticationType.CINDERCLOUD;

    public TrezorAuthentication(final String xpubkey,
                                final String publicKey,
                                final String chainCode,
                                final String address) {
        super();
        this.xpubkey = xpubkey;
        this.publicKey = publicKey;
        this.chainCode = chainCode;
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
        return AuthenticationType.TREZOR;
    }

}
