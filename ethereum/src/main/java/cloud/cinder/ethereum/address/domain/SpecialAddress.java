package cloud.cinder.ethereum.address.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Table(name = "special_addresses")
@DiscriminatorColumn(name = "type")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SpecialAddress {

    @Id
    @GeneratedValue
    private Long id;
    private String address;
    private String name;
    private String slug;
    private String chain;
    private String url;
}
