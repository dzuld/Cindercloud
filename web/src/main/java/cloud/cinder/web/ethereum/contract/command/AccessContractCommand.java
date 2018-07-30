package cloud.cinder.web.ethereum.contract.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessContractCommand {

    private String address;
    private String abi;

    public String getAbi() {
        return abi == null ? null : abi.trim();
    }
}
