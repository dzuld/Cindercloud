package cloud.cinder.vechain.thorifyj.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
public class ThorifyTransactionMeta {
    @JsonProperty("blockID")
    private String blockId;
    private BigInteger blockNumber;
    private Long blockTimestamp;
}
