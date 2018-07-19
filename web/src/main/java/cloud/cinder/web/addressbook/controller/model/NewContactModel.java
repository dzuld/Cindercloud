package cloud.cinder.web.addressbook.controller.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.NotEmpty;

@Data
@NoArgsConstructor
public class NewContactModel {

    @NotEmpty
    private String address;
    @NotEmpty
    private String nickname;
}
