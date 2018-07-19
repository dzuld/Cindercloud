package cloud.cinder.ethereum.abi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AbiContractFunction extends AbiContractElement {

    private String name;
    private boolean payable;
    private String stateMutability;
    private boolean constant;
    private List<AbiElementInput> inputs;
    private List<AbiElementOutput> outputs;

}
