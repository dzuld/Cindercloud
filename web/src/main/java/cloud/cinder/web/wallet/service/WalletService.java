package cloud.cinder.web.wallet.service;

import cloud.cinder.common.event.domain.Event;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.web.wallet.domain.GeneratedCredentials;
import cloud.cinder.web.wallet.domain.PrivateKey;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.web3j.crypto.*;

import java.io.IOException;
import java.util.Objects;

import static org.springframework.util.StringUtils.isEmpty;

@Service
@Slf4j
public class WalletService {

    private static final ObjectMapper walletmapper = new ObjectMapper();
    private ApplicationEventPublisher $;
    private BIP44Service bip44Service;

    public WalletService(final ApplicationEventPublisher _$,
                         final BIP44Service bip44Service) {
        this.$ = _$;
        this.bip44Service = bip44Service;
    }

    static {
        walletmapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
        walletmapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public Credentials loginWithPrivateKey(final String privateKey) {
        validatePrivateKey(privateKey);
        try {
            return Credentials.create(privateKey);
        } catch (final Exception exc) {
            throw new IllegalArgumentException("The private key you provided was not valid");
        }
    }

    public Credentials loginWithWallet(final String password, final String wallet) {
        try {
            final WalletFile walletFile = walletmapper.readValue(wallet, WalletFile.class);
            return Credentials.create(Wallet.decrypt(password, walletFile));
        } catch (final IOException io) {
            if (!isEmpty(wallet) && wallet.length() < 100) {
                log.debug("Unable to login with wallet: {}", wallet);
            }
            throw new IllegalArgumentException("The keystore you provided is not valid");
        } catch (final CipherException cip) {
            log.debug("Tried to login with incorrect password");
            throw new IllegalArgumentException("Unable to decrypt wallet. Is your password correct?");
        }
    }

    public String loginWithWeb3(final String address) {
        validateAddress(address);
        return address;
    }

    public Credentials loginWithMnemonic(final String mnemonic, final int index) {
        try {
            final ECKeyPair keypair = bip44Service.fromMnemonic(mnemonic, index);
            return Credentials.create(keypair);
        } catch (final Exception e) {
            throw new IllegalArgumentException("Unable to login using the mnemonic phrase. Please try again or contact an admin.");
        }
    }

    private void validateAddress(final String address) {
        if (address == null || (address.length() != 40 && address.length() != 42)) {
            throw new IllegalArgumentException("The address you provided is not valid");
        }
    }

    public GeneratedCredentials generateWallet(final String password, final boolean strong) {
        try {
            Objects.requireNonNull(password);
            final ECKeyPair ecKeyPair = Keys.createEcKeyPair();
            final WalletFile walletFile = strong ? Wallet.createStandard(password, ecKeyPair) : Wallet.createLight(password, ecKeyPair);
            $.publishEvent(
                    Event.builder()
                            .message(walletFile.getAddress())
                            .type(EventType.WALLET_CREATED)
                            .build(
                            ));
            return new GeneratedCredentials(walletmapper.writeValueAsString(walletFile), new PrivateKey(ecKeyPair.getPrivateKey()));
        } catch (final Exception ex) {
            throw new IllegalArgumentException("Unable to generate wallet at this point");
        }
    }

    public String generateMnemonic() {
        return bip44Service.generateMnemonicCode();
    }

    private void validatePrivateKey(final String privateKey) {
        if (privateKey == null || (privateKey.length() != 64 && privateKey.length() != 66)) {
            throw new IllegalArgumentException("The private key you provided is not valid");
        }
    }
}
