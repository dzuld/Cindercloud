package cloud.cinder.vechain.block.domain;

import cloud.cinder.vechain.thorifyj.domain.ThorifyBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ocpsoft.prettytime.PrettyTime;

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
@Table(name = "vechain_blocks")
@Entity
public class VechainBlock {

    private static final DecimalFormat percentage = new DecimalFormat("##.##%");

    @Id
    private String id;
    private BigInteger number;
    @Column(name = "parent_id")
    private String parentId;
    private BigInteger size;
    @Column(name = "state_root")
    private String stateroot;
    private BigInteger gasLimit;
    private String beneficiary;
    private BigInteger gasUsed;
    private BigInteger totalScore;
    @Column(name = "txs_root")
    private String txsRoot;
    @Column(name = "receipts_root")
    private String receiptsRoot;
    private String signer;
    private boolean trunk;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;

    public String gasUsedPercentage() {
        try {
            return percentage.format(gasUsed.doubleValue() / gasLimit.doubleValue());
        } catch (final Exception ex) {
            return "0%";
        }
    }

    public String getPrettyScore() {
        return totalScore == null ? "" : totalScore.divide(BigInteger.valueOf(1000000000).multiply(BigInteger.valueOf(1000))).toString();
    }

    public String prettyHash() {
        return id.substring(0, 25) + "...";
    }

    public String prettySigner() {
        return signer.substring(0, 15) + "...";
    }

    public String prettyTimestamp() {
        final LocalDateTime localDateTime = timestamp.toInstant().atOffset(ZoneOffset.UTC).toLocalDateTime();
        final PrettyTime prettyTime = new PrettyTime(Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()));
        return prettyTime.format(Date.from(localDateTime.atOffset(ZoneOffset.UTC).toInstant()));
    }


    public static VechainBlock asVechainBlock(ThorifyBlock thorifyBlock) {
        return VechainBlock.builder()
                .beneficiary(thorifyBlock.getBeneficiary())
                .gasLimit(thorifyBlock.getGasLimit())
                .gasUsed(thorifyBlock.getGasUsed())
                .size(thorifyBlock.getSize())
                .id(thorifyBlock.getId())
                .stateroot(thorifyBlock.getStateRoot())
                .signer(thorifyBlock.getSigner())
                .number((thorifyBlock.getNumber()))
                .receiptsRoot(thorifyBlock.getReceiptsRoot())
                .txsRoot(thorifyBlock.getTxsRoot())
                .totalScore(thorifyBlock.getTotalScore())
                .parentId(thorifyBlock.getParentID())
                .trunk(thorifyBlock.isTrunk())
                .timestamp(Date.from(LocalDateTime.ofEpochSecond(thorifyBlock.getTimestamp(), 0, ZoneOffset.UTC).atOffset(ZoneOffset.UTC).toInstant()))
                .build();
    }
}
