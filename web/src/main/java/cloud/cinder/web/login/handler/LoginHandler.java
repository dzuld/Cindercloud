package cloud.cinder.web.login.handler;

import cloud.cinder.common.login.domain.LoginEvent;
import cloud.cinder.web.security.domain.ClientSideAuthentication;
import cloud.cinder.web.security.domain.PrivateKeyAuthentication;
import cloud.cinder.web.security.domain.TrezorAuthentication;
import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.web3j.crypto.Credentials;

import java.util.Date;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Component
public class LoginHandler {

    private ApplicationEventPublisher $;

    public LoginHandler(final ApplicationEventPublisher applicationEventPublisher) {
        this.$ = applicationEventPublisher;
    }

    public void login(final Credentials credentials) {
        SecurityContextHolder.getContext().setAuthentication(new PrivateKeyAuthentication(credentials.getEcKeyPair(), prettifyAddress(credentials.getAddress())));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(credentials.getAddress(), "CINDERCLOUD");
    }

    public void clientsideLogin(final String address) {
        SecurityContextHolder.getContext().setAuthentication(new ClientSideAuthentication(prettifyAddress(address)));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(address, "WEB3");
    }

    public void trezorLogin(final String xpubkey,
                            final String publicKey,
                            final String chainCode,
                            final String address) {
        Preconditions.checkNotNull(xpubkey);
        Preconditions.checkNotNull(publicKey);
        Preconditions.checkNotNull(chainCode);
        Preconditions.checkNotNull(address);
        SecurityContextHolder.getContext().setAuthentication(new TrezorAuthentication(xpubkey, publicKey, chainCode, prettifyAddress(address)));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(address, "TREZOR");
    }


    private void throwEvent(final String address, final String walletType) {
        $.publishEvent(
                LoginEvent
                        .builder()
                        .wallet(address)
                        .eventTime(new Date())
                        .walletType(walletType)
                        .build()
        );
    }
}
