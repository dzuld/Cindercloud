package cloud.cinder.wallet.wallet.controller;

import cloud.cinder.core.wallet.service.AuthenticationService;
import cloud.cinder.core.wallet.service.command.ConfirmEtherTransactionCommand;
import cloud.cinder.wallet.wallet.controller.command.create.CreateEtherTransactionCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class CreateEtherTransactionController {

    @Autowired
    private AuthenticationService authenticationService;

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
}
