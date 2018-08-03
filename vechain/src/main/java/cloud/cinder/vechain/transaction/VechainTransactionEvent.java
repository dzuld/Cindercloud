package cloud.cinder.vechain.transaction;

import cloud.cinder.vechain.thorifyj.transaction.domain.ThorifyTransactionReceiptOutput;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Table(name = "vechain_transaction_events")
@Entity
@NoArgsConstructor
public class VechainTransactionEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;
    @ElementCollection
    @CollectionTable(name = "vechain_transaction_event_topics",
            joinColumns = {
                    @JoinColumn(name = "event_id", nullable = false, referencedColumnName = "id")
            })
    @Column(name = "topic")
    private List<String> topics;
    private String data;

    @ManyToOne
    @JoinColumn(name = "output_id", referencedColumnName = "id", nullable = false)
    private VechainTransactionReceiptOutput output;

    @Builder
    public VechainTransactionEvent(final String address,
                                   final List<String> topics,
                                   final String data,
                                   final VechainTransactionReceiptOutput vechainTransactionReceiptOutput) {
        this.address = address;
        this.topics = topics;
        this.data = data;
        this.output = vechainTransactionReceiptOutput;
    }

    public static VechainTransactionEvent of(ThorifyTransactionReceiptOutput.Event event, final VechainTransactionReceiptOutput savedOutput) {
        return VechainTransactionEvent
                .builder()
                .address(event.getAddress())
                .data(event.getData())
                .topics(event.getTopics())
                .vechainTransactionReceiptOutput(savedOutput)
                .build();
    }
}
