package cloud.cinder.ethereum.token.domain;

import java.math.BigInteger;

public class TransferEventResponse {

    private String from;
    private String to;
    private BigInteger value;

    public TransferEventResponse(final String from, final String to, final BigInteger value) {
        this.from = from;
        this.to = to;
        this.value = value;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public BigInteger getValue() {
        return value;
    }
}