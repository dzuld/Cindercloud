package cloud.cinder.vechain.thorifyj.transaction.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@NoArgsConstructor
@Data
public class ThorifyTransaction {
    private String id;

    /**
     * last byte of genesis block ID
     */
    private Long chainTag;

    /**
     * 8 bytes prefix of some block ID
     */
    private String blockRef;

    /**
     * expiration relative to blockRef, in unit block
     */
    private BigInteger expiration;

    /**
     * coefficient used to calculate the final gas price
     */
    private BigInteger gasPriceCoef;

    /**
     * max amount of gas can be consumed to execute this transaction
     */
    private BigInteger gas;

    /**
     * The one who signed the transaction
     */
    private String origin;
    private String nonce;

    /**
     * ID of the transaction on which the current transaction depends (bytes32)
     */
    @JsonProperty("depends_on")
    private String dependsOn;
    private BigInteger size;
    private ThorifyTransactionMeta meta;
    private List<ThorifyTransactionClause> clauses;
}
