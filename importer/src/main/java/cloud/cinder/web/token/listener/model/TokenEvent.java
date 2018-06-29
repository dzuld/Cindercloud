package cloud.cinder.web.token.listener.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.web3j.abi.EventValues;

@AllArgsConstructor
@Data
public class TokenEvent {
    private TokenEventType eventType;
    private EventValues eventValues;
}