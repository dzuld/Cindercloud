package cloud.cinder.web.whitehat.tokens;

public class TokenTransfer {

    public TokenTransfer() {
    }

    private String blockNumber;

    public String getBlockNumber() {
        return blockNumber;
    }

    public TokenTransfer setBlockNumber(final String blockNumber) {
        this.blockNumber = blockNumber;
        return this;
    }
}
