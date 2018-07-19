package cloud.cinder.web.whitehat.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@RequestMapping("/faq")
@Controller
public class FAQController {

    @RequestMapping(method = RequestMethod.GET, value = "/whitehat")
    public String faq() {
        return "faq/whitehat";
    }

    @RequestMapping(method = RequestMethod.GET, value = "/how-to-create-a-wallet")
    public String howToCreateAWallet() {
        return "faq/how-to-create-a-wallet";
    }
}
