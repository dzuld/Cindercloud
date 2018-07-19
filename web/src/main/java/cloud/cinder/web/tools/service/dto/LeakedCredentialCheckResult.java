package cloud.cinder.web.tools.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeakedCredentialCheckResult {

    private String subject;
    private boolean found;

}
