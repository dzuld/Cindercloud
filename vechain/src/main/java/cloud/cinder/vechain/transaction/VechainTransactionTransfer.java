package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceiptOutput;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Table(name = "vechain_transaction_transfers")
@NoArgsConstructor
@Entity
public class VechainTransactionTransfer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String sender;
    private String recipient;
    private BigInteger amount;

    @ManyToOne
    @JoinColumn(name = "output_id", referencedColumnName = "id", nullable = false)
    private VechainTransactionReceiptOutput output;

    @Builder
    public VechainTransactionTransfer(final String sender,
                                      final String recipient,
                                      final BigInteger amount,
                                      final VechainTransactionReceiptOutput output) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.output = output;
    }

    public static VechainTransactionTransfer of(final ThorifyTransactionReceiptOutput.Transfer transfer,
                                                final VechainTransactionReceiptOutput output) {
        return VechainTransactionTransfer
                .builder()
                .amount(toBigInt(transfer.getAmount()))
                .recipient(transfer.getRecipient())
                .sender(transfer.getSender())
                .output(output)
                .build();
    }

    private static BigInteger toBigInt(final String reward) {
        return new BigInteger(reward != null ? reward.startsWith("0x") ? reward.substring(2) : reward : "0", 16);
    }
}
