package cloud.cinder.ethereum.erc20.service;

import cloud.cinder.ethereum.erc20.dto.AddressAmountDto;
import cloud.cinder.ethereum.token.domain.DeltaBalances;
import cloud.cinder.ethereum.token.domain.ERC20;
import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ERC20Service {

    private final DecimalFormat formatter = new DecimalFormat("0.######");

    private Web3jGateway web3j;
    private DeltaBalances deltaBalances;

    public ERC20Service(final Web3jGateway web3j, final DeltaBalances deltaBalances) {
        this.web3j = web3j;
        this.deltaBalances = deltaBalances;
    }

    public BigInteger rawBalanceOf(final String address, final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            return erc20.balanceOf(address).send();
        } catch (final Exception e) {
            return BigInteger.ZERO;
        }
    }

    public List<AddressAmountDto> balanceOf(final String address, final List<? extends ERC20> tokens) {
        List<BigInteger> bigIntegers = deltaBalances.tokenBalances(address, tokens.stream().map(ERC20::getAddress).collect(Collectors.toList()));
        final List<AddressAmountDto> addressTokenDtos = new ArrayList<>();
        for (int i = 0; i < bigIntegers.size(); i++) {
            final int decimals = decimals(tokens.get(i).getAddress());
            final BigDecimal rawBalance = new BigDecimal(bigIntegers.get(i));
            final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals);
            addressTokenDtos.add(AddressAmountDto.builder()
                    .token(tokens.get(i))
                    .balance(
                            formatter.format(rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN)))
                    .rawBalance(new BigDecimal(bigIntegers.get(i)).doubleValue())
                    .build(
                    ));
        }
        return addressTokenDtos;
    }

    @Cacheable(cacheNames = "wallets.tokens.amount", key = "#address+'-'+#token")
    public BigDecimal balanceOf(final String address, final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            final BigInteger decimals = erc20.decimals().send();
            final BigDecimal rawBalance = new BigDecimal(erc20.balanceOf(address).send());
            final BigDecimal divider = BigDecimal.valueOf(10).pow(decimals.intValue());
            return rawBalance.divide(divider, 6, RoundingMode.HALF_DOWN);
        } catch (final Exception e) {
            return BigDecimal.ZERO;
        }
    }

    @Cacheable(cacheNames = "erc20.tokens.decimals", key = "#token")
    public int decimals(final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            return erc20.decimals().send().intValue();
        } catch (final Exception e) {
            return 18;
        }
    }

    @CacheEvict(cacheNames = "wallets.tokens.amount", key = "#address+'-'+#token")
    public void evictBalanceOf(final String address, final String token) {
    }

    private HumanStandardToken getERC20(final String token) {
        return HumanStandardToken.load(token, web3j.web3j());
    }
}
