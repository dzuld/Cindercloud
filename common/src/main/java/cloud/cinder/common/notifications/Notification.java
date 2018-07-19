package cloud.cinder.common.notifications;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Notification {

    private String type;
    private String text;

    @Builder
    public Notification(final String type, final String text) {
        this.type = type;
        this.text = text;
    }
}
