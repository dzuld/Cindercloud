package cloud.cinder.common.login.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Table(name = "login_events")
@Entity
@Data
@NoArgsConstructor
public class LoginEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "wallet")
    private String wallet;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_time")
    private Date eventTime;

    @Column(name = "wallet_type")
    private String walletType;

    @Builder
    public LoginEvent(final String wallet, final Date eventTime, final String walletType) {
        this.wallet = wallet;
        this.eventTime = eventTime;
        this.walletType = walletType;
    }
}
