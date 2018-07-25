package cloud.cinder.vechain.thorifyj.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ThorifyBlock {
    private BigInteger number;
    private String id;
    private String parentID;
    private Long timestamp;
    private BigInteger size;
    private BigInteger gasLimit;
    private String beneficiary;
    private BigInteger gasUsed;
    private BigInteger totalScore;
    private String txsRoot;
    private String stateRoot;
    private String receiptsRoot;
    private String signer;
    @JsonProperty("isTrunk")
    private boolean trunk;
    private List<String> transactions = new ArrayList<>();
}
