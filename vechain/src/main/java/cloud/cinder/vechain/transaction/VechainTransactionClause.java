package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.domain.ThorifyTransactionClause;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "vechain_transaction_clauses")
@Entity
public class VechainTransactionClause {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String transactionId;
    @Column(name = "to_address")
    private String to;
    private String data;
    private String value;

    public static VechainTransactionClause of(final ThorifyTransactionClause clause, final String transactionId) {
        return VechainTransactionClause
                .builder()
                .data(clause.getData())
                .value(clause.getValue())
                .to(clause.getTo())
                .transactionId(transactionId)
                .build();
    }
}
