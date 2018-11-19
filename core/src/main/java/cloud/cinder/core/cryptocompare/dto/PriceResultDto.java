package cloud.cinder.core.cryptocompare.dto;

import cloud.cinder.core.coinmarketcap.dto.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class PriceResultDto implements Serializable {

    static final long serialVersionUID = 1L;

    @JsonProperty("EUR")
    private String EUR;
    @JsonProperty("USD")
    private String USD;
    @JsonProperty("BTC")
    private String BTC;
    @JsonProperty("ETH")
    private String ETH;

    public String getFor(final Currency currency) {
        switch (currency) {
            case EUR:
                return EUR;
            case USD:
                return USD;
            case ETH:
                return ETH;
            case BTC:
                return BTC;
            default:
                return EUR;
        }
    }
}
