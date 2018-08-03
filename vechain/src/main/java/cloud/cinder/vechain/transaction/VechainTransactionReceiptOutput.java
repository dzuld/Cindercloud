package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceiptOutput;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "vechain_transaction_receipt_outputs")
@Entity
@Data
@NoArgsConstructor
public class VechainTransactionReceiptOutput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "contract_address")
    private String contractAddress;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private VechainTransactionReceipt vechainTransactionReceipt;

    @Builder
    public VechainTransactionReceiptOutput(final String contractAddress,
                                           final VechainTransactionReceipt vechainTransactionReceipt) {
        this.contractAddress = contractAddress;
        this.vechainTransactionReceipt = vechainTransactionReceipt;
    }

    public static VechainTransactionReceiptOutput of(final ThorifyTransactionReceiptOutput output,
                                                     final VechainTransactionReceipt receipt) {
        return VechainTransactionReceiptOutput
                .builder()
                .contractAddress(output.getContractAddress())
                .vechainTransactionReceipt(receipt)
                .build();
    }
}
