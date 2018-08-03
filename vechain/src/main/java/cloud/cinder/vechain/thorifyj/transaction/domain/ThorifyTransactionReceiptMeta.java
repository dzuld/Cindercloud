package cloud.cinder.vechain.thorifyj.transaction.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class ThorifyTransactionReceiptMeta {

    @JsonProperty("blockID")
    private String blockId;
    private BigInteger blockNumber;
    private Long blockTimestamp;
    @JsonProperty("txID")
    private String transactionId;
    @JsonProperty("txOrigin")
    private String transactionOrigin;
}
