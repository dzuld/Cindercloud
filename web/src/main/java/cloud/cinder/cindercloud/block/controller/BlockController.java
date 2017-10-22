package cloud.cinder.cindercloud.block.controller;

import cloud.cinder.cindercloud.address.model.SpecialAddress;
import cloud.cinder.cindercloud.address.service.AddressService;
import cloud.cinder.cindercloud.block.service.BlockService;
import cloud.cinder.cindercloud.transaction.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping(value = "/blocks")
public class BlockController {

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
            modelMap.put("blocks", blockService.search(searchKey.get(), pageable));
            modelMap.put("q", searchKey.get());
        } else {
            modelMap.put("blocks", blockService.getLastBlocks(pageable));
            modelMap.put("q", "");
        }
        return "blocks/list";
    }

    @RequestMapping(value = "/{hash}")
    public DeferredResult<ModelAndView> getBlock(@PathVariable("hash") final String hash) {
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        blockService.getBlock(hash).subscribe(block -> {
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
        transactionService.getTransactionsForBlock(hash)
                .toList()
                .map(x -> {
                    final ModelAndView modelAndView = new ModelAndView("blocks/transactions :: blockTransactions");
                    modelAndView.addObject("transactions", x);
                    return modelAndView;
                })
                .subscribe(result::setResult);
        return result;
    }
}
