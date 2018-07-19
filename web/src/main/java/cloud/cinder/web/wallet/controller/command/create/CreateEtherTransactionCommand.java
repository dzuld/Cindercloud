package cloud.cinder.web.wallet.controller.command.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateEtherTransactionCommand {

    @NotEmpty
    private String to;
    private String gasPrice;
    @Min(21000)
    private BigInteger gasLimit = BigInteger.valueOf(31000);
    private double amount;

    public BigInteger amountInWei() {
        final BigDecimal bigDecimal = new BigDecimal(amount, MathContext.DECIMAL64);
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 18)));
        return weiValue.toBigInteger();
    }

    public BigInteger gasPriceInWei() {
        final BigDecimal bigDecimal = new BigDecimal(String.valueOf(gasPrice));
        final BigDecimal weiValue = bigDecimal.multiply(BigDecimal.valueOf(Math.pow(10, 9)));
        return new BigInteger(weiValue.toPlainString());
    }
}
