package cloud.cinder.web.address.controller;

import cloud.cinder.web.address.controller.vo.AddressVO;
import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.web.block.service.BlockService;
import cloud.cinder.web.coinmarketcap.dto.Currency;
import cloud.cinder.web.coinmarketcap.service.PriceService;
import cloud.cinder.web.token.service.TokenService;
import cloud.cinder.web.transaction.service.TransactionService;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import cloud.cinder.ethereum.util.EthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;
import rx.Observable;

import java.math.BigInteger;
import java.util.Optional;

import static cloud.cinder.ethereum.util.EthUtil.format;
import static cloud.cinder.ethereum.util.EthUtil.prettifyAddress;

@Controller
@RequestMapping("/address")
public class AddressController {


    @Autowired
    private TransactionService transactionService;
    @Autowired
    private BlockService blockService;
    @Autowired
    private AddressService addressService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private TokenService tokenService;

    @RequestMapping("/{hash}")
    public DeferredResult<ModelAndView> getAddress(@PathVariable("hash") final String hash) {
        validateAddress(hash);

        if (isToken(hash)) {
            final DeferredResult<ModelAndView> result = new DeferredResult<>();
            result.setResult(new ModelAndView("redirect:/token/" + hash));
            return result;
        }

        final String address = prettifyAddress(hash);
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        final ModelAndView modelAndView = new ModelAndView("addresses/address");
        final Observable<String> code = addressService.getCode(address);
        final Observable<Slice<Transaction>> transactions = transactionService.findByAddress(address, new PageRequest(0, 10));
        final Observable<BigInteger> transactionCount = addressService.getTransactionCount(address);
        final Observable<BigInteger> balance = addressService.getBalance(address);
        final Optional<SpecialAddress> specialAddress = addressService.findByAddress(address);
        Observable.zip(code, transactions, transactionCount, balance, (cde, tx, count, bal) -> {
            final Slice<Transaction> convertedSlice = tx.map(x -> transactionService.enrichWithSpecialAddresses(x));
            modelAndView.addObject("address", new AddressVO(cde, format(bal), count, convertedSlice));
            modelAndView.addObject("balEUR", priceService.getPrice(Currency.EUR) * EthUtil.asEth(bal));
            modelAndView.addObject("balUSD", priceService.getPrice(Currency.USD) * EthUtil.asEth(bal));
            modelAndView.addObject("isSpecial", specialAddress.isPresent());
            modelAndView.addObject("hash", address);
            modelAndView.addObject("specialName", specialAddress.map(SpecialAddress::getName).orElse(""));
            return modelAndView;
        }).subscribe(result::setResult);
        return result;
    }

    @RequestMapping("/{hash}/has-mined-blocks")
    @ResponseBody
    public boolean hasMinedblocks(@PathVariable("hash") String hash) {
        return blockService.findByMiner(prettifyAddress(hash), new PageRequest(0, 10)).first().toBlocking().first().hasContent();
    }


    @RequestMapping("/{hash}/mined-blocks")
    public String minedBlocks(@PathVariable("hash") String hash,
                              final ModelMap modelMap) {
        modelMap.put("minedBlocks", blockService.findByMiner(prettifyAddress(hash), new PageRequest(0, 10)).first().toBlocking().first());
        return "components/addresses :: mined_blocks";
    }

    private void validateAddress(final String hash) {
        if (hash == null || hash.isEmpty() || noValidAddress(hash)) {
            throw new IllegalArgumentException("Not a valid address: " + hash);
        }
    }


    private boolean isToken(final String hash) {
        return tokenService.findByAddress(hash).isPresent();
    }

    private boolean noValidAddress(final String hash) {
        return !(hash.length() == 40 || hash.length() == 42);
    }
}
