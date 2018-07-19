package cloud.cinder.ethereum.block.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "block_import_jobs")
@Data
@NoArgsConstructor
public class BlockImportJob {

    @Id
    @GeneratedValue
    private Long id;

    private boolean active;
    @Column(name = "from_block")
    private Long fromBlock;
    @Column(name = "to_block")
    private Long toBlock;

    @Column(name = "start_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date startTime;

    @Column(name = "end_time")
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date endTime;
}
