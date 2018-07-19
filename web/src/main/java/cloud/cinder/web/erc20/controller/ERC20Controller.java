package cloud.cinder.web.erc20.controller;

import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.cryptocompare.service.TokenPriceService;
import cloud.cinder.web.erc20.controller.dto.AddressTokenDto;
import cloud.cinder.web.erc20.controller.dto.CustomAddressTokenDto;
import cloud.cinder.web.erc20.service.CustomERC20Service;
import cloud.cinder.web.token.service.TokenService;
import cloud.cinder.ethereum.erc20.service.ERC20Service;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;


@Controller
@RequestMapping(value = "/address/{address}")
public class ERC20Controller {

    private final DecimalFormat formatter = new DecimalFormat("##.######");

    private ERC20Service erc20Service;
    private TokenService tokenService;
    private TokenPriceService tokenPriceService;
    private CustomERC20Service customERC20Service;

    public ERC20Controller(final ERC20Service erc20Service, final TokenService tokenService, final TokenPriceService tokenPriceService, final CustomERC20Service customERC20Service) {
        this.erc20Service = erc20Service;
        this.tokenService = tokenService;
        this.tokenPriceService = tokenPriceService;
        this.customERC20Service = customERC20Service;
    }

    @RequestMapping(value = "/tokens")
    public String balanceOf(final @PathVariable("address") String address, final ModelMap modelmap) {
        List<AddressTokenDto> tokens = getTokens(address);
        List<CustomAddressTokenDto> customTokens = getCustomTokens(address);
        if (tokens.size() > 0) {
            tokenService.importAddress(address);
        }
        modelmap.put("tokens", tokens);
        modelmap.putIfAbsent("customTokens", customTokens);
        return "addresses/tokens :: tokenlist";
    }

    private List<AddressTokenDto> getTokens(final String address) {
        return tokenService.findAll()
                .stream()
                .map(x -> {
                    final double rawBalance = erc20Service.balanceOf(address, x.getAddress()).doubleValue();
                    return new AddressTokenDto(
                            x,
                            formatter.format(rawBalance),
                            rawBalance,
                            tokenPriceService.getPriceAsString(x.getSymbol(), Currency.EUR, rawBalance),
                            tokenPriceService.getPriceAsString(x.getSymbol(), Currency.USD, rawBalance)
                    );
                })
                .filter(x -> x.getRawBalance() > 0)
                .collect(Collectors.toList());
    }

    private List<CustomAddressTokenDto> getCustomTokens(final String address) {
        return customERC20Service.findAll(address)
                .stream()
                .map(x -> {
                    final double rawBalance = erc20Service.balanceOf(address, x.getAddress()).doubleValue();
                    return new CustomAddressTokenDto(
                            x,
                            formatter.format(rawBalance),
                            rawBalance,
                            tokenPriceService.getPriceAsString(x.getSymbol(), Currency.EUR, rawBalance),
                            tokenPriceService.getPriceAsString(x.getSymbol(), Currency.USD, rawBalance)
                    );
                })
                .filter(x -> x.getRawBalance() > 0)
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/token-transfers")
    public String tokenTransfers(final @PathVariable("address") String address, final ModelMap modelmap) {
        modelmap.put("tokenTransfers", tokenService.findTransfersByFromOrTo(address));
        return "addresses/tokens :: transfers";
    }

    @RequestMapping(value = "/has-token-transfers")
    @ResponseBody
    public boolean hasTokenTransfers(final @PathVariable("address") String address) {
        return tokenService.hasTokenTransfersFromOrTo(address);
    }
}
