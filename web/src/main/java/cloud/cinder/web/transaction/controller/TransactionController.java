package cloud.cinder.web.transaction.controller;

import cloud.cinder.web.transaction.service.TransactionService;
import cloud.cinder.ethereum.parity.domain.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping(value = "/tx")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @RequestMapping(method = GET)
    public String getTransactions(final Pageable pageable,
                                  final ModelMap modelMap,
                                  @RequestParam(name = "q", required = false) final Optional<String> searchKey,
                                  @RequestParam(name = "block", required = false) final Optional<String> block) {
        if (containsParameter(searchKey) || containsParameter(block)) {
            if (containsParameter(searchKey) && containsParameter(block)) {
                modelMap.put("transactions", transactionService.findByBlockAndAddress(block.get(), searchKey.get(), pageable));
            } else if (containsParameter(searchKey)) {
                modelMap.put("transactions", transactionService.findByAddress(searchKey.get(), pageable).toBlocking().first());
            } else {
                modelMap.put("transactions", transactionService.findByBlock(block.get(), pageable));
            }
            modelMap.put("q", searchKey.orElse(""));
            modelMap.put("block", block.orElse(""));
        } else {
            modelMap.put("transactions", transactionService.getLastTransactions(pageable));
            modelMap.put("q", "");
            modelMap.put("block", "");
        }
        return "transactions/list";
    }

    private boolean containsParameter(final @RequestParam(name = "q", required = false) Optional<String> searchKey) {
        return searchKey
                .filter(x -> !x.isEmpty())
                .isPresent();
    }

    @RequestMapping(value = "/{hash}", method = GET)
    public DeferredResult<ModelAndView> getTransaction(@PathVariable("hash") final String hash) {
        final DeferredResult<ModelAndView> result = new DeferredResult<>();
        transactionService.getTransaction(hash).subscribe(tx -> {
            final ModelAndView modelAndView = new ModelAndView("transactions/transaction");
            modelAndView.addObject("tx", tx);
            result.setResult(modelAndView);
        }, onError -> result.setErrorResult("error"));
        return result;
    }

    @RequestMapping(value = "/{hash}/method-signature")
    public @ResponseBody
    MethodSignature getMethodSignature(@PathVariable("hash") final String hash) {
        return transactionService.getMethodSignature(hash).orElse(null);
    }
}
