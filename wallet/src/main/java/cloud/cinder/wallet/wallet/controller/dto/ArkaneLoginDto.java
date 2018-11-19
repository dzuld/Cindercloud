package cloud.cinder.wallet.wallet.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArkaneLoginDto {
    private String address;
    private String bearerToken;
    private String walletId;
}
