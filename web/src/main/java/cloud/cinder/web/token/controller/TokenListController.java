package cloud.cinder.web.token.controller;

import cloud.cinder.web.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tokens")
public class TokenListController {

    @Autowired
    private TokenService tokenService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("tokens", tokenService.findAll(new PageRequest(0, 25)));
        return "tokens/index";
    }
}
