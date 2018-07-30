package cloud.cinder.web.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/wallet/login")
public class LoginController {

    @Value("${cloud.cinder.wallet.baseUrl}")
    private String walletBaseUrl;

    @GetMapping
    public String redirect() {
        return "redirect:/" + walletBaseUrl + "/wallet/login";
    }
}
