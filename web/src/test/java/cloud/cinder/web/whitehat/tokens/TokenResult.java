package cloud.cinder.web.whitehat.tokens;

import java.util.List;

public class TokenResult {
    private List<Token> tokens;

    public TokenResult() {
    }

    public List<Token> getTokens() {
        return tokens;
    }

    public TokenResult setTokens(final List<Token> tokens) {
        this.tokens = tokens;
        return this;
    }
}
