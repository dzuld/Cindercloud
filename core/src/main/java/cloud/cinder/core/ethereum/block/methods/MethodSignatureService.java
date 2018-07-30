package cloud.cinder.core.ethereum.block.methods;

import cloud.cinder.ethereum.parity.domain.MethodSignature;
import cloud.cinder.ethereum.parity.domain.ParitySignatureRegistry;
import org.bouncycastle.util.encoders.Hex;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.web3j.rlp.RlpString;

import java.util.Arrays;
import java.util.Optional;

@Component
@ConditionalOnBean(ParitySignatureRegistry.class)
public class MethodSignatureService {

    private final ParitySignatureRegistryService paritySignatureRegistryService;

    public MethodSignatureService(final ParitySignatureRegistryService paritySignatureRegistryService) {
        this.paritySignatureRegistryService = paritySignatureRegistryService;
    }

    public Optional<MethodSignature> findSignature(final String input) {
        try {
            final String sanitizedInput = sanitize(input);
            final byte[] bytes = RlpString.create(Arrays.copyOfRange(Hex.decode(sanitizedInput), 0, 4)).getBytes();
            final String signature = paritySignatureRegistryService.entries(bytes);
            if (!StringUtils.isEmpty(signature)) {
                return Optional.of(new MethodSignature(input, signature));
            } else {
                return Optional.empty();
            }
        } catch (final Exception ex) {
            return Optional.empty();
        }
    }

    private String sanitize(final String input) {
        if ((input != null) && input.toLowerCase().startsWith("0x")) {
            return input.substring(2);
        } else {
            return input;
        }
    }
}
