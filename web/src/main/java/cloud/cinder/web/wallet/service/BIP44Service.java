package cloud.cinder.web.wallet.service;

import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.wallet.DeterministicKeyChain;
import org.bitcoinj.wallet.DeterministicSeed;
import org.bitcoinj.wallet.UnreadableWalletException;
import org.springframework.stereotype.Service;
import org.web3j.crypto.ECKeyPair;

import javax.validation.constraints.NotNull;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BIP44Service {

    public static final ChildNumber BIP44_PURPOSE = new ChildNumber(44, true);
    public static final ChildNumber ETHEREUM_COIN_TYPE = new ChildNumber(60, true);
    public static final ChildNumber DEFAULT_ACCOUNT = ChildNumber.ZERO_HARDENED;
    public static final ChildNumber DEFAULT_CHANGE = ChildNumber.ZERO;

    public ECKeyPair fromMnemonic(final String mnemonic) {
        return fromMnemonic(mnemonic, 0);
    }

    public ECKeyPair fromMnemonic(final String mnemonic, final int index) {
        final DeterministicSeed deterministicSeed = createSeed(mnemonic.trim());
        final DeterministicKeyChain deterministicKeyChain = DeterministicKeyChain.builder().seed(deterministicSeed).build();

        final List<ChildNumber> childNumbers = new ArrayList<>();
        childNumbers.add(BIP44_PURPOSE);
        childNumbers.add(ETHEREUM_COIN_TYPE);
        childNumbers.add(DEFAULT_ACCOUNT);
        childNumbers.add(DEFAULT_CHANGE);
        childNumbers.add(new ChildNumber(index));
        final byte[] privKey = deterministicKeyChain.getKeyByPath(childNumbers, true).getPrivKeyBytes();
        return ECKeyPair.create(privKey);
    }

    public String generateMnemonicCode() {
        return new DeterministicSeed(new SecureRandom(), 128, "", 0).getMnemonicCode()
                .stream()
                .collect(Collectors.joining(" "));
    }

    @NotNull
    private DeterministicSeed createSeed(final String mnemonic) {
        try {
            return new DeterministicSeed(mnemonic, null, "", 0);
        } catch (UnreadableWalletException e) {
            throw new IllegalArgumentException("Unable to create seed from mnemonic code", e);
        }
    }
}
