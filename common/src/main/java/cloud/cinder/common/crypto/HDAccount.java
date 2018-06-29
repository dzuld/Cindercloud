package cloud.cinder.common.crypto;

import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.core.Base58;
import org.bitcoinj.crypto.ChildNumber;
import org.bitcoinj.crypto.DeterministicKey;
import org.bitcoinj.crypto.HDKeyDerivation;

import java.nio.ByteBuffer;

/**
 * HDAccount.java : an account in a BIP44 wallet
 */
public class HDAccount {

    private DeterministicKey aKey = null;
    private int aID;

    private String strXPUB = null;
    private String strPath = null;

    /**
     * Constructor for account.
     *
     * @param wKey  deterministic key for this account
     * @param child id within the wallet for this account
     */
    public HDAccount(DeterministicKey wKey, int child) {
        aID = child;
        // L0PRV & STDVx: private derivation.
        int childnum = child;
        childnum |= ChildNumber.HARDENED_BIT;
        aKey = HDKeyDerivation.deriveChildKey(wKey, childnum);
        strPath = aKey.getPathAsString();

    }

    /**
     * Constructor for watch-only account.
     *
     * @param xpub  XPUB for this account
     * @param child id within the wallet for this account
     */
    public HDAccount(String xpub, int child) throws AddressFormatException {

        aID = child;

        // assign master key to account key
        aKey = createMasterPubKeyFromXPub(xpub);

        strXPUB = xpub;
    }

    /**
     * Constructor for watch-only account.
     *
     * @param xpub XPUB for this account
     */
    public HDAccount(String xpub) throws AddressFormatException {
        // assign master key to account key
        aKey = createMasterPubKeyFromXPub(xpub);
        strXPUB = xpub;
    }

    /**
     * Restore watch-only account deterministic public key from XPUB.
     *
     * @return DeterministicKey
     */
    private DeterministicKey createMasterPubKeyFromXPub(String xpubstr) throws AddressFormatException {


        byte[] xpubBytes = Base58.decodeChecked(xpubstr);

        ByteBuffer bb = ByteBuffer.wrap(xpubBytes);


        byte[] chain = new byte[32];
        byte[] pub = new byte[33];
        // depth:
        bb.get();
        // parent fingerprint:
        bb.getInt();
        // child no.
        bb.getInt();
        bb.get(chain);
        bb.get(pub);

        return HDKeyDerivation.createMasterPubKeyFromBytes(pub, chain);
    }

    /**
     * Return XPUB string for this account.
     *
     * @return String
     */
    public String getXpub() {
        return strXPUB;
    }


    /**
     * Return BIP44 path for this account (m / purpose' / coin_type' / account').
     *
     * @return String
     */
    public String getPath() {
        return strPath;
    }

}