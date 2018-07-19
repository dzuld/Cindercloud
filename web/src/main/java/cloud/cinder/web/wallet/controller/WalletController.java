package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.coinmarketcap.service.PriceService;
import cloud.cinder.web.transaction.service.TransactionService;
import cloud.cinder.web.wallet.service.AuthenticationService;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import cloud.cinder.ethereum.util.EthUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.math.BigInteger;
import java.text.DecimalFormat;

@Controller
@RequestMapping("/wallet")
@Slf4j
public class WalletController {

    private static final DecimalFormat currencyFormat = new DecimalFormat("#.00");

    @Autowired
    private AddressService addressService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final BigInteger balance = addressService.getBalance(address).toBlocking().first();
        modelMap.put("balance", EthUtil.format(balance));
        modelMap.put("balEUR", formatCurrency(priceService.getPrice(Currency.EUR) * EthUtil.asEth(balance)));
        modelMap.put("balUSD", formatCurrency(priceService.getPrice(Currency.USD) * EthUtil.asEth(balance)));
        modelMap.put("hasBalance", balance.longValue() != 0);
        modelMap.put("address", address);
        return "wallets/index";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/transactions")
    public String transactions(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        final Slice<Transaction> transactions = transactionService.findByAddress(address, new PageRequest(0, 10)).toBlocking().first();
        modelMap.put("transactions", transactions);
        modelMap.put("address", address);
        return "wallets/transactions";
    }

    private String formatCurrency(final double v) {
        return currencyFormat.format(v);
    }

}
