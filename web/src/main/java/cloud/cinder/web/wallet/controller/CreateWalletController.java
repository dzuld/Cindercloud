package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.wallet.controller.command.create.CreateKeystoreCommand;
import cloud.cinder.web.wallet.domain.GeneratedCredentials;
import cloud.cinder.web.wallet.service.WalletService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = "/wallet/create")
public class CreateWalletController {

    @Autowired
    private WalletService walletService;

    @RequestMapping(method = RequestMethod.GET)
    public String createIndex(final ModelMap modelMap) {
        modelMap.put("createKeystoreCommand", new CreateKeystoreCommand());
        return "wallets/create";
    }

    @RequestMapping(method = RequestMethod.POST, params = "type=keystore")
    public String createKeystore(final ModelMap modelMap,
                                 @ModelAttribute("createKeystore") final CreateKeystoreCommand createKeystoreCommand) {
        if (StringUtils.isEmpty(createKeystoreCommand.getPassword())) {
            modelMap.put("error", "Your password should be at least 8 characters long");
            return "wallets/create";
        }
        final GeneratedCredentials wallet = walletService.generateWallet(createKeystoreCommand.getPassword(), createKeystoreCommand.isSecure());
        modelMap.put("wallet", wallet);
        return "wallets/created_keystore";
    }

    @RequestMapping(method = RequestMethod.POST, params = "type=mnemonic")
    public String createMnemonic(final ModelMap modelMap) {
        modelMap.put("mnemonic", walletService.generateMnemonic());
        return "wallets/created_mnemonic";
    }


}
