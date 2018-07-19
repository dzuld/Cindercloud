package cloud.cinder.web.tools.controller;

import cloud.cinder.web.tools.controller.command.CheckLeakedCredentialsCommand;
import cloud.cinder.web.tools.service.LeakedCredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tools/leaked")
public class LeakedCredentialController {

    @Autowired
    private LeakedCredentialService leakedCredentialService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("checkLeakedCredentialsCommand", new CheckLeakedCredentialsCommand());
        return "tools/leaked-credentials";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String check(final @ModelAttribute("checkLeakedCredentialsCommand") CheckLeakedCredentialsCommand leakedCredentialsCommand,
                        final ModelMap modelMap) {
        modelMap.put("checkLeakedCredentialsCommand", leakedCredentialsCommand);
        modelMap.put("results", leakedCredentialService.check(leakedCredentialsCommand.toCredentials()));
        return "tools/leaked-credentials";
    }

}
