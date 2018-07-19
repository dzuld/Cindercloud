package cloud.cinder.web.block.controller;

import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.web.block.service.BlockService;
import cloud.cinder.web.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
@RequestMapping(value = "/uncles")
public class UncleController {

    @Autowired
    private BlockService blockService;
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private AddressService addressService;

    @RequestMapping(method = RequestMethod.GET)
    public String blocks(final ModelMap modelMap,
                         final @RequestParam("q") Optional<String> searchKey,
                         final Pageable pageable) {
        if (searchKey.isPresent()) {
            modelMap.put("blocks", blockService.searchUncles(searchKey.get(), pageable));
            modelMap.put("q", searchKey.get());
        } else {
            modelMap.put("blocks", blockService.getLastUncles(pageable));
            modelMap.put("q", "");
        }
        return "uncles/list";
    }

    @RequestMapping(value = "/{hash}")
    public DeferredResult<ModelAndView> getUncle(@PathVariable("hash") final String hash) {
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        blockService.getUncle(hash).subscribe(block -> {
            ModelAndView modelAndView = new ModelAndView("blocks/block");
            modelAndView.addObject("block", block);
            final Optional<SpecialAddress> specialMinedBy = addressService.findByAddress(block.getMinedBy());
            modelAndView.addObject("isMinedBySpecialName", specialMinedBy.isPresent());
            modelAndView.addObject("minedBySpecialName", specialMinedBy.map(SpecialAddress::getName).orElse(""));
            result.setResult(
                    modelAndView
            );
        });
        return result;
    }

    @RequestMapping(value = "/{hash}/transactions")
    public DeferredResult<ModelAndView> getTransactionsForBlock(@PathVariable("hash") final String hash) {
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        transactionService.getTransactionsForBlock(hash, new PageRequest(0, 20))
                .map(x -> {
                    final ModelAndView modelAndView = new ModelAndView("blocks/transactions :: blockTransactions");
                    modelAndView.addObject("transactions", x);
                    modelAndView.addObject("hash", hash);
                    return modelAndView;
                })
                .subscribe(result::setResult);
        return result;
    }

}
