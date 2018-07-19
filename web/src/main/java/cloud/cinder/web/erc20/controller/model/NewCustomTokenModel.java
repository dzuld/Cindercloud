package cloud.cinder.web.erc20.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class NewCustomTokenModel {
    @NotEmpty
    private String address;
}
