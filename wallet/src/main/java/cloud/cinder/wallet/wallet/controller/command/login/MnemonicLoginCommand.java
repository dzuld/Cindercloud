package cloud.cinder.wallet.wallet.controller.command.login;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MnemonicLoginCommand {
    private String mnemonic;
    private int index;
}
