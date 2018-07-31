package cloud.cinder.web.ethereum.token.controller;

import cloud.cinder.core.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tokens")
public class EthereumTokenController {

    private TokenService tokenService;

    public EthereumTokenController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("tokens", tokenService.findAll(new PageRequest(0, 25)));
        return "tokens/index";
    }
}
