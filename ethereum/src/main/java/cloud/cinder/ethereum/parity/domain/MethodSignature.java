package cloud.cinder.ethereum.parity.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class MethodSignature {

    private String hash;
    private String signature;

    public MethodSignature(final String hash, final String signature) {
        this.hash = hash;
        this.signature = signature;
    }

    private List<String> arguments = new ArrayList<>();
}
