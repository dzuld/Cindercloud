package cloud.cinder.web.erc20.controller.dto;

import cloud.cinder.ethereum.token.domain.Token;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressTokenDto {
    private Token token;
    private String balance;
    private double rawBalance;
    private String eurPrice;
    private String dollarPrice;
}
