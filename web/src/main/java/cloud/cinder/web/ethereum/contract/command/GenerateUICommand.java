package cloud.cinder.web.ethereum.contract.command;

import cloud.cinder.ethereum.abi.domain.AbiContractElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenerateUICommand {

    private String elementName;
    private List<AbiContractElement> elements;
}
