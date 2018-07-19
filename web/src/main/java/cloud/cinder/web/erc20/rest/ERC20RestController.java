package cloud.cinder.web.erc20.rest;

import cloud.cinder.web.token.service.TokenService;
import cloud.cinder.web.wallet.service.AuthenticationService;
import cloud.cinder.ethereum.erc20.service.ERC20Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;

@RestController
@RequestMapping(value = "/rest/erc20")
public class ERC20RestController {

    private final DecimalFormat formatter = new DecimalFormat("##.######");

    private final ERC20Service erc20Service;
    private final TokenService tokenService;
    private AuthenticationService authenticationService;

    public ERC20RestController(final ERC20Service erc20Service,
                               final TokenService tokenService,
                               final AuthenticationService authenticationService) {
        this.erc20Service = erc20Service;
        this.tokenService = tokenService;
        this.authenticationService = authenticationService;
    }

    @RequestMapping(value = "/{tokenAddress}/balance")
    public String balanceOf(final @RequestParam("address") String address, final @PathVariable("tokenAddress") String tokenAddress) {
        return formatter.format(erc20Service.balanceOf(address, tokenAddress).doubleValue());
    }

    @GetMapping(value = "/refresh")
    public ResponseEntity<String> refresh() {
        authenticationService.requireAuthenticated();
        tokenService.findAll()
                .forEach(x -> erc20Service.evictBalanceOf(authenticationService.getAddress(), x.getAddress()));
        return ResponseEntity.ok("refreshed");
    }
}
