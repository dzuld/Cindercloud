package cloud.cinder.vechain.block.domain;

import cloud.cinder.vechain.thorifyj.domain.ThorifyBlock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vechain_blocks")
@Entity
public class VechainBlock {

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
