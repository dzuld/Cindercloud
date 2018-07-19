package cloud.cinder.web.wallet.controller.command.confirm;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmTokenTransactionCommand {

    @NotEmpty
    private String to;
    private String gasPrice;
    private BigInteger gasPriceInWei;
    private String tokenAddress;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;
    private BigInteger amountInWei;
}
