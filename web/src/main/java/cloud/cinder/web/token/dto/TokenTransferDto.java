package cloud.cinder.web.token.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TokenTransferDto {

    private static final DecimalFormat decimalFormat = new DecimalFormat("##.######");

    private String from;
    private String to;
    private String transactionHash;
    private BigInteger blockHeight;
    private String blockHash;
    private Date blockTimestamp;
    private String tokenName;
    private String tokenSymbol;
    private String tokenAddress;
    private BigInteger amount;
    private int decimals;

    public String prettyFrom() {
        if (from == null || from.contains("0000000000000000000000000000000000000000")) {
            return "Genesis";
        } else {
            return from.substring(0, 18) + "...";
        }
    }

    public String prettyTo() {
        if (to == null || to.contains("0000000000000000000000000000000000000000")) {
            return "Burned";
        } else {
            return to.substring(0, 18) + "...";
        }
    }

    public String prettyTransactionHash() {
        return transactionHash.substring(0, 18) + "...";
    }

    public String prettyAmount() {
        final BigDecimal rawBalance = new BigDecimal(amount);
        final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
        return decimalFormat.format(rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN).doubleValue());
    }

    public String prettyBlockTimestamp() {
        final PrettyTime prettyTime = new PrettyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(blockTimestamp);
    }
}
