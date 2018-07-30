package cloud.cinder.web.ethereum;

import cloud.cinder.core.coinmarketcap.dto.Currency;
import cloud.cinder.core.coinmarketcap.service.PriceService;
import cloud.cinder.core.ethereum.block.service.BlockService;
import cloud.cinder.core.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class EthereumIndexController {

    @Autowired
    private PriceService priceService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;

    @Value("${cloud.cinder.wallet.baseUrl}")
    private String walletBaseUrl;

    @GetMapping
    public String redirect() {
        return "redirect:" + walletBaseUrl;
    }

    @GetMapping(value = "/ethereum")
    public String ethereum(final ModelMap modelMap) {
        modelMap.put("ethPriceUsd", priceService.getPriceAsString(Currency.USD));
        modelMap.put("ethPriceEur", priceService.getPriceAsString(Currency.EUR));
        modelMap.put("lastBlock", blockService.getLastBlock().toBlocking().first().getBlockNumber().longValue());
        return "index";
    }

    @GetMapping("/vechain")
    public String vechain() {
        return "redirect:/vechain/blocks";
    }

    @RequestMapping(value = "/ethereum", params = "section=blocks")
    public String getLast10Blocks(final ModelMap model) {
        model.put("blocks", blockService.getLast10IndexedBlocks());
        return "components/index :: blocks";
    }

    @RequestMapping(value = "/ethereum", params = "section=transactions")
    public String getLast10Transactions(final ModelMap model) {
        model.put("transactions", transactionService.getLast10Transactions());
        return "components/index :: transactions";
    }

}
