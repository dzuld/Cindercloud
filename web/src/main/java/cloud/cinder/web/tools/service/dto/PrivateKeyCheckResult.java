package cloud.cinder.web.tools.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PrivateKeyCheckResult {

    private String address;
    private String balance;
    private BigInteger rawBalance;
    private String privateKey;
    private String txCount;
    private BigInteger rawTxCount;

    public boolean needsAttention() {
        return ((rawBalance != null && rawBalance.longValue() != 0) ||
                (txCount != null && rawTxCount.longValue() != 0));
    }

}
