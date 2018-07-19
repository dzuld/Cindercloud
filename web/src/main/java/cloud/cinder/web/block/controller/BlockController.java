package cloud.cinder.web.block.controller;

import cloud.cinder.ethereum.address.domain.SpecialAddress;
import cloud.cinder.web.address.service.AddressService;
import cloud.cinder.ethereum.block.domain.Block;
import cloud.cinder.web.block.service.BlockService;
import cloud.cinder.web.transaction.service.TransactionService;
import cloud.cinder.ethereum.transaction.domain.Transaction;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping(value = "/blocks")
@Slf4j
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
                         final @RequestParam("mined_by") Optional<String> minedBy,
                         final Pageable pageable) {
        if (searchKey.isPresent() || minedBy.isPresent()) {
            modelMap.put("blocks", blockService.searchBlocks(searchKey.orElse(""), minedBy.orElse(""), pageable));
            modelMap.put("q", searchKey.orElse(""));
            modelMap.put("mined_by", minedBy.orElse(""));
        } else {
            modelMap.put("blocks", blockService.getLastBlocks(pageable));
            modelMap.put("q", "");
            modelMap.put("mined_by", "");
        }
        return "blocks/list";
    }

    @RequestMapping(value = "/{hash}")
    public String getBlock(@PathVariable("hash") final String hash,
                           final Model model) {
        try {
            final Block block = blockService.getBlock(hash).single().toBlocking().first();
            model.addAttribute("block", block);
            final Optional<SpecialAddress> specialMinedBy = addressService.findByAddress(block.getMinedBy());
            model.addAttribute("isMinedBySpecialName", specialMinedBy.isPresent());
            model.addAttribute("minedBySpecialName", specialMinedBy.map(SpecialAddress::getName).orElse(""));
            return "blocks/block";
        } catch (final Exception ex) {
            log.debug("Error while trying to fetch block {}", hash);
            return "error";
        }
    }

    @RequestMapping(value = "/{hash}/transactions")
    public String getTransactionsForUncle(@PathVariable("hash") final String hash,
                                          final Model model) {
        final Slice<Transaction> transactions = transactionService.getTransactionsForBlock(hash, new PageRequest(0, 20)).toBlocking().first();
        model.addAttribute("transactions", transactions);
        model.addAttribute("hash", hash);
        return "blocks/transactions :: blockTransactions";
    }
}
