package cloud.cinder.web.tools.controller;

import cloud.cinder.web.tools.controller.command.TestPrivateKeysCommand;
import cloud.cinder.web.tools.service.PrivateKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/tools/private-key-testing")
public class PrivateKeyTestingToolController {

    @Autowired
    private PrivateKeyService privateKeyService;

    @RequestMapping(method = RequestMethod.GET)
    public String index(final ModelMap modelMap) {
        modelMap.put("testPrivateKeysCommand", new TestPrivateKeysCommand());
        return "tools/private_key_testing";
    }

    @RequestMapping(method = RequestMethod.POST)
    public String check(final @ModelAttribute("testPrivateKeysCommand") TestPrivateKeysCommand testPrivateKeysCommand,
                        final ModelMap modelMap) {
        modelMap.put("testPrivateKeysCommand", testPrivateKeysCommand);
        modelMap.put("results", privateKeyService.check(testPrivateKeysCommand.toPrivateKeys()));
        return "tools/private_key_testing";
    }
}
