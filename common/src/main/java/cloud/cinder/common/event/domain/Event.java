package cloud.cinder.common.event.domain;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "events")
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    @Column(name = "event_type")
    @Enumerated(EnumType.STRING)
    private EventType type;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "event_time")
    private Date eventTime;

    @Builder
    public Event(final String message, final EventType type) {
        this.message = message;
        this.type = type;
        this.eventTime = new Date();
    }
}
