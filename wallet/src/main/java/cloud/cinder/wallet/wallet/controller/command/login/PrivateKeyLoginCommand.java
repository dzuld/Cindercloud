package cloud.cinder.wallet.wallet.controller.command.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PrivateKeyLoginCommand {

    private String privateKey;
}