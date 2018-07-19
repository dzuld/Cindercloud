package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.wallet.service.AuthenticationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet/kyber")
@Slf4j
public class WalletKyberController {

    private final AuthenticationService authenticationService;

    public WalletKyberController(final AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @GetMapping
    public String index(final ModelMap modelMap) {
        authenticationService.requireAuthenticated();
        modelMap.addAttribute("authenticationType", authenticationService.getType());
        modelMap.put("address", SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        return "wallets/kyber";
    }
}
