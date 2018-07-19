package cloud.cinder.ethereum.token.domain;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tokens")
public class Token {

    @Id
    @GeneratedValue
    private long id;
    private String name;

    @Column(name = "coinmarketcap_name")
    private String coinmarketcapName;
    private String slug;
    private String address;
    private String image;
    private String website;
    private String social;
    private String symbol;
    private int decimals;

}
