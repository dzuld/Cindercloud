package cloud.cinder.ethereum.token.domain;

import java.math.BigInteger;

public class ApprovalEventResponse {

    private String owner;
    private String spender;
    private BigInteger value;

    public ApprovalEventResponse(final String owner, final String spender, final BigInteger value) {
        this.owner = owner;
        this.spender = spender;
        this.value = value;
    }

    public String getOwner() {
        return owner;
    }

    public String getSpender() {
        return spender;
    }

    public BigInteger getValue() {
        return value;
    }
}