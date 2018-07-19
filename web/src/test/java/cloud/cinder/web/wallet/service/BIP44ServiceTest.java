package cloud.cinder.web.wallet.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.runners.MockitoJUnitRunner;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class BIP44ServiceTest {

    @InjectMocks
    private BIP44Service bip44Service;

    @Test
    public void standardMnemonic() {
        final ECKeyPair keyPair = bip44Service.fromMnemonic("spoil ranch brother foster release list arrest hammer net search hand hint");
        assertThat(Credentials.create(keyPair).getAddress()).isEqualToIgnoringCase("0x2ee45694140d5a9fbbb9757e876747c4c86ba3f5");
    }

    @Test
    public void standardMnemonicWithIndex() {
        final ECKeyPair keyPair = bip44Service.fromMnemonic("symptom twin initial online slab road ahead post twenty tissue outside asset", 9);
        assertThat(Credentials.create(keyPair).getAddress()).isEqualToIgnoringCase("0x058da4b43cee210cdb0b76abcce53850058dbf99");
    }
}