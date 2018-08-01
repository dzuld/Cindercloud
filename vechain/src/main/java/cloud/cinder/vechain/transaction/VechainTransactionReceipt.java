package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceipt;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@Table(name = "vechain_transaction_receipts")
@Entity
@NoArgsConstructor
public class VechainTransactionReceipt {

    @Id
    private String id;
    @Column(name = "gas_used")
    private BigInteger gasUsed;
    @Column(name = "gas_payer")
    private String gasPayer;
    private BigInteger paid;
    private BigInteger reward;
    private boolean reverted;
    private String blockId;
    private BigInteger blockNumber;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    @Builder
    public VechainTransactionReceipt(final String id, final BigInteger gasUsed, final String gasPayer, final BigInteger paid, final BigInteger reward, final boolean reverted, final String blockId, final BigInteger blockNumber, final Date timestamp) {
        this.id = id;
        this.gasUsed = gasUsed;
        this.gasPayer = gasPayer;
        this.paid = paid;
        this.reward = reward;
        this.reverted = reverted;
        this.blockId = blockId;
        this.blockNumber = blockNumber;
        this.timestamp = timestamp;
    }

    public static VechainTransactionReceipt of(final String transactionId, final ThorifyTransactionReceipt receipt) {
        final VechainTransactionReceipt retVal = VechainTransactionReceipt
                .builder()
                .blockId(receipt.getMeta().getBlockId())
                .blockNumber(receipt.getMeta().getBlockNumber())
                .timestamp(Date.from(LocalDateTime.ofEpochSecond(receipt.getMeta().getBlockTimestamp(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                .reverted(receipt.isReverted())
                .reward(toBigInt(receipt.getReward()))
                .paid(toBigInt(receipt.getPaid()))
                .gasPayer(receipt.getGasPayer())
                .gasUsed(receipt.getGasUsed())
                .id(transactionId)
                .build();
        return retVal;
    }

    private static BigInteger toBigInt(final String reward) {
        return new BigInteger(reward != null ? reward.startsWith("0x") ? reward.substring(2) : reward : "0", 16);
    }
}
