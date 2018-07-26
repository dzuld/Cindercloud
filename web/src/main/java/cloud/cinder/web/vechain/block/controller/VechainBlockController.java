package cloud.cinder.web.vechain.block.controller;

import cloud.cinder.vechain.block.domain.VechainBlock;
import cloud.cinder.vechain.transaction.VechainTransaction;
import cloud.cinder.web.vechain.block.service.VechainBlockService;
import cloud.cinder.web.vechain.block.service.VechainTransactionService;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping(value = "/vechain/blocks")
@Slf4j
public class VechainBlockController {

    private VechainBlockService blockService;
    private VechainTransactionService transactionService;

    public VechainBlockController(final VechainBlockService blockService,
                                  final VechainTransactionService transactionService) {
        this.blockService = blockService;
        this.transactionService = transactionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String blocks(final ModelMap modelMap,
                         final @RequestParam("q") Optional<String> searchKey,
                         final @RequestParam("mined_by") Optional<String> minedBy,
                         final Pageable pageable) {

        modelMap.put("blocks", blockService.getLastBlocks(pageable));
        return "vechain/blocks/list";
    }

    @RequestMapping(value = "/{id}")
    public String getBlock(@PathVariable("id") final String hash,
                           final Model model) {
        try {
            final VechainBlock block = blockService.getBlock(hash).get();
            model.addAttribute("block", block);
            model.addAttribute("isMinedBySpecialName", false);
            return "vechain/blocks/block";
        } catch (final Exception ex) {
            log.debug("Error while trying to fetch block {}", hash);
            return "error";
        }
    }

    @RequestMapping(value = "/{id}/transactions")
    public String getTransactions(@PathVariable("id") final String id,
                                  final Model model) {
        model.addAttribute("id", id);
        final Slice<VechainTransaction> transactions = transactionService.getTransactionsForBlock(id, new PageRequest(0, 20));
        model.addAttribute("transactions", transactions);
        return "vechain/blocks/transactions :: blockTransactions";
    }
}
