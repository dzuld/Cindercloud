package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.login.handler.LoginHandler;
import cloud.cinder.web.wallet.controller.command.login.KeystoreLoginCommand;
import cloud.cinder.web.wallet.controller.command.login.MnemonicLoginCommand;
import cloud.cinder.web.wallet.controller.command.login.PrivateKeyLoginCommand;
import cloud.cinder.web.wallet.controller.command.mnemonic.MnemonicAddressesRequest;
import cloud.cinder.web.wallet.controller.dto.MnemonicAddressDto;
import cloud.cinder.web.wallet.service.WalletService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.web3j.crypto.Credentials;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RequestMapping(value = "/wallet")
@Controller
@Slf4j
public class WalletLoginController {

    private final WalletService walletService;
    private final LoginHandler loginHandler;

    public WalletLoginController(final WalletService walletService,
                                 final LoginHandler loginHandler) {
        this.walletService = walletService;
        this.loginHandler = loginHandler;
    }

    @GetMapping(value = "/login")
    public String index(final ModelMap modelMap) {
        modelMap.put("keystoreLoginCommand", new KeystoreLoginCommand());
        modelMap.put("privatekeyLoginCommand", new PrivateKeyLoginCommand());
        modelMap.put("mnemonicAddressRequest", new MnemonicAddressesRequest());
        modelMap.put("randomChallenge", Hex.encodeHexString(UUID.randomUUID().toString().getBytes()));
        modelMap.put("visualChallenge", "Cindercloud @ " + LocalDateTime.now().toString());
        return "wallets/login";
    }

    @RequestMapping(method = POST, value = "/login", params = "type=keystore")
    public String loginWithKeystore(final @ModelAttribute("keystoreLoginCommand") KeystoreLoginCommand keystoreLoginCommand,
                                    final RedirectAttributes redirectAttributes) {
        try {
            final Credentials creds = walletService.loginWithWallet(keystoreLoginCommand.getPassword(), keystoreLoginCommand.getKeystoreValue());
            loginHandler.login(creds);
            return "redirect:/wallet";
        } catch (final Exception ex) {
            redirectAttributes.addFlashAttribute("keystoreError", ex.getMessage());
            return "redirect:/wallet/login";
        }
    }

    @RequestMapping(method = POST, value = "/login", params = "type=privatekey")
    public String loginWithPrivateKey(final @ModelAttribute("keystoreLoginCommand") PrivateKeyLoginCommand privateKeyLoginCommand,
                                      final RedirectAttributes redirectAttributes) {
        try {
            final Credentials creds = walletService.loginWithPrivateKey(privateKeyLoginCommand.getPrivateKey());
            loginHandler.login(creds);
            return "redirect:/wallet";
        } catch (final Exception ex) {
            log.debug("Error while trying to login with invalid private key", privateKeyLoginCommand.getPrivateKey());
            redirectAttributes.addFlashAttribute("privateKeyError", ex.getMessage());
            return "redirect:/wallet/login";
        }
    }

    @PostMapping(value = "/mnemonic")
    public String requestAddresses(final @ModelAttribute("mnemonicAddressRequest") MnemonicAddressesRequest mnemonicAddressesRequest,
                                   final ModelMap modelMap) {
        try {
            modelMap.put("mnemonicAddresses", getAddresses(mnemonicAddressesRequest));
            final MnemonicLoginCommand mnemonicLoginCommand = new MnemonicLoginCommand();
            mnemonicLoginCommand.setMnemonic(mnemonicAddressesRequest.getMnemonic());
            modelMap.put("mnemonicLoginCommand", mnemonicLoginCommand);
            return "wallets/mnemonic :: choose";
        } catch (final Exception ex) {
            return "redirect:/";
        }
    }

    private List<MnemonicAddressDto> getAddresses(final @ModelAttribute("mnemonicAddressRequest") MnemonicAddressesRequest mnemonicAddressesRequest) {
        return IntStream.rangeClosed(mnemonicAddressesRequest.getPage(), 9 + mnemonicAddressesRequest.getPage())
                .mapToObj(index -> new MnemonicAddressDto(walletService.loginWithMnemonic(mnemonicAddressesRequest.getMnemonic(), index).getAddress(), index))
                .collect(Collectors.toList());
    }

    @RequestMapping(method = POST, value = "/login", params = "type=mnemonic")
    public String loginWithMnemonic(final @ModelAttribute("mnemonicLoginCommand") MnemonicLoginCommand mnemonicLoginCommand,
                                    final RedirectAttributes redirectAttributes) {
        try {
            final Credentials creds = walletService.loginWithMnemonic(mnemonicLoginCommand.getMnemonic(), mnemonicLoginCommand.getIndex());
            loginHandler.login(creds);
            return "redirect:/wallet";
        } catch (final Exception ex) {
            log.debug("Error while trying to login with invalid mnemonic and idx", mnemonicLoginCommand.getMnemonic());
            redirectAttributes.addFlashAttribute("mnemonicError", ex.getMessage());
            return "redirect:/wallet/login";
        }
    }
}
