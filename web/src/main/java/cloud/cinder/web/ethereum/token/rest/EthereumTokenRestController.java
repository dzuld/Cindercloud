package cloud.cinder.web.ethereum.token.rest;

import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.core.token.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/rest/tokens")
public class EthereumTokenRestController {

    private TokenService tokenService;

    public EthereumTokenRestController(final TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @RequestMapping(method = GET)
    public List<Token> tokens() {
        return tokenService.findAll();
    }

}
