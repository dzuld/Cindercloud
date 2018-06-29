package cloud.cinder.web.whitehat;

import java.util.List;

public class CrowdsaleResult {

    private List<Transaction> result;

    public CrowdsaleResult() {
    }

    public List<Transaction> getResult() {
        return result;
    }

    public CrowdsaleResult setResult(final List<Transaction> result) {
        this.result = result;
        return this;
    }

    public static class Transaction {
        private String input;
        private String from;
        private String to;

        public Transaction() {
        }

        public String getFrom() {
            return from;
        }

        public Transaction setFrom(final String from) {
            this.from = from;
            return this;
        }

        public String getTo() {
            return to;
        }

        public Transaction setTo(final String to) {
            this.to = to;
            return this;
        }

        public String getInput() {
            return input;
        }

        public Transaction setInput(final String input) {
            this.input = input;
            return this;
        }
    }

}
