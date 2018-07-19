package cloud.cinder.web.coinmarketcap.rest;

import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.coinmarketcap.service.PriceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/rest/price")
public class PriceRestController {

    @Autowired
    private PriceService priceService;

    @RequestMapping(method = RequestMethod.GET)
    public String getPrice(@RequestParam(name = "currency", defaultValue = "USD") final Currency currency) {
        return priceService.getPriceAsString(currency);
    }
}
