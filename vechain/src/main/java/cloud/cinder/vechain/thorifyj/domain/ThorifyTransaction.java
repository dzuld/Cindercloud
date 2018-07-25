package cloud.cinder.vechain.thorifyj.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@NoArgsConstructor
@Data
public class ThorifyTransaction {
    private String id;
    private Long chainTag;
    private String blockRef;
    private Long expiration;
    private Long gasPriceCoef;
    private BigInteger gas;
    private String origin;
    private String nonce;
    private String dependsOn;
    private Long size;
    private ThorifyTransactionMeta meta;
}
