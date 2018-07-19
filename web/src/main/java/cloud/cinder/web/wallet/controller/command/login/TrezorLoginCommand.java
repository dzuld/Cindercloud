package cloud.cinder.web.wallet.controller.command.login;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bitcoinj.core.ECKey;
import org.bouncycastle.util.encoders.Hex;

@Data
@NoArgsConstructor
public class TrezorLoginCommand {

    private String xpubkey;
    private String publicKey;
    private String chainCode;
    private String address;
    private String path;

    public ECKey asPublicKey() {
        return ECKey.fromPublicOnly(Hex.decode(publicKey));
    }
}
