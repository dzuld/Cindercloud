package cloud.cinder.web.infrastructure.controller;

import cloud.cinder.core.ethereum.block.service.BlockService;
import cloud.cinder.core.coinmarketcap.dto.Currency;
import cloud.cinder.core.coinmarketcap.service.PriceService;
import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.core.event.service.EventService;
import cloud.cinder.core.login.service.LoginEventService;
import cloud.cinder.core.token.service.TokenService;
import cloud.cinder.core.transaction.service.TransactionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final BlockService blockService;
    private final TransactionService transactionService;
    private final PriceService priceService;
    private final LoginEventService loginEventService;
    private final TokenService tokenService;
    private final EventService eventService;

    public IndexController(final BlockService blockService,
                           final TransactionService transactionService,
                           final PriceService priceService,
                           final LoginEventService loginEventService,
                           final TokenService tokenService,
                           final EventService eventService) {
        this.blockService = blockService;
        this.transactionService = transactionService;
        this.priceService = priceService;
        this.loginEventService = loginEventService;
        this.tokenService = tokenService;
        this.eventService = eventService;
    }

    @GetMapping("/ethereum")
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

    @GetMapping("/")
    public String landing(final ModelMap modelMap) {
        modelMap.put("loginCount", loginEventService.totalLogins());
        modelMap.put("supportedTokenCount", tokenService.count());
        modelMap.put("generatedWalletCount", eventService.countByType(EventType.WALLET_CREATED) + 1241 /* take into account the amount of wallets pre-event table */);
        return "landing";
    }


    @RequestMapping(params = "section=blocks")
    public String getLast10Blocks(final ModelMap model) {
        model.put("blocks", blockService.getLast10IndexedBlocks());
        return "components/index :: blocks";
    }

    @RequestMapping(params = "section=transactions")
    public String getLast10Transactions(final ModelMap model) {
        model.put("transactions", transactionService.getLast10Transactions());
        return "components/index :: transactions";
    }
}
