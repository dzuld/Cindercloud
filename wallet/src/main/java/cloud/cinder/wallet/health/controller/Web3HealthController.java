package cloud.cinder.wallet.health.controller;

import cloud.cinder.ethereum.web3j.Web3jGateway;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.web3j.protocol.core.DefaultBlockParameterName;

import java.io.IOException;

@RestController
@RequestMapping("/health/web3")
public class Web3HealthController {

    private Web3jGateway web3jGateway;

    private Web3HealthController(final Web3jGateway web3jGateway) {
        this.web3jGateway = web3jGateway;
    }

    @GetMapping
    public String getLastBlock() throws IOException {
        return web3jGateway.websocket().ethGetBlockByNumber(DefaultBlockParameterName.LATEST, false).send().getBlock().getNumber().toString();
    }
}
