package cloud.cinder.common.crypto;

import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Utils;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;
import org.bouncycastle.util.encoders.Hex;
import org.web3j.crypto.ECKeyPair;

/**
 * HDAddress.java : an address in a BIP44 wallet account chain
 */
public class HDAddress {

    private int childNum;
    private String strPath = null;
    private ECKeyPair ecKey = null;
    private byte[] pubKey = null;
    private byte[] pubKeyHash = null;

    private NetworkParameters params = null;

    /**
     * Constructor an HD address.
     *
     * @param params NetworkParameters
     * @param cKey   deterministic key for this address
     * @param child  index of this address in its chain
     */
    public HDAddress(NetworkParameters params, DeterministicKey cKey, int child) {

        this.params = params;
        childNum = child;

        DeterministicKey dk = HDKeyDerivation.deriveChildKey(cKey, new ChildNumber(childNum, false));
        // compressed WIF private key format
        if (dk.hasPrivKey()) {
            ecKey = ECKeyPair.create(dk.getPrivKeyBytes());
        } else {
            ecKey = ECKeyPair.create(dk.getPubKey());
        }

        long now = Utils.now().getTime() / 1000;    // use Unix time (in seconds)

        pubKey = ecKey.getPublicKey().toByteArray();
        pubKeyHash = ecKey.getPublicKey().toByteArray();

        strPath = dk.getPathAsString();
    }

    /**
     * Get pubKey as byte array.
     *
     * @return byte[]
     */
    public byte[] getPubKey() {
        return pubKey;
    }

    /**
     * Get pubKeyHash as byte array.
     *
     * @return byte[]
     */
    public byte[] getPubKeyHash() {
        return pubKeyHash;
    }

    /**
     * Return public address for this instance.
     *
     * @return String
     */
    public String getAddressString() {
        return Hex.toHexString(ecKey.getPrivateKey().toByteArray());
    }

    /**
     * Return private key for this address (compressed WIF format).
     *
     * @return String
     */
    public String getPrivateKeyString() {

        return Hex.toHexString(ecKey.getPrivateKey().toByteArray());
    }

    /**
     * Return BIP44 path for this address (m / purpose' / coin_type' / account' / chain /
     * address_index).
     *
     * @return String
     */
    public String getPath() {
        return strPath;
    }

    public int getChildNum() {
        return childNum;
    }

}