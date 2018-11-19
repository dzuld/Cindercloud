package cloud.cinder.core.login.handler;

import cloud.cinder.common.login.domain.LoginEvent;
import cloud.cinder.core.security.domain.ArkaneAuthentication;
import cloud.cinder.core.security.domain.TrezorAuthentication;
import cloud.cinder.core.security.domain.Web3Authentication;
import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Date;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Component
public class LoginHandler {

    private ApplicationEventPublisher $;

    public LoginHandler(final ApplicationEventPublisher applicationEventPublisher) {
        this.$ = applicationEventPublisher;
    }

    public void web3login(final String address) {
        SecurityContextHolder.getContext().setAuthentication(new Web3Authentication(prettifyAddress(address)));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(address, "WEB3");
    }

    public void arkaneLogin(final String bearerToken, final String address, final String walletId) {
        SecurityContextHolder.getContext().setAuthentication(new ArkaneAuthentication(prettifyAddress(address), bearerToken, walletId));
        SecurityContextHolder.getContext().getAuthentication().setAuthenticated(true);
        throwEvent(address, "ARKANE");
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
