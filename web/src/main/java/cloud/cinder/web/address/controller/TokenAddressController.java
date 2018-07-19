package cloud.cinder.web.address.controller;

import cloud.cinder.ethereum.abi.AbiDecoder;
import cloud.cinder.ethereum.abi.AbiService;
import cloud.cinder.ethereum.abi.domain.AbiContractFunction;
import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.cryptocompare.service.TokenPriceService;
import cloud.cinder.ethereum.token.domain.Token;
import cloud.cinder.web.token.dto.TokenTransferDto;
import cloud.cinder.web.token.service.TokenService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping(value = "/token")
public class TokenAddressController {

    private TokenService tokenService;
    private TokenPriceService tokenPriceService;
    private AbiDecoder abiDecoder;
    private AbiService abiService;


    public TokenAddressController(final TokenService tokenService,
                                  final TokenPriceService tokenPriceService,
                                  final AbiDecoder abiDecoder,
                                  final AbiService abiService) {
        this.tokenService = tokenService;
        this.tokenPriceService = tokenPriceService;
        this.abiDecoder = abiDecoder;
        this.abiService = abiService;
    }

    @RequestMapping(value = "/{address}")
    public String byAddress(@PathVariable("address") final String address,
                            final ModelMap modelMap) {
        final Optional<Token> byAddress = tokenService.findByAddress(address);
        if (byAddress.isPresent()) {
            modelMap.put("token", byAddress.get());
            modelMap.put("price_eur", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.EUR));
            modelMap.put("price_usd", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.USD));
            modelMap.put("price_eth", tokenPriceService.getPriceAsString(byAddress.get().getSymbol(), Currency.ETH));
            return "addresses/token";
        } else {
            return "redirect:/address/" + address;
        }
    }

    @RequestMapping(value = "/{address}/transfers")
    public String getTokenTransfers(@PathVariable("address") final String address,
                                    final ModelMap modelMap) {
        List<TokenTransferDto> tokens = tokenService.findByToken(address);
        modelMap.put("tokenTransfers", tokens);
        return "addresses/tokens :: transfers";
    }

    @RequestMapping(value = "/{address}/access-contract")
    public String accessERC20(final ModelMap modelMap, @PathVariable("address") String address) {
        try {
            final String erc20Abi = abiService.getERC20Abi();
            modelMap.addAttribute("abi", erc20Abi);
            modelMap.addAttribute("address", address);
            modelMap.addAttribute("abiContract", abiDecoder.decode(erc20Abi.trim())
                    .getElements()
                    .stream()
                    .filter(x -> x instanceof AbiContractFunction).map(x -> (AbiContractFunction) x)
                    .filter(AbiContractFunction::isConstant)
                    .collect(Collectors.toList())
            );
            return "components/access-contract :: erc20";
        } catch (final Exception ex) {
            return "";
        }
    }
}
