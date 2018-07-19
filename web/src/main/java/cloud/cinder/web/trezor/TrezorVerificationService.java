package cloud.cinder.web.trezor;

import org.bitcoinj.core.ECKey;
import org.bitcoinj.core.Sha256Hash;
import org.bitcoinj.core.VarInt;
import org.bouncycastle.util.Arrays;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;

import static org.bitcoinj.core.Utils.BITCOIN_SIGNED_MESSAGE_HEADER_BYTES;

@Component
public class TrezorVerificationService {

    public boolean verify(final String randomChallenge,
                          final String visualChallenge,
                          final String publicKey,
                          final String signature) {
        try {
            final byte[] random_challenge = Hex.decode(randomChallenge);
            final byte[] sigBytes = Hex.decode(signature);
            final byte[] hiddenChallenge_Sha = Sha256Hash.hash(random_challenge);
            final byte[] visualChallenge_Sha = Sha256Hash.hash(visualChallenge.getBytes());

            final ECKey pubKey = ECKey.fromPublicOnly(Hex.decode(publicKey));

            final ECKey.ECDSASignature ecdsaSignature = new ECKey.ECDSASignature(
                    new BigInteger(1, Arrays.copyOfRange(sigBytes, 1, 33)),
                    new BigInteger(1, Arrays.copyOfRange(sigBytes, 33, 65)));
            final byte[] message = Arrays.concatenate(hiddenChallenge_Sha, visualChallenge_Sha);
            return ECKey.verify(Sha256Hash.hashTwice(formatMessageForSigning(message)), ecdsaSignature, pubKey.getPubKey());
        } catch (final Exception exceptione) {
            exceptione.printStackTrace();
            return false;
        }
    }

    private byte[] formatMessageForSigning(final byte[] input) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bos.write(BITCOIN_SIGNED_MESSAGE_HEADER_BYTES.length);
            bos.write(BITCOIN_SIGNED_MESSAGE_HEADER_BYTES);
            VarInt size = new VarInt(input.length);
            bos.write(size.encode());
            bos.write(input);
            return bos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
