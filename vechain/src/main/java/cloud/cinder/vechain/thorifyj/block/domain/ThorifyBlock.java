package cloud.cinder.vechain.thorifyj.block.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ThorifyBlock {

    /**
     * number(height) of the block
     */
    private BigInteger number;

    /**
     * identifier of the block (bytes32)
     */
    private String id;

    /**
     * ID of parent block (bytes32)
     */
    private String parentID;
    private Long timestamp;

    /**
     * byte size of the block that is RLP encoded
     */
    private BigInteger size;
    private BigInteger gasLimit;

    /**
     * address of account to receive block reward
     */
    private String beneficiary;
    private BigInteger gasUsed;
    private BigInteger totalScore;

    /**
     * root hash of transactions in the block (bytes32)
     */
    private String txsRoot;

    /**
     * root hash of accounts state (bytes32)
     */
    private String stateRoot;

    /**
     * root hash of transaction receipts (bytes32)
     */
    private String receiptsRoot;

    /**
     * the one who signed this block (bytes20)
     */
    private String signer;

    /**
     * whether block is trunk
     */
    @JsonProperty("isTrunk")
    private boolean trunk;
    private List<String> transactions = new ArrayList<>();
}
