package cloud.cinder.ethereum.erc20.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "custom_erc20")
@Entity
@Data
@NoArgsConstructor
public class CustomERC20 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "address")
    private String address;

    @Column(name = "name")
    private String name;

    @Column(name = "decimals")
    private int decimals;

    @Column(name = "symbol")
    private String symbol;

    @Column(name = "added_by")
    private String addedBy;

    @Builder
    public CustomERC20(final String address, final String name, final int decimals, final String symbol, final String addedBy) {
        this.address = address;
        this.name = name;
        this.decimals = decimals;
        this.symbol = symbol;
        this.addedBy = addedBy;
    }
}
