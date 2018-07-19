package cloud.cinder.web.etherscan.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EtherscanResponse {

    private int status;
    private String message;
    private List<EtherscanTransaction> result;


    public boolean isSuccess() {
        return status == 1;
    }
}
