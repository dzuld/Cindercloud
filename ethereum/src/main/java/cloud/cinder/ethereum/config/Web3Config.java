package cloud.cinder.ethereum.config;

import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;
import org.web3j.protocol.websocket.WebSocketClient;
import org.web3j.protocol.websocket.WebSocketService;

import java.net.ConnectException;
import java.net.URI;
import java.util.concurrent.TimeUnit;

@Configuration
public class Web3Config {

    private WebSocketClient webSocketClient;

    private void init() {
        if (webSocketClient != null && webSocketClient.isClosed()) {
            webSocketClient.reconnect();
        }
    }

    @Bean
    @Primary
    public Web3j provideWeb3J(final Web3jService web3jService) {
        return Web3j.build(web3jService);
    }

    @Bean
    @Qualifier("local")
    public Web3j provideInfuraWeb3j(@Qualifier("local") final Web3jService web3jService) {
        return Web3j.build(web3jService);
    }

    @Bean
    @Qualifier("websocket")
    public Web3j provideWebsocketWeb3j(@Qualifier("websocket") final Web3jService web3jService) {
        return Web3j.build(web3jService);
    }

    @Bean
    @Primary
    public Web3jService provideWeb3JService(@Value("${cloud.cinder.ethereum.endpoint.url}") final String endpoint) {
        final OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        return new HttpService(endpoint, client, false);
    }

    @Bean
    @Qualifier("local")
    public Web3jService provideInfuraEndpoint(@Value("${cloud.cinder.ethereum.endpoint.local-url}") final String endpoint) {
        final OkHttpClient client = new OkHttpClient.Builder()
                .retryOnConnectionFailure(true)
                .writeTimeout(1, TimeUnit.MINUTES)
                .readTimeout(3, TimeUnit.MINUTES)
                .build();
        return new HttpService(endpoint, client, false);
    }

    @Bean
    @Qualifier("websocket")
    public Web3jService provideWebsocketEndpoint(@Value("${cloud.cinder.ethereum.endpoint.websocket-url}") final String endpoint) throws ConnectException {
        WebSocketClient webSocketClient = new WebSocketClient(URI.create(endpoint));
        WebSocketService webSocketService = new WebSocketService(webSocketClient, false);
        webSocketService.connect();
        return new WebSocketService(webSocketClient, false);
    }
}