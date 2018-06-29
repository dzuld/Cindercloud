package cloud.cinder.web.wallet.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigInteger;

@AllArgsConstructor
@Getter
public class PrivateKey {

    private BigInteger privateKey;

    public String asString() {
        return "0x" + privateKey.toString(16);
    }
}
