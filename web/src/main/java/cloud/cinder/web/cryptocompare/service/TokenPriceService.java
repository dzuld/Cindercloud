package cloud.cinder.web.cryptocompare.service;

import cloud.cinder.web.coinmarketcap.dto.Currency;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;

@Service
public class TokenPriceService {

    private static final DecimalFormat format = new DecimalFormat("###,###.00");

    private final CryptoCompareGateway cryptoCompareGateway;

    public TokenPriceService(final CryptoCompareGateway cryptoCompareGateway) {
        this.cryptoCompareGateway = cryptoCompareGateway;
    }

    public String getPriceAsString(final String symbol, final Currency currency) {
        final String price = cryptoCompareGateway.getPrice(symbol).getFor(currency);
        return price == null ? "0.00" : price;
    }

    public String getPriceAsString(final String symbol, final Currency currency, final double amount) {
        final double fullPrice = getPrice(symbol, currency) * amount;
        return format.format(fullPrice);
    }

    public double getPrice(final String symbol, final Currency currency) {
        final String price = cryptoCompareGateway.getPrice(symbol).getFor(currency);
        return price == null ? 0 : Double.valueOf(price);
    }
}
