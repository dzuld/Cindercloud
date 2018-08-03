package cloud.cinder.web.vechain.transaction.controller;

import cloud.cinder.core.vechain.block.service.VechainTransactionService;
import cloud.cinder.vechain.transaction.VechainTransaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/vechain/tx")
public class VechainTransactionController {

    @Autowired
    private VechainTransactionService transactionService;

    @RequestMapping(method = GET)
    public String getTransactions(final Pageable pageable,
                                  final ModelMap modelMap,
                                  @RequestParam(name = "q", required = false) final Optional<String> searchKey,
                                  @RequestParam(name = "block", required = false) final Optional<String> block) {
        modelMap.put("transactions", transactionService.getLastTransactions(pageable));
        modelMap.put("q", "");
        modelMap.put("block", "");
        return "vechain/transactions/list";
    }

    private boolean containsParameter(final @RequestParam(name = "q", required = false) Optional<String> searchKey) {
        return searchKey
                .filter(x -> !x.isEmpty())
                .isPresent();
    }

    @RequestMapping(value = "/{hash}", method = GET)
    public String getTransaction(@PathVariable("hash") final String hash,
                                 final ModelMap modelMap) {
        final Optional<VechainTransaction> transaction = transactionService.getTransaction(hash);
        if (transaction.isPresent()) {
            modelMap.put("tx", transaction.get());
            return "vechain/transactions/transaction";
        } else {
            return "error";
        }
    }
}
