package cloud.cinder.common.credential.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "leaked_credentials")
@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LeakedCredential {

    @Column(name = "address")
    @Id
    private String address;

    @Column(name = "private_key")
    private String privateKey;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date_added")
    private Date dateAdded;
}
