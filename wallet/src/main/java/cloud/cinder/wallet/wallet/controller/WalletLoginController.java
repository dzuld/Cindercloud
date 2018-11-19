package cloud.cinder.wallet.wallet.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Hex;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;
import java.util.UUID;

@RequestMapping(value = "/wallet")
@Controller
@Slf4j
public class WalletLoginController {

    @GetMapping(value = "/login")
    public String index(final ModelMap modelMap) {
        modelMap.put("randomChallenge", Hex.encodeHexString(UUID.randomUUID().toString().getBytes()));
        modelMap.put("visualChallenge", "Cindercloud @ " + LocalDateTime.now().toString());
        return "wallets/login";
    }
}
