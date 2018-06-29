package cloud.cinder.web.wallet.service;

import cloud.cinder.web.security.domain.AuthenticationType;
import cloud.cinder.web.security.domain.ClientSideAuthentication;
import cloud.cinder.web.security.domain.HardwareWalletAuthentication;
import cloud.cinder.web.security.domain.PrivateKeyAuthentication;
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
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            log.trace("Logged in using private key");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ClientSideAuthentication)) {
            log.trace("Logged in using web3 authentication");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            log.trace("Logged in using web3 authentication");
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public String getAddress() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            return prettifyAddress(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString());
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ClientSideAuthentication)) {
            return prettifyAddress((SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString());
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            return prettifyAddress((SecurityContextHolder.getContext().getAuthentication()).getPrincipal().toString());
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    private ECKeyPair getPrivateKey() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            return ((ECKeyPair) SecurityContextHolder.getContext().getAuthentication().getCredentials());
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ClientSideAuthentication)) {
            throw new InsufficientAuthenticationException("Authenticated with Web3");
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            throw new InsufficientAuthenticationException("Authenticated with Hardware Wallet");
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public AuthenticationType getType() {
        if ((SecurityContextHolder.getContext().getAuthentication() instanceof PrivateKeyAuthentication)) {
            log.trace("Logged in using private key");
            return AuthenticationType.CINDERCLOUD;
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof ClientSideAuthentication)) {
            log.trace("Logged in using web3 authentication");
            return AuthenticationType.WEB3;
        } else if ((SecurityContextHolder.getContext().getAuthentication() instanceof HardwareWalletAuthentication)) {
            log.trace("Logged in using web3 authentication");
            return AuthenticationType.TREZOR;
        } else {
            throw new InsufficientAuthenticationException("Not authenticated");
        }
    }

    public byte[] sign(final RawTransaction etherTransaction) {
        return TransactionEncoder.signMessage(etherTransaction, Credentials.create(getPrivateKey()));
    }
}
