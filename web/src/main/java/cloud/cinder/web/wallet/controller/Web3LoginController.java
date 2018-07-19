package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.login.handler.LoginHandler;
import cloud.cinder.web.wallet.service.WalletService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/web3/login")
public class Web3LoginController {

    private WalletService walletService;
    private LoginHandler loginHandler;

    public Web3LoginController(final WalletService walletService,
                               final LoginHandler loginHandler) {
        this.walletService = walletService;
        this.loginHandler = loginHandler;
    }

    @RequestMapping(method = GET)
    public String index() {
        return "wallets/web3";
    }

    @RequestMapping(method = POST)
    public @ResponseBody
    String login(@RequestParam("address") final String address) {
        final String validatedAddress = walletService.loginWithWeb3(address);
        loginHandler.clientsideLogin(validatedAddress);
        return "OK";
    }
}
