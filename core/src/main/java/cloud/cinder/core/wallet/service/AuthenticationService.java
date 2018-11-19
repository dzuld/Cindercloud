package cloud.cinder.core.wallet.service;

import cloud.cinder.core.security.domain.ArkaneAuthentication;
import cloud.cinder.core.security.domain.AuthenticationType;
import cloud.cinder.core.security.domain.HardwareWalletAuthentication;
import cloud.cinder.core.security.domain.Web3Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Service
@Slf4j
public class AuthenticationService {

    public void requireAuthenticated() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof Web3Authentication)) {
            log.trace("Logged in using web3 authentication");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            log.trace("Logged in using web3 authentication");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ArkaneAuthentication)) {
            log.trace("Logged in using Arkane authentication");
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public String getAddress() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof Web3Authentication)) {
            return prettifyAddress((SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString());
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            return prettifyAddress((SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString());
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ArkaneAuthentication)) {
            return prettifyAddress((SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString());
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public AuthenticationType getType() {
         if ((SecurityContextHolder.getContext().getAuthentication() instanceof Web3Authentication)) {
            log.trace("Logged in using web3 authentication");
            return AuthenticationType.WEB3;
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            log.trace("Logged in using web3 authentication");
            return AuthenticationType.TREZOR;
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ArkaneAuthentication)) {
            log.trace("Logged in using Arkane authentication");
            return AuthenticationType.ARKANE;
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public byte[] sign(final RawTransaction etherTransaction) {
        return TransactionEncoder.signMessage(etherTransaction, Credentials.create(""));
    }
}
