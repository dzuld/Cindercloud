package cloud.cinder.web.whitehat.tokens;

public class Token {

    private String address;
    private String name;
    private String symbol;

    public Token() {
    }

    public String getAddress() {
        return address;
    }

    public Token setAddress(final String address) {
        this.address = address;
        return this;
    }

    public String getName() {
        return name;
    }

    public Token setName(final String name) {
        this.name = name;
        return this;
    }

    public String getSymbol() {
        return symbol;
    }

    public Token setSymbol(final String symbol) {
        this.symbol = symbol;
        return this;
    }
}
