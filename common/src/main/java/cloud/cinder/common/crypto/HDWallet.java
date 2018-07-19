package cloud.cinder.common.crypto;

import com.google.common.base.Joiner;
import org.apache.commons.codec.binary.Hex;
import org.bitcoinj.core.AddressFormatException;
import org.bitcoinj.crypto.*;

import java.util.ArrayList;
import java.util.List;

/**
 * HDWallet.java : BIP44 wallet
 */
public class HDWallet {

    private byte[] seed = null;
    private String strPassphrase = null;
    private List<String> wordList = null;

    private DeterministicKey dkKey = null;
    private DeterministicKey dkRoot = null;

    private ArrayList<HDAccount> accounts = null;

    private String strPath = null;

    /**
     * Constructor for wallet.
     *
     * @param mc         mnemonic code object
     * @param seed       seed for this wallet
     * @param passphrase optional BIP39 passphrase
     * @param nbAccounts number of accounts to create
     */
    public HDWallet(MnemonicCode mc, byte[] seed, String passphrase, int nbAccounts) throws MnemonicException.MnemonicLengthException {

        this.seed = seed;
        strPassphrase = passphrase;

        wordList = mc.toMnemonic(seed);
        byte[] hd_seed = MnemonicCode.toSeed(wordList, strPassphrase);
        dkKey = HDKeyDerivation.createMasterPrivateKey(hd_seed);
        DeterministicKey dKey = HDKeyDerivation.deriveChildKey(dkKey, 44 | ChildNumber.HARDENED_BIT);
        dkRoot = HDKeyDerivation.deriveChildKey(dKey, ChildNumber.HARDENED_BIT);

        accounts = new ArrayList<>();
        for (int i = 0; i < nbAccounts; i++) {
            accounts.add(new HDAccount(dkRoot, i));
        }

        strPath = dKey.getPathAsString();
    }

    /**
     * Constructor for watch-only wallet initialized from submitted XPUB(s).
     *
     * @param xpubs arrayList of XPUB strings
     */
    public HDWallet(ArrayList<String> xpubs) throws AddressFormatException {

        accounts = new ArrayList<>();

        int i = 0;
        for (String xpub : xpubs) {
            accounts.add(new HDAccount(xpub, i));
            i++;
        }
    }

    /**
     * Return wallet seed as byte array.
     *
     * @return byte[]
     */
    public byte[] getSeed() {
        return seed;
    }

    /**
     * Return wallet seed as hex string.
     *
     * @return String
     */
    public String getSeedHex() {
        return Hex.encodeHexString(seed);
    }

    /**
     * Return wallet BIP39 mnemonic as string containing space separated words.
     *
     * @return String
     */
    @Deprecated
    public String getMnemonicOld() {
        return Joiner.on(" ").join(wordList);
    }

    public List<String> getMnemonic() {
        return wordList;
    }

    /**
     * Return wallet BIP39 passphrase.
     *
     * @return String
     */
    public String getPassphrase() {
        return strPassphrase;
    }

    /**
     * Return accounts for this wallet.
     *
     * @return List<HDAccount>
     */
    public List<HDAccount> getAccounts() {
        return accounts;
    }

    /**
     * Return account for submitted account id.
     *
     * @return HDAccount
     */
    public HDAccount getAccount(int accountId) {
        return accounts.get(accountId);
    }

    /**
     * Add new account.
     */
    public HDAccount addAccount() {
        HDAccount account = new HDAccount(dkRoot, accounts.size());
        accounts.add(account);

        return account;
    }

    /**
     * Return BIP44 path for this wallet (m / purpose').
     *
     * @return String
     */
    public String getPath() {
        return strPath;
    }


    public DeterministicKey getMasterKey() {
        return dkKey;
    }

}