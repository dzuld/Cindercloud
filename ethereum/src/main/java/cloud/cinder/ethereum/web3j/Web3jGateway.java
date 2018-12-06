package cloud.cinder.ethereum.web3j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.web3j.protocol.Web3j;

import javax.annotation.PostConstruct;

@Component
public class Web3jGateway {

    @Autowired
    private Web3j cindercloud;
    @Autowired
    @Qualifier("websocket")
    private Web3j websocketWeb3j;

    private Web3j currentProvider;

    @PostConstruct
    @Scheduled(fixedDelay = 20_000)
    private void init() {
        currentProvider = updateCurrentProvider();
    }

    public Web3j web3j() {
        return currentProvider;
    }

    public Web3j cindercloud() {
        return websocketWeb3j;
    }

    public Web3j websocket() {
        return websocketWeb3j;
    }

    private Web3j updateCurrentProvider() {
        try {
            websocketWeb3j.ethBlockNumber().send();
            return websocketWeb3j;
        } catch (final Exception ex) {
            return cindercloud;
        }
    }
}