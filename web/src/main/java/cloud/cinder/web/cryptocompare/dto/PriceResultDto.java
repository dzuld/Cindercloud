package cloud.cinder.web.cryptocompare.dto;

import cloud.cinder.web.coinmarketcap.dto.Currency;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceResultDto {

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
