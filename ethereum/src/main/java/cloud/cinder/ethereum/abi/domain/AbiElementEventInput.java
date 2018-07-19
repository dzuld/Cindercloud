package cloud.cinder.ethereum.abi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AbiElementEventInput {
    private String name;
    private String type;
    private boolean indexed;
}
