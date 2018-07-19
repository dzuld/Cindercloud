package cloud.cinder.ethereum.abi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class AbiContractConstructor extends AbiContractElement {
    private boolean payable;
    private String stateMutability;
    private List<AbiElementInput> inputs;
}
