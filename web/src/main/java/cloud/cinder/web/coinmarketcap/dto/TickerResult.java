package cloud.cinder.web.coinmarketcap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TickerResult {

    private String id;
    private String name;
    private String symbol;
    @JsonProperty("price_usd")
    private String priceUsd;
    @JsonProperty("price_btc")
    private String priceBtc;
    @JsonProperty("price_eur")
    private String priceEur;
    @JsonProperty("24h_volume_usd")
    private String volumeUsd;
    @JsonProperty("available_supply")
    private String availableSupply;
    @JsonProperty("total_supply")
    private String totalSupply;
    @JsonProperty("percent_change_1h")
    private String percentChange1h;
    @JsonProperty("percent_change_24h")
    private String percentChange24h;
    @JsonProperty("percent_change_7d")
    private String percentChange7d;
    @JsonProperty("lastUpdated")
    private Long lastUpdated;

    public String forCurrency(final Currency currency) {
        switch (currency) {
            case BTC:
                return getPriceBtc();
            case EUR:
                return getPriceEur();
            default:
                return getPriceUsd();
        }
    }

}
