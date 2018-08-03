package cloud.cinder.vechain.thorifyj.transaction.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ThorifyTransactionReceiptOutput {

    /**
     * deployed contract address, if the corresponding clause is a contract deployment clause
     */
    private String contractAddress;

    private List<Event> events;
    private List<Transfer> transfers;

    @Data
    @NoArgsConstructor
    public static class Event {
        private String address;
        private List<String> topics;
        private String data;
    }

    @Data
    @NoArgsConstructor
    public static class Transfer {
        private String sender;
        private String recipient;
        private String amount;
    }
}
