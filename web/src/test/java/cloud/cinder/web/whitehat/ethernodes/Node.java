package cloud.cinder.web.whitehat.ethernodes;

import java.util.Date;

public class Node {

    public Node() {
    }

    private String id;
    private String host;
    private String port;
    private String clientId;
    private String client;
    private Date lastUpdate;
    private String country;

    public String getId() {
        return id;
    }

    public Node setId(final String id) {
        this.id = id;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Node setHost(final String host) {
        this.host = host;
        return this;
    }

    public String getPort() {
        return port;
    }

    public Node setPort(final String port) {
        this.port = port;
        return this;
    }

    public String getClientId() {
        return clientId;
    }

    public Node setClientId(final String clientId) {
        this.clientId = clientId;
        return this;
    }

    public String getClient() {
        return client;
    }

    public Node setClient(final String client) {
        this.client = client;
        return this;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public Node setLastUpdate(final Date lastUpdate) {
        this.lastUpdate = lastUpdate;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public Node setCountry(final String country) {
        this.country = country;
        return this;
    }
}
