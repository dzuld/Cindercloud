package cloud.cinder.ethereum.erc20.dto;

import cloud.cinder.ethereum.token.domain.ERC20;
import cloud.cinder.ethereum.token.domain.Token;
import lombok.Builder;
import lombok.Getter;

@Getter
public class AddressAmountDto {
    private ERC20 token;
    private String balance;
    private double rawBalance;

    @Builder
    public AddressAmountDto(final ERC20 token, final String balance, final double rawBalance) {
        this.token = token;
        this.balance = balance;
        this.rawBalance = rawBalance;
    }
}
