package cloud.cinder.vechain.thorifyj.transaction.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@Data
@NoArgsConstructor
public class ThorifyTransactionReceipt {

    private BigInteger gasUsed;

    /**
     * address of account who paid used gas
     */
    private String gasPayer;

    /**
     * hex form of amount of paid energy
     */
    private String paid;

    /**
     * hex form of amount of reward
     */
    private String reward;

    /**
     * true means the transaction was reverted
     */
    private boolean reverted;

    private List<ThorifyTransactionReceiptOutput> outputs;
    private ThorifyTransactionMeta meta;
}
