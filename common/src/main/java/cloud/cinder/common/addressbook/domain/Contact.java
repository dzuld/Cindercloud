package cloud.cinder.common.addressbook.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "addressbook_contacts")
@Entity
@Data
@NoArgsConstructor
public class Contact {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "owner_address")
    private String owner;
    @Column(name = "address")
    private String address;
    @Column(name = "last_modified")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModified;
    @Column(name = "nickname")
    private String nickname;

    @Builder
    public Contact(final String owner, final String address, final String nickname) {
        this.owner = owner;
        this.address = address;
        this.nickname = nickname;
        this.lastModified = new Date();
    }
}
