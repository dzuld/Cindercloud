package cloud.cinder.vechain.block.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vechain_blocks")
@Entity
public class VechainBlock {

    private String id;
    private BigInteger number;
    private String parentId;
    private BigInteger size;
    private String stateroot;
    private BigInteger gasLimit;
    private String beneficiary;
    private BigInteger gasUsed;
    private BigInteger totalScore;
    private String txsRoot;
    private String receiptsRoot;
    private String signer;
    private boolean trunk;

    @Column(name = "block_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestampDateTime;
}
