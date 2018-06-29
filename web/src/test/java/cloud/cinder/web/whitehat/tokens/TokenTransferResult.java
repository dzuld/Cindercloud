package cloud.cinder.web.whitehat.tokens;

import java.util.List;

public class TokenTransferResult {
    private String status;
    private String message;
    private List<TokenTransfer> result;

    public TokenTransferResult() {
    }

    public String getStatus() {
        return status;
    }

    public TokenTransferResult setStatus(final String status) {
        this.status = status;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public TokenTransferResult setMessage(final String message) {
        this.message = message;
        return this;
    }

    public List<TokenTransfer> getResult() {
        return result;
    }

    public TokenTransferResult setResult(final List<TokenTransfer> result) {
        this.result = result;
        return this;
    }
}
