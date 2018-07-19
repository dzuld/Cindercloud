package cloud.cinder.web.wallet.controller;

import cloud.cinder.web.login.handler.LoginHandler;
import cloud.cinder.web.trezor.TrezorVerificationService;
import cloud.cinder.web.wallet.controller.command.login.TrezorLoginCommand;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hwallet")
public class HardwareWalletLoginController {

    private TrezorVerificationService trezorVerificationService;
    private LoginHandler loginHandler;

    public HardwareWalletLoginController(final TrezorVerificationService trezorVerificationService,
                                         final LoginHandler loginHandler) {
        this.trezorVerificationService = trezorVerificationService;
        this.loginHandler = loginHandler;
    }

    @RequestMapping(value = "/trezor", method = RequestMethod.POST)
    public @ResponseBody
    boolean trezorLogin(@RequestBody TrezorLoginCommand trezorLoginCommand) {
        try {
            loginHandler.trezorLogin(trezorLoginCommand.getXpubkey(), trezorLoginCommand.getPublicKey(), trezorLoginCommand.getChainCode(), trezorLoginCommand.getAddress());
            return true;
        } catch (final Exception ex) {
            return false;
        }
    }
}
