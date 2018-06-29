package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.security.domain.AuthenticationType;
import cloud.cinder.web.wallet.controller.command.confirm.ConfirmEtherTransactionCommand;
import cloud.cinder.web.wallet.controller.command.create.CreateEtherTransactionCommand;
import cloud.cinder.web.wallet.service.AuthenticationService;
import cloud.cinder.web.wallet.service.Web3TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class CreateEtherTransactionController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private Web3TransactionService web3TransactionService;

    @RequestMapping(method = RequestMethod.GET, value = "/send")
    public String index(final ModelMap modelMap,
                        @RequestParam("to") final Optional<String> to) {
        authenticationService.requireAuthenticated();
        final String address = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        modelMap.put("address", address);
        final CreateEtherTransactionCommand createCommand = new CreateEtherTransactionCommand();
        createCommand.setTo(to.orElse(""));
        modelMap.put("createEtherTransactionCommand", createCommand);
        return "wallets/send";
    }

    @RequestMapping(method = RequestMethod.POST, value = "/send")
    public String createTransaction(@Valid @ModelAttribute("createEtherTransactionCommand") final CreateEtherTransactionCommand createEtherTransactionCommand,
                                    final BindingResult bindingResult,
                                    final ModelMap modelMap) {
        if (bindingResult.hasErrors()) {
            return index(modelMap, Optional.empty());
        } else {
            modelMap.addAttribute("authenticationType", authenticationService.getType());
            modelMap.put("confirm", new ConfirmEtherTransactionCommand(
                    createEtherTransactionCommand.getTo(),
                    createEtherTransactionCommand.getGasPrice(),
                    createEtherTransactionCommand.gasPriceInWei(),
                    createEtherTransactionCommand.getGasLimit(),
                    createEtherTransactionCommand.getAmount(),
                    createEtherTransactionCommand.amountInWei()
            ));
            return "wallets/confirm";
        }
    }

    @RequestMapping(method = RequestMethod.POST, value = "/confirm")
    public String confirmTransaction(@Valid @ModelAttribute("confirm") final ConfirmEtherTransactionCommand confirmEtherTransactionCommand,
                                     final BindingResult bindingResult,
                                     final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to confirm your transaction");
            return "redirect:/wallet/send";
        } else {
            if (authenticationService.getType().equals(AuthenticationType.CINDERCLOUD)) {
                //do transaction
                try {
                    final String transactionHash = web3TransactionService.submitEtherTransaction(confirmEtherTransactionCommand);
                    redirectAttributes.addFlashAttribute("success", "Your transaction has been submitted to the network: " + transactionHash);
                    return "redirect:/wallet/send";
                } catch (final AuthenticationException e) {
                    throw e;
                } catch (final Exception ex) {
                    redirectAttributes.addFlashAttribute("error", ex.getMessage());
                    return "redirect:/wallet/send";
                }
            } else {
                redirectAttributes.addFlashAttribute("error", "Something went wrong while trying to execute your transaction, please try again");
                return "redirect:/wallet/send";
            }
        }
    }
}
