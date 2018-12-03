package cloud.cinder.wallet.erc20.controller.dto;

import cloud.cinder.ethereum.token.domain.ERC20;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTokenDto {
    private ERC20 token;
    private String balance;
    private double rawBalance;
    private String eurPrice;
    private String dollarPrice;
}
