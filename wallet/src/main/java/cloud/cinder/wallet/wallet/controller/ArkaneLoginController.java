package cloud.cinder.wallet.wallet.controller;

import cloud.cinder.core.login.handler.LoginHandler;
import cloud.cinder.core.security.domain.ArkaneAuthentication;
import cloud.cinder.wallet.wallet.controller.dto.ArkaneLoginDto;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@Controller
@RequestMapping(value = "/arkane/login")
public class ArkaneLoginController {

    private LoginHandler loginHandler;

    public ArkaneLoginController(final LoginHandler loginHandler) {
        this.loginHandler = loginHandler;
    }

    @RequestMapping(method = GET)
    public String index() {
        return "wallets/arkane";
    }

    @RequestMapping(method = POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public @ResponseBody
    ArkaneLoginDto login(@RequestBody final ArkaneLoginDto arkaneLoginDto) {
        loginHandler.arkaneLogin(arkaneLoginDto.getBearerToken(), arkaneLoginDto.getAddress(), arkaneLoginDto.getWalletId());
        return arkaneLoginDto;
    }

    @GetMapping("/token")
    public @ResponseBody
    ArkaneLoginDto getBearerToken() {
        if (SecurityContextHolder.getContext().getAuthentication() instanceof ArkaneAuthentication) {
            return new ArkaneLoginDto(SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString(),
                    SecurityContextHolder.getContext().getAuthentication().getCredentials().toString(),
                    ((ArkaneAuthentication) SecurityContextHolder.getContext().getAuthentication()).getWalletId());
        } else {
            return null;
        }
    }
}
