package cloud.cinder.web.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.Keys;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@AllArgsConstructor
@Getter
public class GeneratedCredentials {

    final DateFormat formatter = new SimpleDateFormat("'UTC--'yyyy-MM-dd'T'HH-mm-ss.SSS'Z'");

    private String wallet;
    private PrivateKey privateKey;

    public String getFilename() {
        return formatter.format(new Date()) + "--" + Credentials.create(privateKey.asString()).getAddress() + ".json";
    }

    public String getAddress() {
        return prettifyAddress(Keys.getAddress(ECKeyPair.create(privateKey.getPrivateKey())));
    }
}
