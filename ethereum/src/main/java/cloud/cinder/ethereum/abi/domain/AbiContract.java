package cloud.cinder.ethereum.abi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AbiContract {

    private List<AbiContractElement> elements;

}
