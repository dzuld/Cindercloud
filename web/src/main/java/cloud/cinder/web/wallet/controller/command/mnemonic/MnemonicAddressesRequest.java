package cloud.cinder.web.wallet.controller.command.mnemonic;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MnemonicAddressesRequest {
    private String mnemonic;
    private int page = 0;
}
