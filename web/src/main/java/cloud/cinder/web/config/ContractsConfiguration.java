package cloud.cinder.web.config;

import cloud.cinder.ethereum.parity.domain.ParitySignatureRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.protocol.Web3j;

@Configuration
public class ContractsConfiguration {

    @Bean
    public ParitySignatureRegistry paritySignatureRegistry(
            final @Value("${contracts.parity.signature-registry.address}") String address,
            final Web3j web3j) {
        return new ParitySignatureRegistry(address, web3j);
    }

}
