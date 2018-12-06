package cloud.cinder.ethereum.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.scheduling.annotation.Scheduled;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.net.URI;

@Configuration
@Slf4j
public class Web3Config {

    private WebSocketClient arkaneClient;
    private WebSocketClient cinderClient;

    @Scheduled(fixedDelay = 20_000)
    private void reconnect() {
        if (arkaneClient != null && !arkaneClient.isOpen()) {
            try {
                arkaneClient.connect();
            } catch (final Exception ex) {
                log.error("Unable to reconnect to arkane ws {}", ex.getMessage());
            }
        }

        if (cinderClient != null && !cinderClient.isOpen()) {
            try {
                cinderClient.connect();
            } catch (final Exception ex) {
                log.error("Unable to reconnect to arkane ws {}", ex.getMessage());
            }
        }
    }

    @Bean
    @Primary
    public Web3j provideWeb3J(final Web3jService web3jService) {
        return Web3j.build(web3jService);
    }

    @Bean
    @Qualifier("websocket")
    public Web3j provideWebsocketWeb3j(@Qualifier("websocket") final Web3jService web3jService) {
        return Web3j.build(web3jService);
    }

    @Bean
    @Primary
    public Web3jService provideWeb3JService(@Value("${cloud.cinder.ethereum.endpoint.url}") final String endpoint) throws ConnectException {
        arkaneClient = new WebSocketClient(URI.create(endpoint));
        final WebSocketService webSocketService = new WebSocketService(arkaneClient, false);
        webSocketService.connect();
        return webSocketService;
    }

    @Bean
    @Qualifier("websocket")
    public Web3jService provideWebsocketEndpoint(@Value("${cloud.cinder.ethereum.endpoint.websocket-url}") final String endpoint) throws ConnectException {
        cinderClient = new WebSocketClient(URI.create(endpoint));
        final WebSocketService webSocketService = new WebSocketService(cinderClient, false);
        webSocketService.connect();
        return webSocketService;
    }
}