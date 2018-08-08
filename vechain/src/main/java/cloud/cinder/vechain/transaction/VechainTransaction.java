package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransaction;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.ocpsoft.prettytime.PrettyTime;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import static org.apache.commons.lang.StringUtils.isEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vechain_transactions")
@Entity
public class VechainTransaction {

    @Id
    private String id;
    @Column(name = "chain_tag")
    private Long chainTag;
    @Column(name = "block_ref")
    private String blockRef;
    private BigInteger expiration;
    @Column(name = "gas_price_coef")
    private BigInteger gasPriceCoef;
    private BigInteger gas;
    private String origin;
    private String nonce;
    @Column(name = "depends_on")
    private String dependsOn;
    private BigInteger size;
    @Column(name = "block_id")
    private String blockId;
    @Column(name = "block_number")
    private BigInteger blockNumber;
    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public String prettyTimestamp() {
        final LocalDateTime localDateTime = timestamp.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        final PrettyTime prettyTime = new PrettyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(Date.from(localDateTime.atOffset(ZoneOffset.UTC).toInstant()));
    }

    public String prettyHash() {
        return id.substring(0, 66) + "";
    }

    public String prettyFromAddress() {
        return origin.substring(0, 18) + "...";
    }

    public BigInteger nonceAsInt() {
        if (!isEmpty(nonce)) {
            return new BigInteger(Hex.decode(nonce.substring(2)));
        } else {
            return BigInteger.ZERO;
        }
    }

    public static VechainTransaction of(final ThorifyTransaction thorifyTransaction) {
        return VechainTransaction
                .builder()
                .blockId(thorifyTransaction.getMeta().getBlockId())
                .blockNumber(thorifyTransaction.getMeta().getBlockNumber())
                .timestamp(Date.from(LocalDateTime.ofEpochSecond(thorifyTransaction.getMeta().getBlockTimestamp(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                .id(thorifyTransaction.getId())
                .chainTag(thorifyTransaction.getChainTag())
                .blockRef(thorifyTransaction.getBlockRef())
                .expiration(thorifyTransaction.getExpiration())
                .gasPriceCoef(thorifyTransaction.getGasPriceCoef())
                .gas(thorifyTransaction.getGas())
                .origin(thorifyTransaction.getOrigin())
                .nonce(thorifyTransaction.getNonce())
                .dependsOn(thorifyTransaction.getDependsOn())
                .size(thorifyTransaction.getSize())
                .build();
    }

    public Direction direction(final String address) {
        if (origin.equals(address)) {
            return Direction.OUT;
        } else {
            return Direction.IN;
        }
    }

    public enum Direction {
        IN, OUT
    }
}
