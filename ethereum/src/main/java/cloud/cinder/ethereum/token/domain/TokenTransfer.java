package cloud.cinder.ethereum.token.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Entity
@Table(name = "token_transfers")
@Data
@NoArgsConstructor
public class TokenTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_address")
    private String fromAddress;

    @Column(name = "to_address")
    private String toAddress;

    @Column(name = "amount")
    private BigInteger amount;

    @Column(name = "transaction_hash")
    private String transactionHash;

    @Column(name = "block_height")
    private BigInteger blockHeight;

    @Column(name = "log_index")
    private String logIndex;

    @Column(name = "origin_address")
    private String originAddress;

    @Column(name = "removed")
    private boolean removed;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date blockTimestamp;
    @Column(name = "token_address")
    private String tokenAddress;

    @Builder
    public TokenTransfer(final String fromAddress, final String toAddress, final BigInteger amount, final String transactionHash, final BigInteger blockHeight, final String logIndex, final String originAddress, final boolean removed, final Date blockTimestamp, final String tokenAddress) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
        this.transactionHash = transactionHash;
        this.blockHeight = blockHeight;
        this.logIndex = logIndex;
        this.originAddress = originAddress;
        this.removed = removed;
        this.blockTimestamp = blockTimestamp;
        this.tokenAddress = tokenAddress;
    }
}
