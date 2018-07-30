package cloud.cinder.wallet.infrastructure.controller;

import cloud.cinder.common.event.domain.EventType;
import cloud.cinder.core.event.service.EventService;
import cloud.cinder.core.login.service.LoginEventService;
import cloud.cinder.core.token.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/")
public class IndexController {

    private final LoginEventService loginEventService;
    private final TokenService tokenService;
    private final EventService eventService;

    public IndexController(
            final LoginEventService loginEventService,
            final TokenService tokenService,
            final EventService eventService) {
        this.loginEventService = loginEventService;
        this.tokenService = tokenService;
        this.eventService = eventService;
    }


    @GetMapping("/")
    public String landing(final ModelMap modelMap) {
        modelMap.put("loginCount", loginEventService.totalLogins());
        modelMap.put("supportedTokenCount", tokenService.count());
        modelMap.put("generatedWalletCount", eventService.countByType(EventType.WALLET_CREATED) + 1241 /* take into account the amount of wallets pre-event table */);
        return "landing";
    }

}
