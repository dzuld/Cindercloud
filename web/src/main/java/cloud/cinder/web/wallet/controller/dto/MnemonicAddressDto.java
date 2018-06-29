package cloud.cinder.web.wallet.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MnemonicAddressDto {
    private String address;
    private int index;
}
