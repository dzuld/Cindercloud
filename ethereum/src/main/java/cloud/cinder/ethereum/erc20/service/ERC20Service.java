package cloud.cinder.ethereum.erc20.service;

import cloud.cinder.ethereum.token.domain.HumanStandardToken;
import cloud.cinder.ethereum.web3j.Web3jGateway;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

@Component
public class ERC20Service {

    private Web3jGateway web3j;

    public ERC20Service(final Web3jGateway web3j) {
        this.web3j = web3j;
    }

    public BigInteger rawBalanceOf(final String address, final String token) {
        final HumanStandardToken erc20 = getERC20(token);
        try {
            return erc20.balanceOf(address).send();
        } catch (final Exception e) {
            return BigInteger.ZERO;
        }
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
