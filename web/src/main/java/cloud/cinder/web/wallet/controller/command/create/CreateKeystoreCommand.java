package cloud.cinder.web.wallet.controller.command.create;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateKeystoreCommand {

    private String password;
    private boolean secure = true;
}
