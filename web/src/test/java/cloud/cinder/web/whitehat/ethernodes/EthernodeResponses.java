package cloud.cinder.web.whitehat.ethernodes;

import java.util.List;

public class EthernodeResponses {


    public EthernodeResponses() {
    }

    private List<Node> data;


    public List<Node> getData() {
        return data;
    }

    public EthernodeResponses setData(final List<Node> data) {
        this.data = data;
        return this;
    }
}
