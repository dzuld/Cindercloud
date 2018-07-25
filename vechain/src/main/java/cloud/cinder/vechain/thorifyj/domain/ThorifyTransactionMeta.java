package cloud.cinder.vechain.thorifyj.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class ThorifyTransactionMeta {
    @JsonProperty("blockID")
    private String blockId;
    private BigDecimal blockNumber;
    private Long blockTimestamp;
}
