package cloud.cinder.ethereum.parity;

import cloud.cinder.ethereum.parity.domain.ParitySignatureRegistry;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class ParitySignatureRegistryService {

    private final ParitySignatureRegistry paritySignatureRegistry;

    public ParitySignatureRegistryService(final ParitySignatureRegistry paritySignatureRegistry) {
        this.paritySignatureRegistry = paritySignatureRegistry;
    }

    @Cacheable(cacheNames = "contracts.parity.method-signatures", key = "T(org.bouncycastle.util.encoders.Hex).toHexString(#bytes)")
    public String entries(final byte[] bytes) {
        return paritySignatureRegistry.entries(bytes);
    }
}
