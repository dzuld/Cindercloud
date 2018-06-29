package cloud.cinder.ethereum.block.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bouncycastle.util.encoders.Hex;
import org.ocpsoft.prettytime.PrettyTime;
import org.web3j.protocol.core.methods.response.EthBlock;

import javax.persistence.*;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "blocks")
@Entity
public class Block {

    private static final DecimalFormat format = new DecimalFormat("##.##%");

    @Id
    private String hash;
    private String mixHash;
    private BigInteger height;
    private String parentHash;
    @Column(name = "sha3_uncles")
    private String sha3Uncles;
    private String minedBy;
    private BigInteger difficulty;
    private BigInteger difficultyTotal;
    private BigInteger gasLimit;
    private BigInteger gasUsed;

    @Column(name = "tx_count")
    private Long txCount;
    private String receiptsRoot;
    private BigInteger size;
    private BigInteger timestamp;
    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampDateTime;
    private BigInteger nonce;
    private String extraData;

    @Builder.Default
    private boolean uncle = false;

    public String gasUsedPercentage() {
        try {
            return format.format(gasUsed.doubleValue() / gasLimit.doubleValue());
        } catch (final Exception ex) {
            return "0%";
        }
    }

    public String getExtraDataString() {
        try {
            return new String(Hex.decode(extraData.substring(2)));
        } catch (Exception extraDataString) {
            return "";
        }
    }

    public String getPrettyDifficulty() {
        return difficulty == null ? "" : difficulty.divide(BigInteger.valueOf(1000000000).multiply(BigInteger.valueOf(1000))).toString();
    }

    public String getPrettyTotalDifficulty() {
        return difficultyTotal == null ? "" : difficultyTotal.divide(BigInteger.valueOf(1000000000).multiply(BigInteger.valueOf(1000))).toString();
    }

    public String prettyHash() {
        return hash.substring(0, 25) + "...";
    }

    public String prettyMinedBy() {
        return minedBy.substring(0, 15) + "...";
    }

    public String prettyTimestamp() {
        final LocalDateTime localDateTime = LocalDateTime.ofEpochSecond(timestamp.longValue(), 0, ZoneOffset.UTC);
        final PrettyTime prettyTime = new PrettyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(Date.from(localDateTime.atOffset(ZoneOffset.UTC).toInstant()));
    }

    public static Block asBlock(final EthBlock.Block block) {
        return Block.builder()
                .difficulty(block.getDifficulty())
                .difficultyTotal(block.getTotalDifficulty())
                .extraData(block.getExtraData())
                .hash(block.getHash())
                .mixHash(block.getMixHash())
                .gasLimit(block.getGasLimit())
                .gasUsed(block.getGasUsed())
                .minedBy(block.getMiner())
                .timestampDateTime(Date.from(LocalDateTime.ofEpochSecond(block.getTimestamp().longValue(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                .sha3Uncles(block.getSha3Uncles())
                .nonce(block.getNonceRaw() != null ? block.getNonce() : BigInteger.ZERO)
                .size(block.getSize())
                .timestamp(block.getTimestamp())
                .txCount((long) block.getTransactions().size())
                .parentHash(block.getParentHash())
                .receiptsRoot(block.getReceiptsRoot())
                .height(block.getNumber())
                .build();
    }

    public static Block asUncle(final EthBlock.Block block) {
        final Block retVal = asBlock(block);
        retVal.setUncle(true);
        return retVal;
    }

}
